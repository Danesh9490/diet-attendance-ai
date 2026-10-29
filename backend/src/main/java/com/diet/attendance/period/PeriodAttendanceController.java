package com.diet.attendance.period;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/period-attendance")
@CrossOrigin
public class PeriodAttendanceController {

    private final PeriodAttendanceService service;

    public PeriodAttendanceController(
            PeriodAttendanceService service) {

        this.service = service;
    }

    @PostMapping("/mark")
    public ResponseEntity<?> mark(
            @RequestBody PeriodAttendanceRequest request) {

        try {

            PeriodAttendance record =
                    service.markAttendance(

                            request.studentId,

                            request.studentName,

                            request.facultyId,

                            request.facultyName,

                            request.department,

                            request.subject,

                            request.period,

                            request.date,

                            request.startTime,

                            request.endTime,

                            request.status,

                            request.room
                    );

            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "success",
                    true
            );

            response.put(
                    "message",
                    "Period attendance saved successfully."
            );

            response.put(
                    "record",
                    record
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "success",
                                    false,
                                    "message",
                                    e.getMessage()
                            )
                    );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            Map.of(
                                    "success",
                                    false,
                                    "message",
                                    "Unable to save period attendance."
                            )
                    );
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> all() {

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,
                        "records",
                        service.getAll()
                )
        );
    }


    @GetMapping("/student/{id}")
    public ResponseEntity<?> student(
            @PathVariable String id) {

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,
                        "records",
                        service.getStudent(id)
                )
        );
    }


    @GetMapping("/department/{department}")
    public ResponseEntity<?> department(
            @PathVariable String department) {

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,
                        "records",
                        service.getDepartment(department)
                )
        );
    }


    @GetMapping("/date/{date}")
    public ResponseEntity<?> date(
            @PathVariable String date) {

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,
                        "records",
                        service.getByDate(date)
                )
        );
    }


    @GetMapping("/period/{period}")
    public ResponseEntity<?> period(
            @PathVariable Integer period) {

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,
                        "records",
                        service.getByPeriod(period)
                )
        );
    }


    @GetMapping("/subject/{subject}")
    public ResponseEntity<?> subject(
            @PathVariable String subject) {

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,
                        "records",
                        service.getBySubject(subject)
                )
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable String id) {

        boolean deleted =
                service.delete(id);

        if (!deleted) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,
                        "message",
                        "Period attendance deleted."
                )
        );
    }


    @DeleteMapping("/clear")
    public ResponseEntity<?> clear() {

        service.clearAll();

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,
                        "message",
                        "All period attendance cleared."
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
                        "period-attendance",
                        "status",
                        "ONLINE"
                )
        );
    }


    public static class PeriodAttendanceRequest {

        public String studentId;

        public String studentName;

        public String facultyId;

        public String facultyName;

        public String department;

        public String subject;

        public Integer period;

        public String date;

        public String startTime;

        public String endTime;

        public String status;

        public String room;
    }
}