package com.example.workitapp;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class LabelFormatter extends ValueFormatter {
    ArrayList<String> names;

    public LabelFormatter(ArrayList<String> names) {
        this.names = names;
    }

    @Override
    public String getFormattedValue(float value) {
        return names.get((int) value);
    }
}
