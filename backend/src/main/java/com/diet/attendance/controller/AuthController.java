package com.diet.attendance.controller;

import com.diet.attendance.model.Person;
import com.diet.attendance.service.DirectoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final DirectoryService directoryService;


    public AuthController(
            DirectoryService directoryService) {

        this.directoryService =
                directoryService;
    }


    /*
     * =====================================================
     * LOGIN
     * =====================================================
     *
     * URL:
     *
     * POST /api/auth/login
     *
     * Request:
     *
     * {
     *     "id": "268T1A1293",
     *     "password": "student123"
     * }
     *
     * The role comes from the actual account.
     * The frontend does not decide the user's role.
     */

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        try {

            if (request == null) {

                return error(
                        HttpStatus.BAD_REQUEST,
                        "Login request is required."
                );
            }


            String id =
                    request.id == null
                            ? ""
                            : request.id.trim();


            String password =
                    request.password == null
                            ? ""
                            : request.password;


            if (id.isBlank()) {

                return error(
                        HttpStatus.BAD_REQUEST,
                        "College ID / Faculty ID is required."
                );
            }


            if (password.isBlank()) {

                return error(
                        HttpStatus.BAD_REQUEST,
                        "Password is required."
                );
            }


            /*
             * Authenticate against the existing
             * no-database DirectoryService.
             */

            Person person =
                    directoryService.authenticate(
                            id,
                            password
                    );


            if (person == null) {

                return error(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid ID or password."
                );
            }


            /*
             * Create a clean response.
             *
             * Do not return unnecessary internal
             * information.
             */

            Map<String, Object> response =
                    new HashMap<>();


            response.put(
                    "success",
                    true
            );


            response.put(
                    "id",
                    safe(
                            person.getId()
                    )
            );


            response.put(
                    "name",
                    safe(
                            person.getName()
                    )
            );


            response.put(
                    "role",
                    normalizeRole(
                            person.getRole()
                    )
            );


            response.put(
                    "department",
                    safe(
                            person.getDepartment()
                    )
            );


            response.put(
                    "designation",
                    safe(
                            person.getDesignation()
                    )
            );


            response.put(
                    "email",
                    safe(
                            person.getEmail()
                    )
            );


            response.put(
                    "profilePhoto",
                    safe(
                            person.getProfilePhoto()
                    )
            );


            /*
             * Also return the login ID so the
             * frontend can display it.
             */

            response.put(
                    "registrationNumber",
                    id
            );


            return ResponseEntity.ok(
                    response
            );


        } catch (Exception exception) {

            exception.printStackTrace();


            return error(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Login server error: "
                            +
                            (
                                exception.getMessage()
                                == null
                                    ? "Unknown error"
                                    : exception.getMessage()
                            )
            );
        }
    }


    /*
     * =====================================================
     * ROLE NORMALIZATION
     * =====================================================
     *
     * Existing backend may use:
     *
     * TEACHER
     *
     * while the website uses:
     *
     * FACULTY
     */

    private String normalizeRole(
            String role) {

        if (role == null ||
            role.isBlank()) {

            return "";
        }


        String value =
                role.trim()
                    .toUpperCase();


        if (
                value.equals("TEACHER") ||
                value.equals("FACULTY")
        ) {

            return "FACULTY";
        }


        if (
                value.equals("STUDENT")
        ) {

            return "STUDENT";
        }


        if (
                value.equals("HOD")
        ) {

            return "HOD";
        }


        if (
                value.equals("ADMIN") ||
                value.equals("ADMINISTRATOR")
        ) {

            return "ADMIN";
        }


        return value;
    }


    /*
     * =====================================================
     * SAFE STRING
     * =====================================================
     */

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }


    /*
     * =====================================================
     * ERROR RESPONSE
     * =====================================================
     */

    private ResponseEntity<?> error(
            HttpStatus status,
            String message) {

        Map<String, Object> response =
                new HashMap<>();


        response.put(
                "success",
                false
        );


        response.put(
                "message",
                message
        );


        return ResponseEntity
                .status(status)
                .body(response);
    }


    /*
     * =====================================================
     * LOGIN REQUEST
     * =====================================================
     */

    public static class LoginRequest {

        public String id;

        public String password;
    }
}