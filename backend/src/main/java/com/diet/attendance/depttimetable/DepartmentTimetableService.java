package com.diet.attendance.depttimetable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DepartmentTimetableService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final File file =
            new File("data/department-timetable.json");

    public DepartmentTimetableService() {
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
                        new ArrayList<DepartmentTimetable>()
                );
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to initialize department timetable file",
                    e
            );
        }
    }

    private List<DepartmentTimetable> readRecords() {

        try {

            if (!file.exists()) {
                initializeFile();
            }

            return objectMapper.readValue(
                    file,
                    new TypeReference<List<DepartmentTimetable>>() {
                    }
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to read department timetable records",
                    e
            );
        }
    }

    private void saveRecords(
            List<DepartmentTimetable> records) {

        try {

            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(file, records);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to save department timetable records",
                    e
            );
        }
    }

    public synchronized DepartmentTimetable save(
            String department,
            String day,
            Integer period,
            String subject,
            String facultyId,
            String facultyName,
            String startTime,
            String endTime,
            String room) {

        if (department == null ||
                department.isBlank()) {

            throw new IllegalArgumentException(
                    "Department is required."
            );
        }

        if (day == null ||
                day.isBlank()) {

            throw new IllegalArgumentException(
                    "Day is required."
            );
        }

        if (period == null ||
                period < 1 ||
                period > 8) {

            throw new IllegalArgumentException(
                    "Period must be between 1 and 8."
            );
        }

        if (subject == null ||
                subject.isBlank()) {

            throw new IllegalArgumentException(
                    "Subject is required."
            );
        }

        List<DepartmentTimetable> records =
                readRecords();

        DepartmentTimetable existing =
                records.stream()
                        .filter(r ->
                                department.equalsIgnoreCase(
                                        r.getDepartment())
                                &&
                                day.equalsIgnoreCase(
                                        r.getDay())
                                &&
                                period.equals(
                                        r.getPeriod())
                        )
                        .findFirst()
                        .orElse(null);

        if (existing != null) {

            existing.setSubject(subject);
            existing.setFacultyId(facultyId);
            existing.setFacultyName(facultyName);
            existing.setStartTime(startTime);
            existing.setEndTime(endTime);
            existing.setRoom(room);

            saveRecords(records);

            return existing;
        }

        DepartmentTimetable record =
                new DepartmentTimetable();

        record.setId(
                UUID.randomUUID().toString()
        );

        record.setDepartment(department);
        record.setDay(day);
        record.setPeriod(period);
        record.setSubject(subject);
        record.setFacultyId(facultyId);
        record.setFacultyName(facultyName);
        record.setStartTime(startTime);
        record.setEndTime(endTime);
        record.setRoom(room);

        records.add(record);

        saveRecords(records);

        return record;
    }

    public List<DepartmentTimetable> getAll() {
        return readRecords();
    }

    public List<DepartmentTimetable> getDepartment(
            String department) {

        return readRecords()
                .stream()
                .filter(r ->
                        department.equalsIgnoreCase(
                                r.getDepartment()))
                .collect(Collectors.toList());
    }

    public List<DepartmentTimetable> getDay(
            String day) {

        return readRecords()
                .stream()
                .filter(r ->
                        day.equalsIgnoreCase(
                                r.getDay()))
                .collect(Collectors.toList());
    }

    public List<DepartmentTimetable> getDepartmentAndDay(
            String department,
            String day) {

        return readRecords()
                .stream()
                .filter(r ->
                        department.equalsIgnoreCase(
                                r.getDepartment())
                        &&
                        day.equalsIgnoreCase(
                                r.getDay()))
                .collect(Collectors.toList());
    }

    public List<DepartmentTimetable> getPeriod(
            Integer period) {

        return readRecords()
                .stream()
                .filter(r ->
                        period.equals(r.getPeriod()))
                .collect(Collectors.toList());
    }

    public synchronized boolean delete(
            String id) {

        List<DepartmentTimetable> records =
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
                new ArrayList<DepartmentTimetable>()
        );
    }
}