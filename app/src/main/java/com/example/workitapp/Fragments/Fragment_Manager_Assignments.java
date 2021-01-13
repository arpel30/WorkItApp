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

import com.example.workitapp.Adapter_AssignmentM;
import com.example.workitapp.Adapter_AssignmentW;
import com.example.workitapp.AssignmentMockDB;
import com.example.workitapp.More.MyCallBack;
import com.example.workitapp.Objects.Assignment;
import com.example.workitapp.Objects.Worker;
import com.example.workitapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Manager_Assignments extends MyFragment {
    private RecyclerView assignM_LST_assignments;
    private View view;
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
        assignments = AssignmentMockDB.generateMovies();
        adapter_movie = new Adapter_AssignmentM(context, assignments);

        mDialog = new Dialog(context);

        adapter_movie.setClickListener(new Adapter_AssignmentM.MyItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Assignment tmpA = assignments.get(position);
                openInfo(tmpA);
            }

            @Override
            public void onFinishAssignment(View view, Assignment assignment) {

            }
        });

        assignM_LST_assignments.setLayoutManager(new LinearLayoutManager(context));
        assignM_LST_assignments.setAdapter(adapter_movie);
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
        popupAM_SCC_workers.addItems(getNames(workers),0);
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
                Toast.makeText(context, selectedWorker.getName() + " will do " + assignment.getTitle(), Toast.LENGTH_SHORT).show();
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
        assignM_LST_assignments = view.findViewById(R.id.assignM_LST_assignments);
    }


}
