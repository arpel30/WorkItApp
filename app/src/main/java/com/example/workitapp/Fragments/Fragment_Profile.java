package com.example.workitapp.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.workitapp.Activities.Activity_Base;
import com.example.workitapp.AssignmentMockDB;
import com.example.workitapp.Objects.Worker;
import com.example.workitapp.R;
import com.vaibhavlakhera.circularprogressview.CircularProgressView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Profile extends Fragment {

    private View view;
    private Context context;

    private CircleImageView profile_IMG_pic;
    private TextView profile_LBL_name;
    private TextView profile_LBL_email;
    private TextView profile_LBL_division;
    private TextView profile_LBL_date;
    private CircularProgressView profile_PRB_week;
    private CircularProgressView profile_PRB_allTime;
    private CircularProgressView profile_PRB_left;


    private Worker w;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = view.getContext();
        findViews();
        getWorkers();
        initViews();
        return view;
    }

    private void getWorkers() {
        w = new Worker("Moshe Levi", "moshelevi@gmail.com","not",12);
        w.setAssignments(AssignmentMockDB.generateMovies());
    }

    private void initViews() {
        Glide
                .with(this)
                .load(R.drawable.unauth_pic)
                .into(profile_IMG_pic);

        w.setAssignmentsDoneWeek(10);
        w.setAssignmentsDoneAll(60);
        int tasksLeft = w.getAssignments().size();
        profile_LBL_name.setText(w.getName());
        profile_LBL_email.setText(w.getEmail());
        profile_LBL_division.setText("Division : " + w.getDivisionID());
        profile_LBL_date.setText("Starting Date : " + w.getStartDate().toString());
        profile_PRB_week.setProgress((int)((w.getAssignmentsDoneWeek()*100)/(w.getAssignmentsDoneWeek()+tasksLeft)), true);
        profile_PRB_allTime.setProgress((int)((w.getAssignmentsDoneAll()*100)/(w.getAssignmentsDoneAll()+tasksLeft)), true);
        profile_PRB_left.setTotal(tasksLeft);
        profile_PRB_left.setProgress(tasksLeft, true);
//        (Activity_Base)getActivity().setIma
    }

    private void findViews() {
        profile_IMG_pic = view.findViewById(R.id.profile_IMG_pic);
        profile_LBL_name = view.findViewById(R.id.profile_LBL_name);
        profile_LBL_email = view.findViewById(R.id.profile_LBL_email);
        profile_LBL_division = view.findViewById(R.id.profile_LBL_division);
        profile_LBL_date = view.findViewById(R.id.profile_LBL_date);
        profile_PRB_week = view.findViewById(R.id.profile_PRB_week);
        profile_PRB_allTime = view.findViewById(R.id.profile_PRB_allTime);
        profile_PRB_left = view.findViewById(R.id.profile_PRB_left);
    }

}