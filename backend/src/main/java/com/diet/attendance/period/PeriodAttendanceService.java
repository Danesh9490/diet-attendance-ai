package com.diet.attendance.period;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PeriodAttendanceService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final File file =
            new File("data/period-attendance.json");

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PeriodAttendanceService() {
        initializeFile();
    }

    private void initializeFile() {

        try {

            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if (!file.exists()) {

                objectMapper.writeValue(
                        file,
                        new ArrayList<PeriodAttendance>()
                );
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to initialize period attendance file",
                    e
            );
        }
    }

    private List<PeriodAttendance> readRecords() {

        try {

            if (!file.exists()) {
                initializeFile();
            }

            return objectMapper.readValue(
                    file,
                    new TypeReference<List<PeriodAttendance>>() {
                    }
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to read period attendance records",
                    e
            );
        }
    }

    private void saveRecords(
            List<PeriodAttendance> records) {

        try {

            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(file, records);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to save period attendance records",
                    e
            );
        }
    }

    public synchronized PeriodAttendance markAttendance(
            String studentId,
            String studentName,
            String facultyId,
            String facultyName,
            String department,
            String subject,
            Integer period,
            String date,
            String startTime,
            String endTime,
            String status,
            String room) {

        if (period == null ||
                period < 1 ||
                period > 8) {

            throw new IllegalArgumentException(
                    "Period must be between 1 and 8."
            );
        }

        if (studentId == null ||
                studentId.isBlank()) {

            throw new IllegalArgumentException(
                    "Student ID is required."
            );
        }

        if (subject == null ||
                subject.isBlank()) {

            throw new IllegalArgumentException(
                    "Subject is required."
            );
        }

        if (status == null ||
                status.isBlank()) {

            throw new IllegalArgumentException(
                    "Attendance status is required."
            );
        }

        if (date == null ||
                date.isBlank()) {

            date = LocalDate.now()
                    .format(dateFormatter);
        }

        List<PeriodAttendance> records =
                readRecords();

        String finalDate = date;

        PeriodAttendance existing =
                records.stream()
                        .filter(r ->
                                studentId.equalsIgnoreCase(
                                        r.getStudentId())
                                &&
                                subject.equalsIgnoreCase(
                                        r.getSubject())
                                &&
                                period.equals(
                                        r.getPeriod())
                                &&
                                finalDate.equals(
                                        r.getDate())
                        )
                        .findFirst()
                        .orElse(null);

        if (existing != null) {

            existing.setStatus(status);

            existing.setFacultyId(facultyId);

            existing.setFacultyName(facultyName);

            existing.setRoom(room);

            existing.setStartTime(startTime);

            existing.setEndTime(endTime);

            existing.setMarkedAt(
                    LocalDateTime.now()
                            .format(dateTimeFormatter)
            );

            saveRecords(records);

            return existing;
        }

        PeriodAttendance record =
                new PeriodAttendance();

        record.setId(
                UUID.randomUUID().toString()
        );

        record.setStudentId(studentId);

        record.setStudentName(studentName);

        record.setFacultyId(facultyId);

        record.setFacultyName(facultyName);

        record.setDepartment(department);

        record.setSubject(subject);

        record.setPeriod(period);

        record.setDate(date);

        record.setStartTime(startTime);

        record.setEndTime(endTime);

        record.setStatus(status);

        record.setRoom(room);

        record.setMarkedAt(
                LocalDateTime.now()
                        .format(dateTimeFormatter)
        );

        records.add(record);

        saveRecords(records);

        return record;
    }

    public List<PeriodAttendance> getAll() {
        return readRecords();
    }

    public List<PeriodAttendance> getStudent(
            String studentId) {

        return readRecords()
                .stream()
                .filter(r ->
                        studentId.equalsIgnoreCase(
                                r.getStudentId()))
                .collect(Collectors.toList());
    }

    public List<PeriodAttendance> getDepartment(
            String department) {

        return readRecords()
                .stream()
                .filter(r ->
                        department.equalsIgnoreCase(
                                r.getDepartment()))
                .collect(Collectors.toList());
    }

    public List<PeriodAttendance> getByDate(
            String date) {

        return readRecords()
                .stream()
                .filter(r ->
                        date.equals(r.getDate()))
                .collect(Collectors.toList());
    }

    public List<PeriodAttendance> getByPeriod(
            Integer period) {

        return readRecords()
                .stream()
                .filter(r ->
                        period.equals(r.getPeriod()))
                .collect(Collectors.toList());
    }

    public List<PeriodAttendance> getBySubject(
            String subject) {

        return readRecords()
                .stream()
                .filter(r ->
                        subject.equalsIgnoreCase(
                                r.getSubject()))
                .collect(Collectors.toList());
    }

    public synchronized boolean delete(
            String id) {

        List<PeriodAttendance> records =
                readRecords();

        boolean removed =
                records.removeIf(
                        r -> id.equals(r.getId())
                );

        if (removed) {
            saveRecords(records);
        }

        return removed;
    }

    public synchronized void clearAll() {

        saveRecords(
                new ArrayList<PeriodAttendance>()
        );
    }
}