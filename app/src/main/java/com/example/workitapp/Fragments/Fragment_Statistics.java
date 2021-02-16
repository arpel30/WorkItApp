package com.example.workitapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workitapp.Adapter_Statistics;
import com.example.workitapp.More.CompareByAssignmentsDoneWeekly;
import com.example.workitapp.More.Constants;
import com.example.workitapp.More.MyCallBack;
import com.example.workitapp.Objects.Manager;
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
    private Worker w;
    private Manager manager;

    private MyCallBack callBack;

    private Adapter_Statistics adapter_statistics;
    private ArrayList<Worker> workers = new ArrayList<>();

    private ValueEventListener workerChangedListener;
    private ValueEventListener managerChangedListener;
    private ValueEventListener divisionChangedListener;

    public void setCallBack(MyCallBack _callBack) {
        this.callBack = _callBack;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        context = view.getContext();
        findViews();
        initListeners();
        getWorkers2();
//        workers = new ArrayList<>();
//        Log.d("aaa", workers.toString());
//        getWorker("oj1ySRivKKX3rze9s5IyyKGOkrg2");
//        Log.d("aaa", workers.toString());
//        getDivision(1);
//        workers.sort(new CompareByAssignmentsDoneWeekly());
//        initViews();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        removeListeners();
    }

    @Override
    protected void removeListeners() {
        MyFirebase.getInstance().getFdb().getReference().removeEventListener(workerChangedListener);
        MyFirebase.getInstance().getFdb().getReference().removeEventListener(managerChangedListener);
        MyFirebase.getInstance().getFdb().getReference().removeEventListener(divisionChangedListener);
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
//                Log.d("aaa", "hello");
//                String[] uids = snapshot.getValue(String[].class);
//                Log.d("aaa", uids.toString());
//                for (String uid : uids) {
//                    getWorker(uid);
//                }
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
                if(tmpW.getAssignments() == null)
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
                if(tmpW.getAssignments() == null)
                    tmpW.setAssignments(new ArrayList<>());

                if (workers.contains(tmpW)) {
                    workers.remove(tmpW);
                }
                workers.add(tmpW);
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
//            workers = new ArrayList<>();
//            getDivision(manager.getDivisionID());
        }
    }

    public void getDivision(int division) {
//        workers = new ArrayList<>();
        DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.DIVISION_PATH);
        divRef = divRef.child(division + "");
        divRef.addValueEventListener(divisionChangedListener);
    }

    private void getAllUids(Map<String, String> uids) {
        for (Map.Entry<String, String> entry : uids.entrySet()) {
            String uid = entry.getValue();
            Log.d("aaa", uid);
            DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH);
            divRef = divRef.child(uid);
            divRef.addValueEventListener(workerChangedListener);
//            getWorker(uid, Constants.WORKER_ID);
        }
    }

    // type 0-manager, 1 worker
    public void getManager(String uid, int type) {
//        final Worker[] worker = new Worker[1];
        DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH);
        divRef = divRef.child(uid);
        divRef.addValueEventListener(managerChangedListener);
//        return worker[0];
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
//        Log.d("aaa", workers.toString());
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

//        ArrayList<String> names = (ArrayList<String>) getNames(workers);
        XAxis xAxis = stats_BCH_chart.getXAxis();
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setLabelRotationAngle(-90);
        stats_BCH_chart.setVisibleXRangeMaximum(7);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLables));
        stats_BCH_chart.invalidate();
//        (String[]) getNames(workers).toArray()

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
        profile_PRB_allTime.setTotal(allTime * 100);
        profile_PRB_week.setTotal(week * 100 + 1000);
        profile_PRB_left.setProgress(left, true);
        profile_PRB_allTime.setProgress(allTime * 40, true);
        profile_PRB_week.setProgress(week * 90, true);

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
