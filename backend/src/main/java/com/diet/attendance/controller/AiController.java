package com.diet.attendance.controller;

import com.diet.attendance.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AiController {

    private final AttendanceService attendanceService;

    public AiController(
            AttendanceService attendanceService) {

        this.attendanceService =
                attendanceService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(
            @RequestBody(required = false)
            Map<String, Object> request) {

        try {

            if (request == null) {
                request = new LinkedHashMap<>();
            }

            String role =
                    text(request.get("role"));

            String id =
                    text(
                            request.get("studentId")
                    );

            String department =
                    text(
                            request.get("department")
                    );

            String name =
                    text(
                            request.get("name")
                    );

            Map<String, Object> response =
                    new LinkedHashMap<>();

            response.put(
                    "success",
                    true
            );

            response.put(
                    "engine",
                    "DIET Attendance AI"
            );

            response.put(
                    "name",
                    name
            );

            response.put(
                    "role",
                    role
            );

            response.put(
                    "department",
                    department
            );

            /*
             * STUDENT AI ANALYSIS
             */

            if ("STUDENT".equalsIgnoreCase(role)
                    && !id.isBlank()) {

                Map<String, Object> summary =
                        attendanceService
                                .getStudentSummary(id);

                double percentage =
                        number(
                                summary.get(
                                        "percentage"
                                )
                        );

                int total =
                        integer(
                                summary.get(
                                        "totalClasses"
                                )
                        );

                int present =
                        integer(
                                summary.get(
                                        "present"
                                )
                        );

                int absent =
                        integer(
                                summary.get(
                                        "absent"
                                )
                        );

                response.put(
                        "attendance",
                        percentage
                );

                response.put(
                        "summary",
                        studentSummary(
                                percentage,
                                total,
                                present,
                                absent
                        )
                );

                response.put(
                        "status",
                        percentage >= 75
                                ? "GOOD"
                                : "LOW"
                );

                response.put(
                        "findings",
                        studentFindings(
                                percentage,
                                total
                        )
                );

                response.put(
                        "recommendations",
                        studentRecommendations(
                                percentage
                        )
                );

                return ResponseEntity.ok(
                        response
                );
            }


            /*
             * HOD / FACULTY DEPARTMENT ANALYSIS
             */

            if (!department.isBlank()) {

                Map<String, Object> summary =
                        attendanceService
                                .getDepartmentSummary(
                                        department
                                );

                double percentage =
                        number(
                                summary.get(
                                        "percentage"
                                )
                        );

                int students =
                        integer(
                                summary.get(
                                        "students"
                                )
                        );

                int present =
                        integer(
                                summary.get(
                                        "present"
                                )
                        );

                int absent =
                        integer(
                                summary.get(
                                        "absent"
                                )
                        );

                response.put(
                        "attendance",
                        percentage
                );

                response.put(
                        "students",
                        students
                );

                response.put(
                        "present",
                        present
                );

                response.put(
                        "absent",
                        absent
                );

                response.put(
                        "summary",
                        departmentSummary(
                                department,
                                percentage,
                                students,
                                present,
                                absent
                        )
                );

                response.put(
                        "status",
                        percentage >= 75
                                ? "GOOD"
                                : "ATTENTION REQUIRED"
                );

                response.put(
                        "findings",
                        departmentFindings(
                                percentage,
                                students,
                                absent
                        )
                );

                response.put(
                        "recommendations",
                        departmentRecommendations(
                                percentage
                        )
                );

                return ResponseEntity.ok(
                        response
                );
            }


            /*
             * ADMIN / SYSTEM ANALYSIS
             */

            List<AttendanceService.AttendanceEntry>
                    all =
                    attendanceService
                            .getAllAttendance();

            int present = 0;
            int absent = 0;

            for (
                    AttendanceService.AttendanceEntry
                            entry : all) {

                if ("PRESENT".equalsIgnoreCase(
                        entry.status)) {

                    present++;

                } else if (
                        "ABSENT".equalsIgnoreCase(
                                entry.status)) {

                    absent++;
                }
            }

            int total =
                    present + absent;

            double percentage =
                    total == 0
                            ? 0
                            : present * 100.0 / total;

            response.put(
                    "attendance",
                    round(percentage)
            );

            response.put(
                    "totalRecords",
                    all.size()
            );

            response.put(
                    "present",
                    present
            );

            response.put(
                    "absent",
                    absent
            );

            response.put(
                    "summary",
                    systemSummary(
                            percentage,
                            total,
                            present,
                            absent
                    )
            );

            response.put(
                    "status",
                    percentage >= 75
                            ? "HEALTHY"
                            : "ATTENTION REQUIRED"
            );

            response.put(
                    "findings",
                    systemFindings(
                            percentage,
                            total
                    )
            );

            response.put(
                    "recommendations",
                    systemRecommendations(
                            percentage
                    )
            );

            return ResponseEntity.ok(
                    response
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
                                    "AI analysis failed: "
                                            +
                                            (
                                                e.getMessage()
                                                        == null
                                                        ? "Unknown error"
                                                        : e.getMessage()
                                            )
                            )
                    );
        }
    }


    private String text(Object value) {

        if (value == null) {
            return "";
        }

        return String.valueOf(value)
                .trim();
    }


    private int integer(Object value) {

        if (value instanceof Number number) {
            return number.intValue();
        }

        try {

            return Integer.parseInt(
                    String.valueOf(value)
            );

        } catch (Exception e) {

            return 0;
        }
    }


    private double number(Object value) {

        if (value instanceof Number number) {
            return number.doubleValue();
        }

        try {

            return Double.parseDouble(
                    String.valueOf(value)
            );

        } catch (Exception e) {

            return 0;
        }
    }


    private double round(double value) {

        return Math.round(
                value * 100.0
        ) / 100.0;
    }


    private String studentSummary(
            double percentage,
            int total,
            int present,
            int absent) {

        if (total == 0) {

            return
                    "No attendance records have been recorded yet.";
        }

        if (percentage >= 85) {

            return
                    "Your attendance is strong at "
                            + round(percentage)
                            + "%. Keep maintaining this level.";
        }

        if (percentage >= 75) {

            return
                    "Your attendance is currently "
                            + round(percentage)
                            + "%. You are above the 75% target, "
                            + "but regular attendance is recommended.";
        }

        return
                "Your attendance is "
                        + round(percentage)
                        + "%. This is below the 75% target. "
                        + "You should improve attendance.";
    }


    private List<String> studentFindings(
            double percentage,
            int total) {

        List<String> findings =
                new ArrayList<>();

        findings.add(
                "Attendance records analyzed: "
                        + total
        );

        if (percentage < 75) {

            findings.add(
                    "Attendance is below the 75% target."
            );

        } else {

            findings.add(
                    "Attendance is currently at or above the 75% target."
            );
        }

        if (percentage >= 85) {

            findings.add(
                    "Attendance level is strong."
            );

        } else if (percentage >= 75) {

            findings.add(
                    "There is limited room before attendance falls below target."
            );

        }

        return findings;
    }


    private List<String> studentRecommendations(
            double percentage) {

        List<String> recommendations =
                new ArrayList<>();

        if (percentage < 75) {

            recommendations.add(
                    "Attend upcoming classes consistently."
            );

            recommendations.add(
                    "Avoid unnecessary absences."
            );

            recommendations.add(
                    "Discuss attendance concerns with your faculty or HOD."
            );

        } else {

            recommendations.add(
                    "Continue attending classes regularly."
            );

            recommendations.add(
                    "Maintain attendance above 75%."
            );
        }

        return recommendations;
    }


    private String departmentSummary(
            String department,
            double percentage,
            int students,
            int present,
            int absent) {

        return
                department
                        + " currently has "
                        + students
                        + " registered students and an attendance rate of "
                        + round(percentage)
                        + "% across "
                        + (present + absent)
                        + " attendance records.";
    }


    private List<String> departmentFindings(
            double percentage,
            int students,
            int absent) {

        List<String> findings =
                new ArrayList<>();

        findings.add(
                "Department students: "
                        + students
        );

        findings.add(
                "Present records: "
                        + "reviewed"
        );

        findings.add(
                "Absent records: "
                        + absent
        );

        if (percentage < 75) {

            findings.add(
                    "Department attendance is below the 75% target."
            );

        } else {

            findings.add(
                    "Department attendance is at or above the 75% target."
            );
        }

        return findings;
    }


    private List<String> departmentRecommendations(
            double percentage) {

        List<String> recommendations =
                new ArrayList<>();

        if (percentage < 75) {

            recommendations.add(
                    "Identify students with repeated absences."
            );

            recommendations.add(
                    "Review attendance subject-wise."
            );

            recommendations.add(
                    "Follow up with low-attendance students."
            );

        } else {

            recommendations.add(
                    "Continue monitoring attendance."
            );

            recommendations.add(
                    "Review students approaching the 75% threshold."
            );
        }

        return recommendations;
    }


    private String systemSummary(
            double percentage,
            int total,
            int present,
            int absent) {

        if (total == 0) {

            return
                    "The attendance system has no saved attendance records yet.";
        }

        return
                "The DIET AI engine analyzed "
                        + total
                        + " attendance records. "
                        + "Overall recorded attendance is "
                        + round(percentage)
                        + "%.";
    }


    private List<String> systemFindings(
            double percentage,
            int total) {

        List<String> findings =
                new ArrayList<>();

        findings.add(
                "Total attendance records: "
                        + total
        );

        findings.add(
                "Overall attendance: "
                        + round(percentage)
                        + "%"
        );

        if (percentage < 75) {

            findings.add(
                    "Institute attendance requires attention."
            );

        } else {

            findings.add(
                    "Institute attendance is currently healthy."
            );
        }

        return findings;
    }


    private List<String> systemRecommendations(
            double percentage) {

        List<String> recommendations =
                new ArrayList<>();

        if (percentage < 75) {

            recommendations.add(
                    "Review departments with low attendance."
            );

            recommendations.add(
                    "Generate low-attendance student reports."
            );

            recommendations.add(
                    "Monitor attendance trends regularly."
            );

        } else {

            recommendations.add(
                    "Continue department-level monitoring."
            );

            recommendations.add(
                    "Identify students approaching the minimum attendance threshold."
            );
        }

        return recommendations;
    }
}