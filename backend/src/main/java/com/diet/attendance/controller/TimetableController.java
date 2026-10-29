package com.diet.attendance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/timetable")
@CrossOrigin
public class TimetableController {

    /*
     * DIET COLLEGE TIMINGS
     *
     * College:
     * 09:00 AM - 04:30 PM
     *
     * Break:
     * 10:40 AM - 10:50 AM
     *
     * Lunch:
     * 12:20 PM - 01:10 PM
     */

    private static final List<Period> PERIODS =
            List.of(

                    new Period(
                            1,
                            "09:00",
                            "09:50"
                    ),

                    new Period(
                            2,
                            "09:50",
                            "10:40"
                    ),

                    new Period(
                            3,
                            "10:50",
                            "11:40"
                    ),

                    new Period(
                            4,
                            "11:40",
                            "12:20"
                    ),

                    new Period(
                            5,
                            "13:10",
                            "14:00"
                    ),

                    new Period(
                            6,
                            "14:00",
                            "14:50"
                    ),

                    new Period(
                            7,
                            "14:50",
                            "15:40"
                    ),

                    new Period(
                            8,
                            "15:40",
                            "16:30"
                    )
            );


    private static final List<BreakTime> BREAKS =
            List.of(

                    new BreakTime(
                            "SHORT BREAK",
                            "10:40",
                            "10:50"
                    ),

                    new BreakTime(
                            "LUNCH",
                            "12:20",
                            "13:10"
                    )
            );


    /*
     * GET ALL TIMETABLE INFORMATION
     */

    @GetMapping
    public ResponseEntity<?> getTimetable() {

        Map<String, Object> result =
                new LinkedHashMap<>();

        result.put(
                "collegeStart",
                "09:00"
        );

        result.put(
                "collegeEnd",
                "16:30"
        );

        result.put(
                "periods",
                PERIODS
        );

        result.put(
                "breaks",
                BREAKS
        );

        result.put(
                "departments",
                buildDepartments()
        );

        return ResponseEntity.ok(result);
    }


    /*
     * GET DEPARTMENT TIMETABLE
     *
     * Example:
     *
     * /api/timetable/IT
     */

    @GetMapping("/{department}")
    public ResponseEntity<?> getDepartmentTimetable(
            @PathVariable String department) {

        String code =
                department
                        .trim()
                        .toUpperCase();

        Map<String, Object> result =
                buildDepartment(
                        code
                );

        return ResponseEntity.ok(result);
    }


    /*
     * GET AVAILABLE DEPARTMENTS
     */

    @GetMapping("/departments/list")
    public ResponseEntity<?> getDepartments() {

        return ResponseEntity.ok(
                buildDepartments()
        );
    }


    /*
     * DEPARTMENT LIST
     */

    private List<Map<String, Object>>
    buildDepartments() {

        List<Map<String, Object>>
                departments =
                new ArrayList<>();

        departments.add(
                department(
                        "IT",
                        "Information Technology"
                )
        );

        departments.add(
                department(
                        "CSE",
                        "Computer Science and Engineering"
                )
        );

        departments.add(
                department(
                        "ECE",
                        "Electronics and Communication Engineering"
                )
        );

        departments.add(
                department(
                        "EEE",
                        "Electrical and Electronics Engineering"
                )
        );

        departments.add(
                department(
                        "MECH",
                        "Mechanical Engineering"
                )
        );

        departments.add(
                department(
                        "CIVIL",
                        "Civil Engineering"
                )
        );

        return departments;
    }


    private Map<String, Object>
    department(
            String code,
            String name) {

        Map<String, Object> item =
                new LinkedHashMap<>();

        item.put(
                "code",
                code
        );

        item.put(
                "name",
                name
        );

        return item;
    }


    /*
     * BUILD DEPARTMENT TIMETABLE
     */

    private Map<String, Object>
    buildDepartment(
            String department) {

        Map<String, Object> result =
                new LinkedHashMap<>();

        result.put(
                "department",
                department
        );

        result.put(
                "collegeHours",
                "09:00 AM - 04:30 PM"
        );

        result.put(
                "break1",
                "10:40 AM - 10:50 AM"
        );

        result.put(
                "lunch",
                "12:20 PM - 01:10 PM"
        );

        result.put(
                "periods",
                PERIODS
        );

        Map<String, List<TimetableEntry>>
                weekly =
                new LinkedHashMap<>();

        weekly.put(
                "MONDAY",
                monday(department)
        );

        weekly.put(
                "TUESDAY",
                tuesday(department)
        );

        weekly.put(
                "WEDNESDAY",
                wednesday(department)
        );

        weekly.put(
                "THURSDAY",
                thursday(department)
        );

        weekly.put(
                "FRIDAY",
                friday(department)
        );

        weekly.put(
                "SATURDAY",
                saturday(department)
        );

        result.put(
                "weekly",
                weekly
        );

        return result;
    }


    private List<TimetableEntry>
    monday(String department) {

        return day(
                department,
                "Mathematics",
                "Programming",
                "Database Systems",
                "Communication Skills",
                "Operating Systems",
                "Programming Lab",
                "Programming Lab",
                "Library"
        );
    }


    private List<TimetableEntry>
    tuesday(String department) {

        return day(
                department,
                "Data Structures",
                "Digital Systems",
                "English",
                "Mathematics",
                "Web Technologies",
                "Web Technology Lab",
                "Web Technology Lab",
                "Sports"
        );
    }


    private List<TimetableEntry>
    wednesday(String department) {

        return day(
                department,
                "Operating Systems",
                "Database Systems",
                "Data Structures",
                "Environmental Science",
                "Artificial Intelligence",
                "AI Lab",
                "AI Lab",
                "Library"
        );
    }


    private List<TimetableEntry>
    thursday(String department) {

        return day(
                department,
                "Mathematics",
                "Computer Networks",
                "Artificial Intelligence",
                "Programming",
                "Database Systems",
                "Database Lab",
                "Database Lab",
                "Sports"
        );
    }


    private List<TimetableEntry>
    friday(String department) {

        return day(
                department,
                "Computer Networks",
                "Web Technologies",
                "Operating Systems",
                "Mathematics",
                "Professional Skills",
                "Project Work",
                "Project Work",
                "Seminar"
        );
    }


    private List<TimetableEntry>
    saturday(String department) {

        return day(
                department,
                "Project Work",
                "Project Work",
                "Aptitude",
                "Mentoring",
                "Department Activity",
                "Laboratory",
                "Laboratory",
                "Club Activity"
        );
    }


    private List<TimetableEntry>
    day(
            String department,
            String... subjects) {

        List<TimetableEntry> entries =
                new ArrayList<>();

        for (
                int i = 0;
                i < subjects.length &&
                i < PERIODS.size();
                i++
        ) {

            Period period =
                    PERIODS.get(i);

            entries.add(
                    new TimetableEntry(
                            period.number(),
                            period.start(),
                            period.end(),
                            subjects[i],
                            department
                    )
            );
        }

        return entries;
    }


    /*
     * RECORDS
     */

    public record Period(
            int number,
            String start,
            String end
    ) {}


    public record BreakTime(
            String name,
            String start,
            String end
    ) {}


    public record TimetableEntry(
            int period,
            String start,
            String end,
            String subject,
            String department
    ) {}
}