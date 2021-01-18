package com.example.workitapp.Objects;

public class Division {
    private int id;
//    Manager divisionManager;
//    Worker[] workers;

    private String divisionManager;
    private String[] workers;

    public Division() {
    }

    public Division(int id, String divisionManager, String[] workers) {
        this.id = id;
        this.divisionManager = divisionManager;
        this.workers = workers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDivisionManager() {
        return divisionManager;
    }

    public void setDivisionManager(String divisionManager) {
        this.divisionManager = divisionManager;
    }

    public String[] getWorkers() {
        return workers;
    }

    public void setWorkers(String[] workers) {
        this.workers = workers;
    }
}
