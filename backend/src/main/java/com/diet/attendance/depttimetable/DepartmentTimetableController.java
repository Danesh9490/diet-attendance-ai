package com.diet.attendance.depttimetable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/department-timetable")
@CrossOrigin
public class DepartmentTimetableController {

    private final DepartmentTimetableService service;

    public DepartmentTimetableController(
            DepartmentTimetableService service) {

        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(
            @RequestBody DepartmentTimetableRequest request) {

        try {

            DepartmentTimetable record =
                    service.save(
                            request.department,
                            request.day,
                            request.period,
                            request.subject,
                            request.facultyId,
                            request.facultyName,
                            request.startTime,
                            request.endTime,
                            request.room
                    );

            Map<String, Object> response =
                    new HashMap<>();

            response.put("success", true);

            response.put(
                    "message",
                    "Department timetable saved successfully."
            );

            response.put("record", record);

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
                                    "Unable to save department timetable."
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

    @GetMapping("/day/{day}")
    public ResponseEntity<?> day(
            @PathVariable String day) {

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,
                        "records",
                        service.getDay(day)
                )
        );
    }

    @GetMapping("/department/{department}/day/{day}")
    public ResponseEntity<?> departmentAndDay(
            @PathVariable String department,
            @PathVariable String day) {

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,
                        "records",
                        service.getDepartmentAndDay(
                                department,
                                day
                        )
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
                        service.getPeriod(period)
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
                        "Department timetable deleted."
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
                        "All department timetable records cleared."
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
                        "department-timetable",
                        "status",
                        "ONLINE"
                )
        );
    }

    public static class DepartmentTimetableRequest {

        public String department;

        public String day;

        public Integer period;

        public String subject;

        public String facultyId;

        public String facultyName;

        public String startTime;

        public String endTime;

        public String room;
    }
}