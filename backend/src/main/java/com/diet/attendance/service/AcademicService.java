package com.diet.attendance.service;

import com.diet.attendance.model.StudentRecord;
import com.diet.attendance.model.SubjectAttendance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AcademicService {

    private final List<StudentRecord> students =
            new ArrayList<>();

    public AcademicService() {

        students.add(
                new StudentRecord(
                        "268T1A1293",
                        "Arun Kumar",
                        "INFORMATION TECHNOLOGY",
                        "2nd Year",
                        "A",
                        "arun@diet.edu",
                        "9000000001",
                        82.5,
                        Arrays.asList(
                                "Java",
                                "Database Management Systems",
                                "Web Development",
                                "Mathematics",
                                "Computer Networks"
                        )
                )
        );

        students.add(
                new StudentRecord(
                        "268T1A1294",
                        "Priya Sharma",
                        "INFORMATION TECHNOLOGY",
                        "2nd Year",
                        "A",
                        "priya@diet.edu",
                        "9000000002",
                        74.0,
                        Arrays.asList(
                                "Java",
                                "Database Management Systems",
                                "Web Development",
                                "Mathematics",
                                "Computer Networks"
                        )
                )
        );

        students.add(
                new StudentRecord(
                        "268T1A1295",
                        "Rahul Kumar",
                        "INFORMATION TECHNOLOGY",
                        "2nd Year",
                        "A",
                        "rahul@diet.edu",
                        "9000000003",
                        61.5,
                        Arrays.asList(
                                "Java",
                                "Database Management Systems",
                                "Web Development",
                                "Mathematics",
                                "Computer Networks"
                        )
                )
        );

        students.add(
                new StudentRecord(
                        "268T2A1298",
                        "Kavin Raj",
                        "COMPUTER SCIENCE & ENGINEERING",
                        "2nd Year",
                        "A",
                        "kavin@diet.edu",
                        "9000000004",
                        88.0,
                        Arrays.asList(
                                "Java",
                                "Operating Systems",
                                "Database Management Systems",
                                "Computer Networks"
                        )
                )
        );

        students.add(
                new StudentRecord(
                        "268T2A1299",
                        "Nisha Devi",
                        "COMPUTER SCIENCE & ENGINEERING",
                        "2nd Year",
                        "A",
                        "nisha@diet.edu",
                        "9000000005",
                        69.5,
                        Arrays.asList(
                                "Java",
                                "Operating Systems",
                                "Database Management Systems",
                                "Computer Networks"
                        )
                )
        );

        students.add(
                new StudentRecord(
                        "268T3A1302",
                        "Vijay Kumar",
                        "ARTIFICIAL INTELLIGENCE & DATA SCIENCE",
                        "3rd Year",
                        "A",
                        "vijay@diet.edu",
                        "9000000006",
                        91.0,
                        Arrays.asList(
                                "Machine Learning",
                                "Python",
                                "Data Science",
                                "Artificial Intelligence"
                        )
                )
        );
    }

    public StudentRecord findStudent(
            String id) {

        for (StudentRecord student : students) {

            if (student.getId()
                    .equalsIgnoreCase(id)) {

                return student;
            }
        }

        return null;
    }

    public List<StudentRecord> getStudents() {

        return new ArrayList<>(students);
    }

    public List<StudentRecord> getStudentsByDepartment(
            String department) {

        List<StudentRecord> result =
                new ArrayList<>();

        for (StudentRecord student : students) {

            if (student.getDepartment()
                    .equalsIgnoreCase(department)) {

                result.add(student);
            }
        }

        return result;
    }

    public List<SubjectAttendance>
    getAttendance(String studentId) {

        StudentRecord student =
                findStudent(studentId);

        List<SubjectAttendance> result =
                new ArrayList<>();

        if (student == null) {
            return result;
        }

        switch (studentId.toUpperCase()) {

            case "268T1A1293":

                result.add(
                        new SubjectAttendance(
                                "Java",
                                40,
                                35
                        )
                );

                result.add(
                        new SubjectAttendance(
                                "Database Management Systems",
                                42,
                                34
                        )
                );

                result.add(
                        new SubjectAttendance(
                                "Web Development",
                                38,
                                32
                        )
                );

                result.add(
                        new SubjectAttendance(
                                "Mathematics",
                                40,
                                30
                        )
                );

                result.add(
                        new SubjectAttendance(
                                "Computer Networks",
                                36,
                                31
                        )
                );

                break;

            case "268T1A1294":

                result.add(
                        new SubjectAttendance(
                                "Java",
                                40,
                                31
                        )
                );

                result.add(
                        new SubjectAttendance(
                                "Database Management Systems",
                                42,
                                29
                        )
                );

                result.add(
                        new SubjectAttendance(
                                "Web Development",
                                38,
                                30
                        )
                );

                result.add(
                        new SubjectAttendance(
                                "Mathematics",
                                40,
                                28
                        )
                );

                result.add(
                        new SubjectAttendance(
                                "Computer Networks",
                                36,
                                29
                        )
                );

                break;

            case "268T1A1295":

                result.add(
                        new SubjectAttendance(
                                "Java",
                                40,
                                25
                        )
                );

                result.add(
                        new SubjectAttendance(
                                "Database Management Systems",
                                42,
                                24
                        )
                );

                result.add(
                        new SubjectAttendance(
                                "Web Development",
                                38,
                                26
                        )
                );

                result.add(
                        new SubjectAttendance(
                                "Mathematics",
                                40,
                                23
                        )
                );

                result.add(
                        new SubjectAttendance(
                                "Computer Networks",
                                36,
                                24
                        )
                );

                break;

            default:

                for (String subject :
                        student.getSubjects()) {

                    result.add(
                            new SubjectAttendance(
                                    subject,
                                    40,
                                    34
                            )
                    );
                }
        }

        return result;
    }
}