package com.example.workitapp.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.example.workitapp.Adapters.Adapter_Requests;
import com.example.workitapp.More.Constants;
import com.example.workitapp.Objects.MyFirebase;
import com.example.workitapp.Objects.Request;
import com.example.workitapp.R;
import com.example.workitapp.Objects.Worker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Requests extends MyFragment {
    private RecyclerView requests_LST_requests;
    private View view;
    private Context context;

    private Dialog mDialog;

    private ArrayList<Request> requests;
    private Adapter_Requests adapter_requests;

    private ArrayList<Worker> workers = new ArrayList<>();

    private ValueEventListener workerChangedListener;
    private ValueEventListener requestsChangedListener;
    private Adapter_Requests.MyItemClickListener requestsClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_requests, container, false);
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
        MyFirebase.getInstance().getFdb().getReference().removeEventListener(workerChangedListener);
        MyFirebase.getInstance().getFdb().getReference().removeEventListener(requestsChangedListener);
    }

    private void initListeners() {
        initWorkerListener();
        initRequestsListener();
    }

    private void initRequestsListener() {
        requestsChangedListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                Worker tmpW = snapshot.getValue(Worker.class);
                if (!workers.contains(tmpW)) {
                    workers.add(tmpW);
                }else{ // contains worker but one of the non identifying details has changed (remove&add will update the worker's entity)
                    workers.remove(tmpW);
                    workers.add(tmpW);
                }
                initViews();
                updateViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }

    private void getWorkers2() {
        DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.REQUESTS_PATH);
        divRef.addValueEventListener(requestsChangedListener);
    }

    private void getAllUids(Iterable<DataSnapshot> children) {
        requests = new ArrayList<>();
        for (DataSnapshot child : children) {
            String uid = child.getKey();
            Request req = child.getValue(Request.class);
            requests.add(req);
            DatabaseReference divRef = MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH);
            divRef = divRef.child(req.getUid());
            divRef.addValueEventListener(workerChangedListener);
        }
    }

    private void updateViews() {
        adapter_requests.setClickListener(requestsClickListener);
    }

    private void initViews() {
        adapter_requests = new Adapter_Requests(context, requests, workers);
        mDialog = new Dialog(context);
        requestsClickListener = new Adapter_Requests.MyItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Request tmpR = requests.get(position);
                openInfo(tmpR);
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
                Toast.makeText(context, tmp.getName() + " Approved !", Toast.LENGTH_SHORT).show();
                answerReq(r.getUid(), true, r, tmp.getDivisionID());
                mDialog.dismiss();
            }
        });
        popupRequest_BTN_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, tmp.getName() + " Declined.", Toast.LENGTH_SHORT).show();
                answerReq(r.getUid(), false, r, tmp.getDivisionID());
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    private void answerReq(String uid, boolean answer, Request r, int division) {
        MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(uid).child(Constants.IS_ACCEPTED).setValue(answer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    removeRquest(uid);
                    removeFromRecycler(r);
                    if (answer)
                        addWorkerToDivision(uid, division);
                } else {
                    Toast.makeText(context, "Cannot Assign.", Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        });
    }

    private void addWorkerToDivision(String uid, int division) {
        MyFirebase.getInstance().getFdb().getReference().child(Constants.DIVISION_PATH).child(division + "").child(uid).setValue(uid);
    }

    private void removeFromRecycler(Request r) {
        int position = requests.indexOf(r);
        requests.remove(r);
        requests_LST_requests.removeViewAt(position);
        adapter_requests.notifyItemRemoved(position);
        adapter_requests.notifyItemRangeChanged(position, requests.size());
        adapter_requests.notifyDataSetChanged();
    }

    private void removeWorker(String uid, int division) {
    }

    private void removeRquest(String uid) {
        MyFirebase.getInstance().getFdb().getReference(Constants.REQUESTS_PATH).child(uid).removeValue();
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
