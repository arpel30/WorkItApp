package com.example.workitapp.Objects;

import android.os.Build;

import java.time.LocalDate;
import java.util.ArrayList;

public class Worker {
    private String name;
    private String email;
    private String password;
    private int divisionID;
    private String imgUrl;
    private boolean isAccepted;
    ArrayList<Assignment> assignments = new ArrayList<>();
    private int assignmentsDoneAll;
    private int assignmentsDoneWeek;
    private LocalDate startDate;


    public Worker() {
        this.isAccepted = false;
        this.assignmentsDoneAll=0;
        this.assignmentsDoneWeek=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startDate = LocalDate.now();
        }
    }

    public Worker(String name, String email, String password, int divisionID) {
        this.name = name;
        this.email = email;
        this.divisionID = divisionID;
        this.imgUrl = "default";
        this.isAccepted = false;
        this.password = password;
        this.assignmentsDoneAll=0;
        this.assignmentsDoneWeek=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startDate = LocalDate.now();
        }
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getAssignmentsDoneAll() {
        return assignmentsDoneAll;
    }

    public void setAssignmentsDoneAll(int assignmentsDoneAll) {
        this.assignmentsDoneAll = assignmentsDoneAll;
    }

    public int getAssignmentsDoneWeek() {
        return assignmentsDoneWeek;
    }

    public void setAssignmentsDoneWeek(int assignmentsDoneWeek) {
        this.assignmentsDoneWeek = assignmentsDoneWeek;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
    }
}
