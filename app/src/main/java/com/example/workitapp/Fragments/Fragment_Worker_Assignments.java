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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workitapp.Adapter_AssignmentW;
import com.example.workitapp.AssignmentMockDB;
import com.example.workitapp.More.MyCallBack;
import com.example.workitapp.Objects.Assignment;
import com.example.workitapp.R;
import com.example.workitapp.Objects.Worker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Fragment_Worker_Assignments extends MyFragment {
    private RecyclerView assign_LST_assignments;
    private View view;
    private Context context;

    private Dialog mDialog;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Workers");
    private Worker w;

    private MyCallBack callBack;

    private ArrayList<Assignment> assignments;
    private Adapter_AssignmentW adapter_movie;

    public void setCallBack(MyCallBack _callBack) {
        this.callBack = _callBack;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_worker_assignments, container, false);
        context = view.getContext();
        findViews();
        initViews();

        return view;
    }

    private void initViews() {
        assignments = AssignmentMockDB.generateMovies();
        adapter_movie = new Adapter_AssignmentW(context, assignments);

        mDialog = new Dialog(context);

        adapter_movie.setClickListener(new Adapter_AssignmentW.MyItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Assignment tmpA = assignments.get(position);
                openInfo(tmpA);
            }

            @Override
            public void onFinishAssignment(View view, Assignment assignment) {

            }
        });

        assign_LST_assignments.setLayoutManager(new LinearLayoutManager(context));
        assign_LST_assignments.setAdapter(adapter_movie);
    }

    private void openInfo(Assignment assignment) {
//        new AlertDialog.Builder(context)
//                .setTitle(assignment.getTitle() +" : " + assignment.getDueTo().toString())
//                .setMessage(assignment.getDescription())
//                .setPositiveButton("Close", null)
//                .show();

        mDialog.setContentView(R.layout.popup_assignment_w);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        TextView popupAW_LBL_title = mDialog.findViewById(R.id.popupAW_LBL_title);
        TextView popupAW_LBL_description = mDialog.findViewById(R.id.popupAW_LBL_description);
        TextView popupAW_LBL_date = mDialog.findViewById(R.id.popupAW_LBL_date);
        Button popupAW_BTN_close = mDialog.findViewById(R.id.popupAW_BTN_close);
        Button popupAW_BTN_done = mDialog.findViewById(R.id.popupAW_BTN_done);
        popupAW_LBL_title.setText(assignment.getTitle());
        popupAW_LBL_description.setText(assignment.getDescription());
        popupAW_LBL_date.setText(assignment.getDueTo().toString());
        popupAW_BTN_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        popupAW_BTN_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Mission " + assignment.getTitle() + " is done !", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
        mDialog.show();

    }

    private void findViews() {
        assign_LST_assignments = view.findViewById(R.id.assign_LST_assignments);
    }



}
