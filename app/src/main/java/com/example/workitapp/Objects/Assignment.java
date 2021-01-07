package com.example.workitapp.Objects;

import java.util.Date;

public class Assignment {
    String title;
    Date dueTo;
    String description;

    public Assignment() {
    }

    public Assignment(String title, Date dueTo, String description) {
        this.title = title;
        this.dueTo = dueTo;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDueTo() {
        return dueTo;
    }

    public void setDueTo(Date dueTo) {
        this.dueTo = dueTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


