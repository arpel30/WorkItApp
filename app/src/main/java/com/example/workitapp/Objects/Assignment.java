package com.example.workitapp.Objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Assignment {
    String title;
    SimpleDateFormat format;
    LocalDate dueTo;
    String description;

    public Assignment(String title, LocalDate  dueTo, String description) {
        this.title = title;
        this.format = new SimpleDateFormat("dd/MM/yy");;
        this.dueTo = dueTo;
        this.description = description;
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate  getDueTo() {
        return dueTo;
    }

    public void setDueTo(LocalDate dueTo) {
        this.dueTo = dueTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


