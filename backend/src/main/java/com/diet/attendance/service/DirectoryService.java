package com.diet.attendance.service;

import com.diet.attendance.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DirectoryService {

    private final List<Person> people = new ArrayList<>();

    public DirectoryService() {

        // =========================
        // ADMIN
        // =========================

        people.add(new Person(
                "ADMIN001",
                "Institute Administrator",
                "admin123",
                "ADMIN",
                "ALL",
                "System Administrator",
                "admin@diet.edu",
                null
        ));

        // =========================
        // INFORMATION TECHNOLOGY
        // A1 = IT
        // =========================

        people.add(new Person(
                "268T1A1293",
                "Arun Kumar",
                "student123",
                "STUDENT",
                "INFORMATION TECHNOLOGY",
                "Student",
                "arun@diet.edu",
                null
        ));

        people.add(new Person(
                "268T1A1294",
                "Priya Sharma",
                "student123",
                "STUDENT",
                "INFORMATION TECHNOLOGY",
                "Student",
                "priya@diet.edu",
                null
        ));

        people.add(new Person(
                "268T1A1295",
                "Rahul Kumar",
                "student123",
                "STUDENT",
                "INFORMATION TECHNOLOGY",
                "Student",
                "rahul@diet.edu",
                null
        ));

        people.add(new Person(
                "268T1A1296",
                "Dr. Meena Rao",
                "faculty123",
                "FACULTY",
                "INFORMATION TECHNOLOGY",
                "Assistant Professor",
                "meena@diet.edu",
                null
        ));

        people.add(new Person(
                "268T1A1297",
                "Dr. Suresh Kumar",
                "hod123",
                "HOD",
                "INFORMATION TECHNOLOGY",
                "Head of Department",
                "suresh@diet.edu",
                null
        ));

        // =========================
        // COMPUTER SCIENCE
        // A2 = CSE
        // =========================

        people.add(new Person(
                "268T2A1298",
                "Kavin Raj",
                "student123",
                "STUDENT",
                "COMPUTER SCIENCE & ENGINEERING",
                "Student",
                "kavin@diet.edu",
                null
        ));

        people.add(new Person(
                "268T2A1299",
                "Nisha Devi",
                "student123",
                "STUDENT",
                "COMPUTER SCIENCE & ENGINEERING",
                "Student",
                "nisha@diet.edu",
                null
        ));

        people.add(new Person(
                "268T2A1300",
                "Dr. Anitha Kumar",
                "faculty123",
                "FACULTY",
                "COMPUTER SCIENCE & ENGINEERING",
                "Assistant Professor",
                "anitha@diet.edu",
                null
        ));

        people.add(new Person(
                "268T2A1301",
                "Dr. Prakash",
                "hod123",
                "HOD",
                "COMPUTER SCIENCE & ENGINEERING",
                "Head of Department",
                "prakash@diet.edu",
                null
        ));

        // =========================
        // AI & DATA SCIENCE
        // A3 = AIDS
        // =========================

        people.add(new Person(
                "268T3A1302",
                "Vijay Kumar",
                "student123",
                "STUDENT",
                "ARTIFICIAL INTELLIGENCE & DATA SCIENCE",
                "Student",
                "vijay@diet.edu",
                null
        ));

        people.add(new Person(
                "268T3A1303",
                "Harini S",
                "student123",
                "STUDENT",
                "ARTIFICIAL INTELLIGENCE & DATA SCIENCE",
                "Student",
                "harini@diet.edu",
                null
        ));

        people.add(new Person(
                "268T3A1304",
                "Dr. Kavitha",
                "faculty123",
                "FACULTY",
                "ARTIFICIAL INTELLIGENCE & DATA SCIENCE",
                "Assistant Professor",
                "kavitha@diet.edu",
                null
        ));

        people.add(new Person(
                "268T3A1305",
                "Dr. Mohan",
                "hod123",
                "HOD",
                "ARTIFICIAL INTELLIGENCE & DATA SCIENCE",
                "Head of Department",
                "mohan@diet.edu",
                null
        ));

        // =========================
        // ELECTRONICS
        // A4 = ECE
        // =========================

        people.add(new Person(
                "268T4A1306",
                "Ajay Kumar",
                "student123",
                "STUDENT",
                "ELECTRONICS & COMMUNICATION ENGINEERING",
                "Student",
                "ajay@diet.edu",
                null
        ));

        people.add(new Person(
                "268T4A1307",
                "Divya R",
                "student123",
                "STUDENT",
                "ELECTRONICS & COMMUNICATION ENGINEERING",
                "Student",
                "divya@diet.edu",
                null
        ));

        people.add(new Person(
                "268T4A1308",
                "Dr. Rekha",
                "faculty123",
                "FACULTY",
                "ELECTRONICS & COMMUNICATION ENGINEERING",
                "Assistant Professor",
                "rekha@diet.edu",
                null
        ));

        people.add(new Person(
                "268T4A1309",
                "Dr. Rajesh",
                "hod123",
                "HOD",
                "ELECTRONICS & COMMUNICATION ENGINEERING",
                "Head of Department",
                "rajesh@diet.edu",
                null
        ));

        // =========================
        // MECHANICAL
        // A5 = MECH
        // =========================

        people.add(new Person(
                "268T5A1310",
                "Manoj Kumar",
                "student123",
                "STUDENT",
                "MECHANICAL ENGINEERING",
                "Student",
                "manoj@diet.edu",
                null
        ));

        people.add(new Person(
                "268T5A1311",
                "Dr. Senthil",
                "faculty123",
                "FACULTY",
                "MECHANICAL ENGINEERING",
                "Assistant Professor",
                "senthil@diet.edu",
                null
        ));

        people.add(new Person(
                "268T5A1312",
                "Dr. Kumar",
                "hod123",
                "HOD",
                "MECHANICAL ENGINEERING",
                "Head of Department",
                "kumar@diet.edu",
                null
        ));
    }

    public Person authenticate(String id, String password) {

        if (id == null || password == null) {
            return null;
        }

        for (Person person : people) {

            if (person.getId().equalsIgnoreCase(id.trim())
                    && person.getPassword().equals(password)) {

                return person;
            }
        }

        return null;
    }

    public Person findById(String id) {

        if (id == null) {
            return null;
        }

        for (Person person : people) {

            if (person.getId().equalsIgnoreCase(id.trim())) {
                return person;
            }
        }

        return null;
    }

    public List<Person> getAll() {
        return new ArrayList<>(people);
    }

    public List<Person> getByDepartment(String department) {

        List<Person> result = new ArrayList<>();

        if (department == null) {
            return result;
        }

        for (Person person : people) {

            if (person.getDepartment()
                    .equalsIgnoreCase(department)) {

                result.add(person);
            }
        }

        return result;
    }

    public List<Person> getByRole(String role) {

        List<Person> result = new ArrayList<>();

        if (role == null) {
            return result;
        }

        for (Person person : people) {

            if (person.getRole()
                    .equalsIgnoreCase(role)) {

                result.add(person);
            }
        }

        return result;
    }
}