package com.diet.attendance.controller;

import com.diet.attendance.model.Person;
import com.diet.attendance.service.AttendanceService;
import com.diet.attendance.service.DirectoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final DirectoryService directoryService;

    public AttendanceController(
            AttendanceService attendanceService,
            DirectoryService directoryService) {

        this.attendanceService =
                attendanceService;

        this.directoryService =
                directoryService;
    }

    @PostMapping("/mark")
    public ResponseEntity<?> mark(
            @RequestBody Map<String, String> request) {

        try {

            String facultyId =
                    request.get("facultyId");

            String studentId =
                    request.get("studentId");

            String subject =
                    request.get("subject");

            String status =
                    request.get("status");

            if (blank(facultyId)
                    || blank(studentId)
                    || blank(subject)) {

                return bad(
                        "Faculty ID, Student ID and subject are required."
                );
            }

            AttendanceService.AttendanceEntry entry =
                    attendanceService.markAttendance(
                            facultyId,
                            studentId,
                            subject,
                            status
                    );

            Map<String, Object> response =
                    new LinkedHashMap<>();

            response.put("success", true);
            response.put(
                    "message",
                    "Attendance saved successfully."
            );
            response.put("attendance", entry);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {

            return bad(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "success",
                                    false,
                                    "message",
                                    "Unable to save attendance."
                            )
                    );
        }
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<?> student(
            @PathVariable String id) {

        try {

            Person person =
                    directoryService.findById(id);

            if (person == null) {

                return notFound(
                        "Student not found."
                );
            }

            if (!"STUDENT".equalsIgnoreCase(
                    person.getRole())) {

                return bad(
                        "The supplied ID is not a student account."
                );
            }

            return ResponseEntity.ok(
                    attendanceService
                            .getStudentSummary(id)
            );

        } catch (Exception e) {

            return serverError(
                    "Unable to load student attendance."
            );
        }
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<?> department(
            @PathVariable String department) {

        if (blank(department)) {

            return bad(
                    "Department is required."
            );
        }

        return ResponseEntity.ok(
                attendanceService
                        .getDepartmentSummary(
                                department
                        )
        );
    }

    @GetMapping("/all")
    public ResponseEntity<?> all() {

        return ResponseEntity.ok(
                attendanceService
                        .getAllAttendance()
        );
    }

    @GetMapping("/students/{department}")
    public ResponseEntity<?> students(
            @PathVariable String department) {

        if (blank(department)) {

            return bad(
                    "Department is required."
            );
        }

        return ResponseEntity.ok(
                attendanceService
                        .getDepartmentStudents(
                                department
                        )
        );
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,
                        "service",
                        "attendance",
                        "status",
                        "ONLINE"
                )
        );
    }

    private boolean blank(String value) {

        return value == null ||
                value.trim().isEmpty();
    }

    private ResponseEntity<?> bad(
            String message) {

        return ResponseEntity
                .badRequest()
                .body(
                        Map.of(
                                "success",
                                false,
                                "message",
                                message == null
                                        ? "Invalid request."
                                        : message
                        )
                );
    }

    private ResponseEntity<?> notFound(
            String message) {

        return ResponseEntity
                .status(
                        HttpStatus.NOT_FOUND
                )
                .body(
                        Map.of(
                                "success",
                                false,
                                "message",
                                message
                        )
                );
    }

    private ResponseEntity<?> serverError(
            String message) {

        return ResponseEntity
                .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(
                        Map.of(
                                "success",
                                false,
                                "message",
                                message
                        )
                );
    }
}