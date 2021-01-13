package com.example.workitapp;

import android.os.Build;

import com.example.workitapp.Objects.Assignment;
import com.example.workitapp.Objects.Request;
import com.example.workitapp.Objects.Worker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AssignmentMockDB {
    public static ArrayList<Assignment> generateMovies() {
        ArrayList<Assignment> assignments = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assignments.add(new Assignment("Make coffee", LocalDate.of(1985, 1, 1), "2 sugar."));

            assignments.add(new Assignment("Make milk", LocalDate.of(1985, 1, 1), "2%."));
            assignments.add(new Assignment("buy coffee", LocalDate.of(1985, 1, 1), "black."));
            assignments.add(new Assignment("buy milk", LocalDate.of(1985, 1, 1), "1%."));
            assignments.add(new Assignment("drink water", LocalDate.of(1985, 1, 1), "2 cups."));
            assignments.add(new Assignment("feed the dog", LocalDate.of(1985, 1, 1), "one time at the morning."));
            assignments.add(new Assignment("wash your hands", LocalDate.of(1999, 10, 28), "twice a day."));
            assignments.add(new Assignment("Make coffee again", LocalDate.now(), "1.5 sugar, im on a diet."));
            assignments.add(new Assignment("Not that coffee again", LocalDate.of(1985, 1, 1), "Enough..."));
            assignments.add(new Assignment("Stop with the coffee", LocalDate.now(), "Enough is enough !"));
            assignments.add(new Assignment("This is a very long title maaan", LocalDate.now(), "And this is a very very very very loooooooooong description for the mission you should do for me bro"));
        }
            return assignments;
    }

    public static ArrayList<Request> generateReqs() {
        ArrayList<Request> requests = new ArrayList<>();
        requests.add(new Request(new Worker("Arad", "aea","aaa",10)));
        requests.add(new Request(new Worker("Avi", "aea","aaa",10)));
        requests.add(new Request(new Worker("Moshe", "moshelevi123@gmail.com","aaa",10)));
        requests.add(new Request(new Worker("Nemo", "aea","aaa",10)));
        requests.add(new Request(new Worker("Levi", "aea","aaa",10)));
        requests.add(new Request(new Worker("Omri", "aea","aaa",10)));
        requests.add(new Request(new Worker("Roni", "aea","aaa",10)));
        requests.add(new Request(new Worker("Menashe", "aea","aaa",10)));
        return requests;
    }
}
