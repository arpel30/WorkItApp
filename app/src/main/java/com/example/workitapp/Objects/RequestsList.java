package com.example.workitapp.Objects;

import java.util.ArrayList;

public class RequestsList {
    private ArrayList<Request> requests;

    public RequestsList(ArrayList<Request> requests) {
        this.requests = requests;
    }

    public RequestsList() {
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }
}
