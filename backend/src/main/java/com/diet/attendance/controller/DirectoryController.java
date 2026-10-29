package com.diet.attendance.controller;

import com.diet.attendance.model.Person;
import com.diet.attendance.service.DirectoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directory")
@CrossOrigin
public class DirectoryController {

    private final DirectoryService directoryService;

    public DirectoryController(
            DirectoryService directoryService) {

        this.directoryService =
                directoryService;
    }

    @GetMapping
    public List<Person> allPeople() {
        return directoryService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> byId(
            @PathVariable String id) {

        Person person =
                directoryService.findById(id);

        if (person == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(person);
    }

    @GetMapping("/department/{department}")
    public List<Person> byDepartment(
            @PathVariable String department) {

        return directoryService
                .getByDepartment(department);
    }

    @GetMapping("/role/{role}")
    public List<Person> byRole(
            @PathVariable String role) {

        return directoryService
                .getByRole(role);
    }
}