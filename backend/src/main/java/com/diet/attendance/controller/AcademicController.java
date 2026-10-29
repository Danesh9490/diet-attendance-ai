package com.diet.attendance.controller;

import com.diet.attendance.model.StudentRecord;
import com.diet.attendance.model.SubjectAttendance;
import com.diet.attendance.service.AcademicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic")
@CrossOrigin
public class AcademicController {

    private final AcademicService academicService;

    public AcademicController(
            AcademicService academicService) {

        this.academicService =
                academicService;
    }

    @GetMapping("/students")
    public List<StudentRecord> students() {

        return academicService.getStudents();
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<StudentRecord>
    student(@PathVariable String id) {

        StudentRecord student =
                academicService.findStudent(id);

        if (student == null) {

            return ResponseEntity.notFound()
                    .build();
        }

        return ResponseEntity.ok(student);
    }

    @GetMapping(
            "/department/{department}/students")
    public List<StudentRecord>
    departmentStudents(
            @PathVariable String department) {

        return academicService
                .getStudentsByDepartment(
                        department
                );
    }

    @GetMapping("/student/{id}/attendance")
    public ResponseEntity<
            List<SubjectAttendance>>
    attendance(
            @PathVariable String id) {

        StudentRecord student =
                academicService.findStudent(id);

        if (student == null) {

            return ResponseEntity.notFound()
                    .build();
        }

        return ResponseEntity.ok(
                academicService.getAttendance(id)
        );
    }
}