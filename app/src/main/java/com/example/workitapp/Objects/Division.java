package com.example.workitapp.Objects;

public class Division {
    private int id;
    Manager divisionManager;
    Worker[] workers;

    public Division() {
    }

    public Division(int id, Manager divisionManager, Worker[] workers) {
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

    public Manager getDivisionManager() {
        return divisionManager;
    }

    public void setDivisionManager(Manager divisionManager) {
        this.divisionManager = divisionManager;
    }

    public Worker[] getWorkers() {
        return workers;
    }

    public void setWorkers(Worker[] workers) {
        this.workers = workers;
    }
}
