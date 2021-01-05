package com.example.workitapp;

public class Worker {
    private String name;
    private String email;
    private String password;
    private int divisionID;
    private String imgUrl;
    private boolean isAccepted;


    public Worker() {
    }

    public Worker(String name, String email, String password, int divisionID) {
        this.name = name;
        this.email = email;
        this.divisionID = divisionID;
        this.imgUrl = imgUrl;
        this.isAccepted = false;
        this.password = password;
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
}
