package com.diet.attendance.model;

public class Person {

    private String id;
    private String name;
    private String password;
    private String role;
    private String department;
    private String designation;
    private String email;
    private String profilePhoto;

    public Person() {
    }

    public Person(
            String id,
            String name,
            String password,
            String role,
            String department,
            String designation,
            String email,
            String profilePhoto) {

        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.department = department;
        this.designation = designation;
        this.email = email;
        this.profilePhoto = profilePhoto;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}