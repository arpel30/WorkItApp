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
import com.example.workitapp.More.MyCallBack;
import com.example.workitapp.Objects.Assignment;
import com.example.workitapp.Objects.Request;
import com.example.workitapp.R;
import com.example.workitapp.Objects.Worker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.util.ArrayList;
import java.util.List;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_requests, container, false);
        context = view.getContext();
        findViews();
        initViews();
        getWorkers();
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
        requests = AssignmentMockDB.generateReqs();
        adapter_requests = new Adapter_Requests(context, requests);

        mDialog = new Dialog(context);

        adapter_requests.setClickListener(new Adapter_Requests.MyItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Request tmpR = requests.get(position);
                openInfo(tmpR);
            }

            @Override
            public void onFinishAssignment(View v, Request a) {

            }

        });

        requests_LST_requests.setLayoutManager(new LinearLayoutManager(context));
        requests_LST_requests.setAdapter(adapter_requests);
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
        popupRequest_LBL_email.setText(r.getWorker().getEmail());
        popupRequest_LBL_name.setText(r.getWorker().getName());
        popupRequest_LBL_division.setText(r.getWorker().getDivisionID()+"");
        popupRequest_LBL_date.setText(r.getDate()+"");

        popupRequest_IMG_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        popupRequest_BTN_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, r.getWorker().getName() + "Approved !", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
        popupRequest_BTN_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, r.getWorker().getName() + "Declined.", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
        mDialog.show();

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
