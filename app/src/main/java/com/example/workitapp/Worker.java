package com.example.workitapp;

public class Worker {
    private String workerID;
    private String name;
    private String email;
    private String password;
    private String divisionID;
    private String imgUrl;
    private boolean isAccepted;

    public Worker() {
    }

    public Worker(String name, String email, String password, String divisionID) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.divisionID = divisionID;
        this.imgUrl = "default";
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(String divisionID) {
        this.divisionID = divisionID;
    }

    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkerID() {
        return workerID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
