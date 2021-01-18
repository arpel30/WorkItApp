package com.example.workitapp.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workitapp.Adapter_Requests;
import com.example.workitapp.Adapter_Statistics;
import com.example.workitapp.AssignmentMockDB;
import com.example.workitapp.LabelFormatter;
import com.example.workitapp.More.Constants;
import com.example.workitapp.More.MyCallBack;
import com.example.workitapp.Objects.Assignment;
import com.example.workitapp.Objects.MySPV;
import com.example.workitapp.Objects.Request;
import com.example.workitapp.R;
import com.example.workitapp.Objects.Worker;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;

public class Fragment_Statistics extends MyFragment {

    private BarChart stats_BCH_chart;
    private RecyclerView stats_LST_workers;
    private TextView stats_LBL_title;
    private View view;
    private Context context;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Workers");
    private Worker w;

    private MyCallBack callBack;

    private Adapter_Statistics adapter_statistics;
    private ArrayList<Worker> workers = new ArrayList<>();

    public void setCallBack(MyCallBack _callBack) {
        this.callBack = _callBack;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        context = view.getContext();
        findViews();
        getWorkers();
        initViews();
        return view;
    }

    private void getWorkers() {
        Worker tmpW = new Worker();
        tmpW.setName("Shlomo");
        workers.add(tmpW);

        tmpW = new Worker();
        tmpW.setName("Tal");
        workers.add(tmpW);

        tmpW = new Worker();
        tmpW.setName("Booki");
        workers.add(tmpW);

        tmpW = new Worker();
        tmpW.setName("Neria");
        workers.add(tmpW);

        tmpW = new Worker();
        tmpW.setName("Arad");
        workers.add(tmpW);

        tmpW = new Worker();
        tmpW.setName("levi");
        workers.add(tmpW);

        tmpW = new Worker();
        tmpW.setName("avi");
        workers.add(tmpW);

        tmpW = new Worker();
        tmpW.setName("adam");
        workers.add(tmpW);
    }

    private void initViews() {
//        workers = AssignmentMockDB.generateReqs();
        adapter_statistics = new Adapter_Statistics(context, workers);

        adapter_statistics.setClickListener(new Adapter_Statistics.MyItemClickListener() {

        });

        stats_LST_workers.setLayoutManager(new LinearLayoutManager(context));
        stats_LST_workers.setAdapter(adapter_statistics);

        int[] colorArr = new int[]{Color.CYAN, Color.RED};

        BarDataSet barDataSet = new BarDataSet(dataValues(), "");
        barDataSet.setColors(colorArr);
        barDataSet.setStackLabels(new String[]{"Done", "Not Done"});

        BarData barData = new BarData(barDataSet);
        stats_BCH_chart.setData(barData);
        stats_BCH_chart.setScaleEnabled(false);
        stats_BCH_chart.setDescription(null);
//        stats_BCH_chart.getXAxis().setValueFormatter(new LabelFormatter((ArrayList<String>) getNames(workers)));
//        stats_BCH_chart.getXAxis().setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return workers.get((int) value).getName();
//
//            }
//        });

//        String[] xAxisLables = new String[]{"d1d","s2", "a3", "hi", "a3", "hi", "hi"};
//        stats_BCH_chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLables));


        ArrayList<String> xAxisLables = new ArrayList();
        for (int i = 0; i < workers.size(); i++) {
            xAxisLables.add(workers.get(i).getName());
        }

//        ArrayList<String> names = (ArrayList<String>) getNames(workers);
        XAxis xAxis = stats_BCH_chart.getXAxis();
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setLabelRotationAngle(-90);
        stats_BCH_chart.setVisibleXRangeMaximum(7);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLables));

//        (String[]) getNames(workers).toArray()

    }

    public ArrayList<String> getNamesLabels() {
        ArrayList<String> label = new ArrayList<>();
        for (int i = 0; i < workers.size(); i++) {
            label.add(workers.get(i).getName());
        }
        return label;
    }

    private ArrayList<BarEntry> dataValues() {
        ArrayList<BarEntry> vals = new ArrayList<>();
        for (int i = 0; i < workers.size(); i++) {
            vals.add(new BarEntry(i, new float[]{workers.get(i).getAssignmentsDoneWeek(), new Random().nextInt(5)}));
        }
        return vals;
    }

    private List<String> getNames(ArrayList<Worker> workers) {
        ArrayList<String> names = new ArrayList<>();
        for (Worker worker : workers) {
            names.add(worker.getName());
        }
        return names;
    }

    private void findViews() {
        stats_LBL_title = view.findViewById(R.id.stats_LBL_title);
        stats_LST_workers = view.findViewById(R.id.stats_LST_workers);
        stats_BCH_chart = view.findViewById(R.id.stats_BCH_chart);
    }

}
