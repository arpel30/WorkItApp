package com.example.workitapp;

import android.os.Build;

import com.example.workitapp.Objects.Assignment;

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
        }
            return assignments;
    }
}
