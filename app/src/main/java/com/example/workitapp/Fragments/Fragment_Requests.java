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

import com.example.workitapp.Adapter_AssignmentM;
import com.example.workitapp.Adapter_Requests;
import com.example.workitapp.AssignmentMockDB;
import com.example.workitapp.More.CompareByAssignmentsDoneWeekly;
import com.example.workitapp.More.Constants;
import com.example.workitapp.More.MyCallBack;
import com.example.workitapp.Objects.Assignment;
import com.example.workitapp.Objects.MyFirebase;
import com.example.workitapp.Objects.Request;
import com.example.workitapp.R;
import com.example.workitapp.Objects.Worker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Fragment_Requests extends MyFragment {
    private RecyclerView requests_LST_requests;
    private View view;
    private Context context;

    private Dialog mDialog;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Workers");
    private Worker w;

    private MyCallBack callBack;

    private ArrayList<Request> requests;
    private Adapter_Requests adapter_requests;

    private ArrayList<Worker> workers = new ArrayList<>();
    private Worker selectedWorker;

    private ValueEventListener workerChangedListener;
    private ValueEventListener requestsChangedListener;
    private Adapter_Requests.MyItemClickListener requestsClickListener;
//    private ValueEventListener divisionChangedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_requests, container, false);
        context = view.getContext();
        findViews();
        initListeners();
        getWorkers2();
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
        MyFirebase.getInstance().getFdb().getReference().removeEventListener(requestsChangedListener);
//        MyFirebase.getInstance().getFdb().getReference().removeEventListener(divisionChangedListener);
    }

    private void initListeners() {
        initWorkerListener();
        initRequestsListener();
//        initDivisionListener();
    }

//    private void initDivisionListener() {
//        divisionChangedListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                workers = new ArrayList<>();
//                getAllUids((Map<String, String>) snapshot.getValue());
//                Log.d("aaa", "hello");
////                String[] uids = snapshot.getValue(String[].class);
////                Log.d("aaa", uids.toString());
////                for (String uid : uids) {
////                    getWorker(uid);
////                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        };
//    }

    private void initRequestsListener() {
        requestsChangedListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("aaa", "Children : " + snapshot.getChildren());
                getAllUids(snapshot.getChildren());
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
                try {
                    Worker tmpW = snapshot.getValue(Worker.class);
                    if (tmpW.getAssignments() == null)
                        tmpW.setAssignments(new ArrayList<>());
//                Log.d("aaa", "uid : " + tmpW.getUid());
                    if (!workers.contains(tmpW)) {
                        Log.d("aaa", "not Contains " + tmpW.getName());
//                    workers.remove(tmpW);
                        workers.add(tmpW);
                    } else {
                        Log.d("aaa", "Contains " + tmpW.getName());
                    }
                }catch (Exception e){

                }
                workers.sort(new CompareByAssignmentsDoneWeekly());
                initViews();
                updateViews();
//                adapter_requests.notifyItemRangeInserted(0, requests.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }

    private void getWorkers2() {
//        getAllUids();
        DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.REQUESTS_PATH);
        divRef.addValueEventListener(requestsChangedListener);
    }

//    public void getDivision(int division) {
//        DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.DIVISION_PATH);
//        divRef = divRef.child(division + "");
//        divRef.addValueEventListener(divisionChangedListener);
//    }

    private void getAllUids(Iterable<DataSnapshot> children) {
        Log.d("aaa", "init requests");
        requests = new ArrayList<>();
        for (DataSnapshot child : children) {
            Log.d("aaa", child.getKey() + ", " + child.toString());
            String uid = child.getKey();
            Request req = child.getValue(Request.class);
            requests.add(req);
//            adapter_requests.notifyItemRangeInserted(0, requests.size());
            Log.d("aaa", req.toString());
            DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH);
            divRef = divRef.child(req.getUid());
            divRef.addValueEventListener(workerChangedListener);
        }
    }

    // type 0-manager, 1 worker
//    public void getManager(String uid, int type) {
////        final Worker[] worker = new Worker[1];
//        DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH);
//        divRef = divRef.child(uid);
//        divRef.addValueEventListener(managerChangedListener);
////        return worker[0];
//    }


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
        adapter_requests.setClickListener(requestsClickListener);
    }

    private void initViews() {
//        requests = AssignmentMockDB.generateReqs();
        adapter_requests = new Adapter_Requests(context, requests, workers);

        mDialog = new Dialog(context);

//        adapter_requests.setClickListener(requestsClickListener);
        requestsClickListener = new Adapter_Requests.MyItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Request tmpR = requests.get(position);
                openInfo(tmpR);
            }

            @Override
            public void onFinishAssignment(View v, Request a) {

            }

        };

        requests_LST_requests.setLayoutManager(new LinearLayoutManager(context));
        requests_LST_requests.setAdapter(adapter_requests);
    }

    public Worker getWorkerFromReq(Request r) {
        Worker tmp = new Worker();
        tmp.setUid(r.getUid());
        tmp = workers.get(workers.indexOf(tmp));
        return tmp;
    }

    private void openInfo(Request r) {
        mDialog.setContentView(R.layout.popup_requests);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        TextView popupRequest_LBL_name = mDialog.findViewById(R.id.popupRequest_LBL_name);
        TextView popupRequest_LBL_email = mDialog.findViewById(R.id.popupRequest_LBL_email);
        TextView popupRequest_LBL_division = mDialog.findViewById(R.id.popupRequest_LBL_division);
        TextView popupRequest_LBL_date = mDialog.findViewById(R.id.popupRequest_LBL_date);
        Button popupRequest_BTN_approve = mDialog.findViewById(R.id.popupRequest_BTN_approve);
        Button popupRequest_BTN_decline = mDialog.findViewById(R.id.popupRequest_BTN_decline);
        ImageView popupRequest_IMG_close = mDialog.findViewById(R.id.popupRequest_IMG_close);

        Worker tmp = getWorkerFromReq(r);

        popupRequest_LBL_email.setText(tmp.getEmail());
        popupRequest_LBL_name.setText(tmp.getName());
        popupRequest_LBL_division.setText(tmp.getDivisionID() + "");
        popupRequest_LBL_date.setText(r.getDate() + "");

        popupRequest_IMG_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        popupRequest_BTN_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, tmp.getName() + "Approved !", Toast.LENGTH_SHORT).show();
                answerReq(r.getUid(), true);
                mDialog.dismiss();
            }
        });
        popupRequest_BTN_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, tmp.getName() + "Declined.", Toast.LENGTH_SHORT).show();
                answerReq(r.getUid(), false);
                mDialog.dismiss();
            }
        });
        mDialog.show();

    }

    private void answerReq(String uid, boolean answer) {
        MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(uid).child(Constants.IS_ACCEPTED).setValue(answer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    removeRquest(uid);
                    if(!answer)
                        removeWorker(uid);
                } else {
                    Toast.makeText(context, "Cannot Assign.", Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        });
    }

    private void removeWorker(String uid) {
        MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(uid).removeValue();
    }

    private void removeRquest(String uid) {
        MyFirebase.getInstance().getFdb().getReference(Constants.REQUESTS_PATH).child(uid).removeValue();
    }

    private void approveReq(String uid) {
    }

    private List<String> getNames(ArrayList<Worker> workers) {
        ArrayList<String> names = new ArrayList<>();
        for (Worker worker : workers) {
            names.add(worker.getName());
        }
        return names;
    }

    private void findViews() {
        requests_LST_requests = view.findViewById(R.id.requests_LST_requests);
    }
}
