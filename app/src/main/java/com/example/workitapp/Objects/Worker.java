package com.example.workitapp.Objects;

import android.os.Build;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class Worker {
    private String name;
    private String email;
    private String password;
    private int divisionID;
    private String imgUrl;
    private boolean isAccepted;
    ArrayList<Assignment> assignments;
    private int assignmentsDoneAll;
    private int assignmentsDoneWeek;
    private String startDate;


    public Worker() {
//        assignments  = new ArrayList<>();
//        this.isAccepted = false;
//        this.assignmentsDoneAll=0;
////        this.assignmentsDoneWeek=0;
//        this.assignmentsDoneWeek=new Random().nextInt(10);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startDate = LocalDate.now();
//        }
    }

    public Worker(String name, String email, String password, int divisionID) {
        assignments  = new ArrayList<>();
        this.name = name;
        this.email = email;
        this.divisionID = divisionID;
        this.imgUrl = "default";
        this.isAccepted = false;
        this.password = password;
        this.assignmentsDoneAll=0;
        this.assignmentsDoneWeek=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startDate = LocalDate.now().toString();
        }
    }

    @Override
    public String toString() {
        return "Worker{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", divisionID=" + divisionID +
                ", imgUrl='" + imgUrl + '\'' +
                ", isAccepted=" + isAccepted +
                ", assignments=" + assignments +
                ", assignmentsDoneAll=" + assignmentsDoneAll +
                ", assignmentsDoneWeek=" + assignmentsDoneWeek +
                ", startDate='" + startDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj != null && obj instanceof Worker) {
            return this.email == ((Worker) obj).getEmail();
        }
        return false;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
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

    public boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
//        return null;
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
    }
}
