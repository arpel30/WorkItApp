package com.example.workitapp.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workitapp.Activities.Activity_Login;
import com.example.workitapp.Activities.Activity_Request;
import com.example.workitapp.Adapter_AssignmentM;
import com.example.workitapp.AssignmentMockDB;
import com.example.workitapp.More.CompareByAssignmentsDoneWeekly;
import com.example.workitapp.More.Constants;
import com.example.workitapp.More.MyCallBack;
import com.example.workitapp.Objects.Assignment;
import com.example.workitapp.Objects.MyFirebase;
import com.example.workitapp.Objects.Worker;
import com.example.workitapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_Manager_Assignments extends MyFragment {
    private RecyclerView assignM_LST_assignments;
    private View view;
    private Button assignM_BTN_add;
    private Context context;

    private Dialog mDialog;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Workers");
    private Worker w;

    private MyCallBack callBack;

    private ArrayList<Assignment> assignments;
    private Adapter_AssignmentM adapter_movie;

    private ArrayList<Worker> workers = new ArrayList<>();
    private Worker selectedWorker;

    //    private ValueEventListener assignmentsListener;
    private ValueEventListener workerChangedListener;
    private ValueEventListener managerChangedListener;
    private ValueEventListener divisionChangedListener;
    private Adapter_AssignmentM.MyItemClickListener assignmentClickListener;

    private Worker manager;
    ArrayList<String> allUids;

//    private ArrayList<Worker> workersNames = new ArrayList<>();

    public void setCallBack(MyCallBack _callBack) {
        this.callBack = _callBack;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manager_assignments, container, false);
        context = view.getContext();
        findViews();
        initListeners();
        getWorkers2();
//        getWorkers();
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
//        MyFirebase.getInstance().getFdb().getReference().removeEventListener(assignmentsListener);
//        MyFirebase.getInstance().getFdb().getReference().removeEventListener(workerChangedListener);
        MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(manager.getUid()).removeEventListener(managerChangedListener);
        MyFirebase.getInstance().getFdb().getReference(Constants.DIVISION_PATH).child(manager.getDivisionID() + "").removeEventListener(divisionChangedListener);
        for (String uid : allUids) {
            MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(uid).removeEventListener(workerChangedListener);
        }
    }

    private void initListeners() {
//        initAssignmentsListener();
        initWorkerListener();
        initManagerListener();
        initDivisionListener();
    }

//    private void initAssignmentsListener() {
//        assignmentsListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                Log.d("aaa", snapshot.toString());
////                Log.d("aaa", snapshot.getChildrenCount()+"");
////                Log.d("aaa", snapshot.getChildren().toString());
//                ArrayList<Assignment> assi = new ArrayList<>();
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    Assignment a1 = ds.getValue(Assignment.class);
////                    Log.d("aaa", "a1 = "+a1);
////                    Log.d("aaa", "a1 = " + ds.getValue(Assignment.class));
//                    assi.add(a1);
////            getWorker(uid, Constants.WORKER_ID);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        };
//    }

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
                manager = tmpW;
                Log.d("aaa", tmpW.toString());
//                tmpW.setAssignments(new ArrayList<>());
                Log.d("aaa", "aft");
                workers = new ArrayList<>();
                initViews();
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
                if (workers.contains(tmpW)) {
                    workers.remove(tmpW);
                }
//                getAssignment(tmpW.getUid());
                workers.add(tmpW);
                workers.sort(new CompareByAssignmentsDoneWeekly());
                updateViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }

//    private void getAssignment(String uid) {
//        DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.ASSIGNMENTS);
//        divRef = divRef.child(uid);
//        divRef.addValueEventListener(assignmentsListener);
//        Log.d("aaa", uid);
//    }

    private void getWorkers2() {
        user = MyFirebase.getInstance().getUser();
        if (user != null) {
            getManager(user.getUid(), Constants.MANAGER_ID);
//            workers = new ArrayList<>();
//            getDivision(manager.getDivisionID());
        }
    }

    public void getDivision(int division) {
        workers = new ArrayList<>();
        DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.DIVISION_PATH);
        divRef = divRef.child(division + "");
        divRef.addValueEventListener(divisionChangedListener);
    }

    private void getAllUids(Map<String, String> uids) {
        allUids = new ArrayList<>();
        for (Map.Entry<String, String> entry : uids.entrySet()) {
            String uid = entry.getValue();
            allUids.add(uid);
            if(!uid.equals(manager.getUid())) {
                Log.d("aaa", "the uid : " + uid);
                DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH);
                divRef = divRef.child(uid);
                divRef.addValueEventListener(workerChangedListener);
            }
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

    private void updateViews() {
        adapter_movie.setClickListener(assignmentClickListener);
    }

    private void initViews() {
//        assignments = AssignmentMockDB.generateMovies();
        assignments = manager.getAssignments();
        adapter_movie = new Adapter_AssignmentM(context, assignments);

        mDialog = new Dialog(context);

        assignmentClickListener = new Adapter_AssignmentM.MyItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Assignment tmpA = assignments.get(position);
                openInfo(tmpA);
            }

            @Override
            public void onFinishAssignment(View view, Assignment assignment) {

            }
        };

        assignM_LST_assignments.setLayoutManager(new LinearLayoutManager(context));
        assignM_LST_assignments.setAdapter(adapter_movie);

        assignM_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAssignment();
            }
        });
    }

    private void addAssignment() {
    }

    private void openInfo(Assignment assignment) {
        mDialog.setContentView(R.layout.popup_assignment_m);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        TextView popupAM_LBL_title = mDialog.findViewById(R.id.popupAM_LBL_title);
        ScrollChoice popupAM_SCC_workers = mDialog.findViewById(R.id.popupAM_SCC_workers);
        TextView popupAM_LBL_date = mDialog.findViewById(R.id.popupAM_LBL_date);
        Button popupAM_BTN_close = mDialog.findViewById(R.id.popupAM_BTN_close);
        Button popupAM_BTN_done = mDialog.findViewById(R.id.popupAM_BTN_done);
        popupAM_LBL_title.setText(assignment.getTitle());

        selectedWorker = workers.get(0);
        popupAM_SCC_workers.addItems(getNames(workers), 0);
        popupAM_SCC_workers.setOnItemSelectedListener(new ScrollChoice.OnItemSelectedListener() {
            @Override
            public void onItemSelected(ScrollChoice scrollChoice, int position, String name) {
                selectedWorker = workers.get(position);
            }
        });

        popupAM_LBL_date.setText(assignment.getDueTo().toString());
        popupAM_BTN_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        popupAM_BTN_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAssignmentToWorker(assignment, selectedWorker);
//                removeAssignment(assignment);
//                updateViews();
                Toast.makeText(context, selectedWorker.getName() + " will do " + assignment.getTitle(), Toast.LENGTH_SHORT).show();
//                mDialog.dismiss();
            }
        });
        mDialog.show();

    }

    public HashMap<String, Object> getAssignmentsMap(Worker worker) {
        HashMap<String, Object> map = new HashMap<>();
//        Log.d("aaa", map.toString());
        for (Assignment assignment : worker.getAssignments()) {
            map.put(worker.getAssignments().indexOf(assignment) + "", assignment);
        }
//        Log.d("aaa", map.toString());
        return map;
    }

    private void sendAssignmentToWorker(Assignment assignment, Worker selectedWorker) {
//        Log.d("aaa", selectedWorker.getAssignments().size() + "");
        manager.getAssignments().remove(assignment);
        manager.setAssignmentsDoneAll(manager.getAssignmentsDoneAll() + 1);
        manager.setAssignmentsDoneWeek(manager.getAssignmentsDoneWeek() + 1);
        selectedWorker.addAssignment(assignment);
        HashMap<String, Object> map = getAssignmentsMap(selectedWorker);
//        Log.d("aaa", selectedWorker.getAssignments().size() + "");
//        MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(selectedWorker.getUid()).child(Constants.ASSIGNMENTS).setValue(selectedWorker.getAssignments().get(0));
//        MyFirebase.getInstance().getFdb().getReference(Constants.ASSIGNMENTS).child(selectedWorker.getUid()).setValue(selectedWorker.getAssignments()).addOnCompleteListener(new OnCompleteListener<Void>() {
        MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(selectedWorker.getUid()).setValue(selectedWorker).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    Log.d("aaa", selectedWorker.getAssignments().size() + "");
//                    getWorkers2();
                    removeAssignment(assignment);
                } else {
                    Toast.makeText(context, "Cannot Assign.", Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        });
    }

    private void removeAssignment(Assignment assignment) {
        MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(manager.getUid()).setValue(manager);
//        int position = assignments.indexOf(assignment);
//        assignments.remove(assignment);
//        assignM_LST_assignments.removeViewAt(position);
//        adapter_movie.notifyItemRemoved(position);
//        adapter_movie.notifyItemRangeChanged(position, assignments.size());
//        adapter_movie.notifyDataSetChanged();
    }

    private List<String> getNames(ArrayList<Worker> workers) {
        ArrayList<String> names = new ArrayList<>();
        for (Worker worker : workers) {
            names.add(worker.getName());
        }
        return names;
    }

    private void findViews() {
        assignM_LST_assignments = view.findViewById(R.id.assignM_LST_assignments);
        assignM_BTN_add = view.findViewById(R.id.assignM_BTN_add);
    }


}
