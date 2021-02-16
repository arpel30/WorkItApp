package com.example.workitapp.Objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Assignment {
    String title;
//    SimpleDateFormat format;
    String dueTo;
    String description;

    public Assignment() {
    }

    public Assignment(String title, String  dueTo, String description) {
        this.title = title;
//        this.format = new SimpleDateFormat("dd/MM/yy");;
        this.dueTo = dueTo;
        this.description = description;
    }


    public Assignment(String title, LocalDate  dueTo, String description) {
        this.title = title;
//        this.format = new SimpleDateFormat("dd/MM/yy");;
        this.dueTo = dueTo.toString();
        this.description = description;
    }

//    public SimpleDateFormat getFormat() {
//        return format;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueTo() {
        return dueTo;
    }

    public void setDueToLocalDate(LocalDate dueTo) {
        this.dueTo = dueTo.toString();
    }

    public void setDueTo(String dueTo) {
        this.dueTo = dueTo.toString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


