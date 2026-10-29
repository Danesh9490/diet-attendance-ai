package com.diet.attendance.model;

public class SubjectAttendance {

    private String subject;
    private int conducted;
    private int attended;

    public SubjectAttendance() {
    }

    public SubjectAttendance(
            String subject,
            int conducted,
            int attended) {

        this.subject = subject;
        this.conducted = conducted;
        this.attended = attended;
    }

    public String getSubject() {
        return subject;
    }

    public int getConducted() {
        return conducted;
    }

    public int getAttended() {
        return attended;
    }

    public double getPercentage() {

        if (conducted == 0) {
            return 0;
        }

        return
                ((double) attended / conducted)
                * 100.0;
    }
}