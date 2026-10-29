package com.diet.attendance.model;

import java.util.List;

public class StudentRecord {

    private String id;
    private String name;
    private String department;
    private String year;
    private String section;
    private String email;
    private String phone;
    private double overallAttendance;
    private List<String> subjects;

    public StudentRecord() {
    }

    public StudentRecord(
            String id,
            String name,
            String department,
            String year,
            String section,
            String email,
            String phone,
            double overallAttendance,
            List<String> subjects) {

        this.id = id;
        this.name = name;
        this.department = department;
        this.year = year;
        this.section = section;
        this.email = email;
        this.phone = phone;
        this.overallAttendance = overallAttendance;
        this.subjects = subjects;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getYear() {
        return year;
    }

    public String getSection() {
        return section;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public double getOverallAttendance() {
        return overallAttendance;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOverallAttendance(double overallAttendance) {
        this.overallAttendance = overallAttendance;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }
}