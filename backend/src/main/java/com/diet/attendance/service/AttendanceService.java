package com.diet.attendance.service;

import com.diet.attendance.model.Person;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceService {

    private final DirectoryService directoryService;

    private final Path dataFile =
            Paths.get("data", "attendance.json");

    private final List<AttendanceEntry> entries =
            new ArrayList<>();

    public AttendanceService(
            DirectoryService directoryService) {

        this.directoryService = directoryService;
        load();
    }

    public synchronized AttendanceEntry markAttendance(
            String facultyId,
            String studentId,
            String subject,
            String status) {

        Person faculty =
                directoryService.findById(facultyId);

        Person student =
                directoryService.findById(studentId);

        if (faculty == null) {
            throw new IllegalArgumentException(
                    "Faculty account not found."
            );
        }

        if (!"FACULTY".equalsIgnoreCase(faculty.getRole())) {
            throw new IllegalArgumentException(
                    "Only faculty can mark attendance."
            );
        }

        if (student == null) {
            throw new IllegalArgumentException(
                    "Student account not found."
            );
        }

        if (!"STUDENT".equalsIgnoreCase(student.getRole())) {
            throw new IllegalArgumentException(
                    "Attendance can only be marked for students."
            );
        }

        if (!sameDepartment(
                faculty.getDepartment(),
                student.getDepartment())) {

            throw new IllegalArgumentException(
                    "Faculty and student must belong to the same department."
            );
        }

        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Subject is required."
            );
        }

        String cleanStatus =
                status == null
                        ? "PRESENT"
                        : status.trim().toUpperCase();

        if (!cleanStatus.equals("PRESENT")
                && !cleanStatus.equals("ABSENT")) {

            throw new IllegalArgumentException(
                    "Status must be PRESENT or ABSENT."
            );
        }

        String cleanStudentId = studentId.trim();
        String cleanSubject = subject.trim();
        String today = LocalDate.now().toString();

        for (AttendanceEntry entry : entries) {

            if (entry.date.equals(today)
                    && entry.studentId.equalsIgnoreCase(cleanStudentId)
                    && entry.subject.equalsIgnoreCase(cleanSubject)) {

                entry.status = cleanStatus;
                entry.markedBy = faculty.getId();
                entry.markedAt = LocalDateTime.now().toString();

                save();

                return entry;
            }
        }

        AttendanceEntry entry =
                new AttendanceEntry();

        entry.id =
                String.valueOf(System.currentTimeMillis());

        entry.date = today;
        entry.studentId = student.getId();
        entry.studentName = student.getName();
        entry.department = student.getDepartment();
        entry.subject = cleanSubject;
        entry.status = cleanStatus;
        entry.markedBy = faculty.getId();
        entry.markedAt = LocalDateTime.now().toString();

        entries.add(entry);

        save();

        return entry;
    }

    public synchronized List<AttendanceEntry>
    getStudentAttendance(String studentId) {

        List<AttendanceEntry> result =
                new ArrayList<>();

        if (studentId == null) {
            return result;
        }

        for (AttendanceEntry entry : entries) {

            if (entry.studentId.equalsIgnoreCase(
                    studentId.trim())) {

                result.add(entry);
            }
        }

        return result;
    }

    public synchronized List<AttendanceEntry>
    getDepartmentAttendance(String department) {

        List<AttendanceEntry> result =
                new ArrayList<>();

        if (department == null) {
            return result;
        }

        for (AttendanceEntry entry : entries) {

            if (sameDepartment(
                    entry.department,
                    department)) {

                result.add(entry);
            }
        }

        return result;
    }

    public synchronized List<AttendanceEntry>
    getAllAttendance() {

        return new ArrayList<>(entries);
    }

    public synchronized List<Person>
    getDepartmentStudents(String department) {

        List<Person> result =
                new ArrayList<>();

        for (Person person :
                directoryService.getByDepartment(department)) {

            if ("STUDENT".equalsIgnoreCase(
                    person.getRole())) {

                result.add(person);
            }
        }

        return result;
    }

    public synchronized Map<String, Object>
    getStudentSummary(String studentId) {

        List<AttendanceEntry> records =
                getStudentAttendance(studentId);

        int total = records.size();
        int present = 0;

        for (AttendanceEntry entry : records) {

            if ("PRESENT".equalsIgnoreCase(
                    entry.status)) {

                present++;
            }
        }

        int absent = total - present;

        double percentage =
                total == 0
                        ? 0
                        : present * 100.0 / total;

        Map<String, Object> result =
                new LinkedHashMap<>();

        result.put("studentId", studentId);
        result.put("totalClasses", total);
        result.put("present", present);
        result.put("absent", absent);
        result.put(
                "percentage",
                Math.round(percentage * 100.0) / 100.0
        );
        result.put(
                "status",
                percentage >= 75 ? "GOOD" : "LOW"
        );
        result.put("records", records);

        return result;
    }

    public synchronized Map<String, Object>
    getDepartmentSummary(String department) {

        List<Person> students =
                getDepartmentStudents(department);

        List<AttendanceEntry> records =
                getDepartmentAttendance(department);

        int present = 0;
        int absent = 0;

        for (AttendanceEntry entry : records) {

            if ("PRESENT".equalsIgnoreCase(
                    entry.status)) {

                present++;
            } else {
                absent++;
            }
        }

        int total = present + absent;

        double percentage =
                total == 0
                        ? 0
                        : present * 100.0 / total;

        Map<String, Object> result =
                new LinkedHashMap<>();

        result.put("department", department);
        result.put("students", students.size());
        result.put("attendanceRecords", records.size());
        result.put("present", present);
        result.put("absent", absent);
        result.put(
                "percentage",
                Math.round(percentage * 100.0) / 100.0
        );
        result.put(
                "status",
                percentage >= 75 ? "GOOD" : "LOW"
        );
        result.put("records", records);

        return result;
    }

    private boolean sameDepartment(
            String first,
            String second) {

        if (first == null || second == null) {
            return false;
        }

        return first.trim().equalsIgnoreCase(
                second.trim()
        );
    }

    private void load() {

        try {

            if (!Files.exists(dataFile)) {
                return;
            }

            String json =
                    Files.readString(
                            dataFile,
                            StandardCharsets.UTF_8
                    );

            parseJson(json);

        } catch (Exception e) {

            System.err.println(
                    "Unable to load attendance data: "
                            + e.getMessage()
            );
        }
    }

    private void save() {

        try {

            Path parent = dataFile.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.writeString(
                    dataFile,
                    toJson(),
                    StandardCharsets.UTF_8
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to save attendance data.",
                    e
            );
        }
    }

    private String toJson() {

        StringBuilder json =
                new StringBuilder();

        json.append("[\n");

        for (int i = 0; i < entries.size(); i++) {

            AttendanceEntry e = entries.get(i);

            json.append("  {\n");
            appendJson(json, "id", e.id);
            appendJson(json, "date", e.date);
            appendJson(json, "studentId", e.studentId);
            appendJson(json, "studentName", e.studentName);
            appendJson(json, "department", e.department);
            appendJson(json, "subject", e.subject);
            appendJson(json, "status", e.status);
            appendJson(json, "markedBy", e.markedBy);
            appendJson(json, "markedAt", e.markedAt, false);
            json.append("\n  }");

            if (i < entries.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]");

        return json.toString();
    }

    private void appendJson(
            StringBuilder json,
            String key,
            String value) {

        appendJson(json, key, value, true);
    }

    private void appendJson(
            StringBuilder json,
            String key,
            String value,
            boolean comma) {

        json.append("    \"")
                .append(escape(key))
                .append("\": \"")
                .append(escape(value))
                .append("\"");

        if (comma) {
            json.append(",");
        }

        json.append("\n");
    }

    private String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    private void parseJson(String json) {

        if (json == null ||
                json.trim().length() < 3) {
            return;
        }

        String body = json.trim();

        if (body.startsWith("[")) {
            body = body.substring(1);
        }

        if (body.endsWith("]")) {
            body = body.substring(
                    0,
                    body.length() - 1
            );
        }

        if (body.trim().isEmpty()) {
            return;
        }

        String[] objects =
                body.split(
                        "\\}\\s*,\\s*\\{"
                );

        for (String raw : objects) {

            String object = raw.trim();

            object =
                    object.replaceFirst(
                            "^\\{",
                            ""
                    );

            object =
                    object.replaceFirst(
                            "\\}$",
                            ""
                    );

            AttendanceEntry entry =
                    new AttendanceEntry();

            entry.id = value(object, "id");
            entry.date = value(object, "date");
            entry.studentId =
                    value(object, "studentId");
            entry.studentName =
                    value(object, "studentName");
            entry.department =
                    value(object, "department");
            entry.subject =
                    value(object, "subject");
            entry.status =
                    value(object, "status");
            entry.markedBy =
                    value(object, "markedBy");
            entry.markedAt =
                    value(object, "markedAt");

            if (entry.studentId != null &&
                    !entry.studentId.isBlank()) {

                entries.add(entry);
            }
        }
    }

    private String value(
            String object,
            String key) {

        String search = "\"" + key + "\"";

        int keyIndex =
                object.indexOf(search);

        if (keyIndex < 0) {
            return "";
        }

        int colon =
                object.indexOf(
                        ':',
                        keyIndex
                );

        if (colon < 0) {
            return "";
        }

        int firstQuote =
                object.indexOf(
                        '"',
                        colon + 1
                );

        if (firstQuote < 0) {
            return "";
        }

        int secondQuote =
                object.indexOf(
                        '"',
                        firstQuote + 1
                );

        if (secondQuote < 0) {
            return "";
        }

        return object.substring(
                firstQuote + 1,
                secondQuote
        )
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }

    public static class AttendanceEntry {

        public String id;
        public String date;
        public String studentId;
        public String studentName;
        public String department;
        public String subject;
        public String status;
        public String markedBy;
        public String markedAt;
    }
}