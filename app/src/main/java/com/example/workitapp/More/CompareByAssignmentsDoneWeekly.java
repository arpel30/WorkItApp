package com.example.workitapp.More;

import com.example.workitapp.Objects.Worker;

import java.util.Comparator;

public class CompareByAssignmentsDoneWeekly implements Comparator<Worker> {

    @Override
    public int compare(Worker o1, Worker o2) {
        return o2.getAssignmentsDoneWeek() - o1.getAssignmentsDoneWeek();
    }
}
