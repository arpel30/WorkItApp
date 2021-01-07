package com.example.workitapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workitapp.Adapter_AssignmentW;
import com.example.workitapp.AssignmentMockDB;
import com.example.workitapp.More.MyCallBack;
import com.example.workitapp.Objects.Assignment;
import com.example.workitapp.R;
import com.example.workitapp.Objects.Worker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_Assignments extends MyFragment {
    private RecyclerView assign_LST_assignments;
    private View view;
    private Context context;

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
        view = inflater.inflate(R.layout.fragment_assignments, container, false);
        context = view.getContext();
        findViews();
        initViews();

        return view;
    }

    private void initViews() {
        assignments = AssignmentMockDB.generateMovies();
        adapter_movie = new Adapter_AssignmentW(context, assignments);
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
        new AlertDialog.Builder(context)
                .setTitle(assignment.getTitle() +" : " + assignment.getDueTo().toString())
                .setMessage(assignment.getDescription())
                .setPositiveButton("Close", null)
                .show();
    }

    private void findViews() {
        assign_LST_assignments = view.findViewById(R.id.assign_LST_assignments);
    }



}
