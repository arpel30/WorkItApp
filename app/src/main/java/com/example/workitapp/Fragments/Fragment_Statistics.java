package com.example.workitapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workitapp.Adapters.Adapter_Statistics;
import com.example.workitapp.More.CompareByAssignmentsDoneWeekly;
import com.example.workitapp.More.Constants;
import com.example.workitapp.Objects.MyFirebase;
import com.example.workitapp.R;
import com.example.workitapp.Objects.Worker;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vaibhavlakhera.circularprogressview.CircularProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Fragment_Statistics extends MyFragment {

    private BarChart stats_BCH_chart;
    private RecyclerView stats_LST_workers;
    private TextView stats_LBL_title;
    private View view;
    private Context context;

    private CircularProgressView profile_PRB_left;
    private CircularProgressView profile_PRB_week;
    private CircularProgressView profile_PRB_allTime;

    private int allTime;
    private int week;
    private int left;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Workers");
    private Worker manager;

    private Adapter_Statistics adapter_statistics;
    private ArrayList<Worker> workers = new ArrayList<>();

    private ValueEventListener workerChangedListener;
    private ValueEventListener managerChangedListener;
    private ValueEventListener divisionChangedListener;

    ArrayList<String> allUids;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        context = view.getContext();
        findViews();
        initListeners();
        getWorkers2();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        removeListeners();
    }

    @Override
    protected void removeListeners() {
        MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(manager.getUid()).removeEventListener(managerChangedListener);
        MyFirebase.getInstance().getFdb().getReference(Constants.DIVISION_PATH).child(manager.getDivisionID() + "").removeEventListener(divisionChangedListener);
        for (String uid : allUids) {
            MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(uid).removeEventListener(workerChangedListener);
        }
    }

    private void initListeners() {
        initWorkerListener();
        initManagerListener();
        initDivisionListener();
    }

    private void initDivisionListener() {
        divisionChangedListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                workers = new ArrayList<>();
                getAllUids((Map<String, String>) snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }

    private void initManagerListener() {
        managerChangedListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Worker tmpW = snapshot.getValue(Worker.class);
                manager = tmpW;
                if (tmpW.getAssignments() == null)
                    tmpW.setAssignments(new ArrayList<>());
                getDivision(tmpW.getDivisionID());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }

    private void initWorkerListener() {
        workerChangedListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Worker tmpW = snapshot.getValue(Worker.class);
                if (tmpW.getAssignments() == null)
                    tmpW.setAssignments(new ArrayList<>());
                if (!workers.contains(tmpW)) {
                    workers.add(tmpW);
                } else {
                    workers.remove(tmpW);
                    workers.add(tmpW);
                }
                workers.sort(new CompareByAssignmentsDoneWeekly());
                initViews();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }

    private void getWorkers2() {
        user = MyFirebase.getInstance().getUser();
        if (user != null) {
            getManager(user.getUid(), Constants.MANAGER_ID);
        }
    }

    public void getDivision(int division) {
        DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.DIVISION_PATH);
        divRef = divRef.child(division + "");
        divRef.addValueEventListener(divisionChangedListener);
    }

    private void getAllUids(Map<String, String> uids) {
        allUids = new ArrayList<>();
        for (Map.Entry<String, String> entry : uids.entrySet()) {
            String uid = entry.getValue();
            allUids.add(uid);
            DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH);
            divRef = divRef.child(uid);
            divRef.addValueEventListener(workerChangedListener);
        }
    }

    // type 0-manager, 1 worker
    public void getManager(String uid, int type) {
        DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH);
        divRef = divRef.child(uid);
        divRef.addValueEventListener(managerChangedListener);
    }

    private void initViews() {
        adapter_statistics = new Adapter_Statistics(context, workers);

        adapter_statistics.setClickListener(new Adapter_Statistics.MyItemClickListener() {
        });

        stats_LST_workers.setLayoutManager(new LinearLayoutManager(context));
        stats_LST_workers.setAdapter(adapter_statistics);

        getParams();
        int[] colorArr = new int[]{Color.CYAN, Color.RED};

        BarDataSet barDataSet = new BarDataSet(dataValues(), "");
        barDataSet.setColors(colorArr);
        barDataSet.setStackLabels(new String[]{"Done", "Not Done"});

        BarData barData = new BarData(barDataSet);
        stats_BCH_chart.setData(barData);
        stats_BCH_chart.setScaleEnabled(false);
        stats_BCH_chart.setDescription(null);

        ArrayList<String> xAxisLables = new ArrayList();
        for (int i = 0; i < workers.size(); i++) {
            xAxisLables.add(workers.get(i).getName());
        }
        XAxis xAxis = stats_BCH_chart.getXAxis();
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        stats_BCH_chart.setVisibleXRangeMaximum(7);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLables));
        stats_BCH_chart.invalidate();
    }

    private void getParams() {
        allTime = 0;
        week = 0;
        left = 0;
        for (Worker worker : workers) {
            allTime += worker.getAssignmentsDoneAll();
            week += worker.getAssignmentsDoneWeek();
            left += worker.getAssignments().size();
        }
        profile_PRB_left.setTotal(left);
        profile_PRB_allTime.setTotal(allTime+left);
        profile_PRB_week.setTotal(week+left);
        profile_PRB_left.setProgress(left, true);
        profile_PRB_allTime.setProgress(allTime, true);
        profile_PRB_week.setProgress(week, true);
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
            vals.add(new BarEntry(i, new float[]{workers.get(i).getAssignmentsDoneWeek(), workers.get(i).getAssignments().size()}));
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

        profile_PRB_allTime = view.findViewById(R.id.profile_PRB_allTime);
        profile_PRB_week = view.findViewById(R.id.profile_PRB_week);
        profile_PRB_left = view.findViewById(R.id.profile_PRB_left);
    }
}
