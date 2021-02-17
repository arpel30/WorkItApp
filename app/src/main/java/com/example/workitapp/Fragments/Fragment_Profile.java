package com.example.workitapp.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
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
import com.example.workitapp.More.Constants;
import com.example.workitapp.Objects.MyFirebase;
import com.example.workitapp.Objects.Worker;
import com.example.workitapp.R;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private String myUri;

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
        w = new Worker("Moshe Levi", "moshelevi@gmail.com", "not", 12);
        w.setAssignments(AssignmentMockDB.generateMovies());
    }

    private void initViews() {
        MyFirebase.getInstance().getFst().getReference().child(Constants.PROFILE_FOLDER + "K5STDN1L6qNbSzcNV25VTRdCTIZ2.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                myUri = uri.toString();
                Glide.with(context)
                        .load(myUri)
                        .into(profile_IMG_pic);
                Log.d("aaa", uri.toString());
                // Got the download URL for 'users/me/profile.png'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        profile_IMG_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        w.setAssignmentsDoneWeek(10);
        w.setAssignmentsDoneAll(60);
        int tasksLeft = w.getAssignments().size();
        profile_LBL_name.setText(w.getName());
        profile_LBL_email.setText(w.getEmail());
        profile_LBL_division.setText("Division : " + w.getDivisionID());
        profile_LBL_date.setText("Starting Date : " + w.getStartDate().toString());
        profile_PRB_week.setProgress((int) ((w.getAssignmentsDoneWeek() * 100) / (w.getAssignmentsDoneWeek() + tasksLeft)), true);
        profile_PRB_allTime.setProgress((int) ((w.getAssignmentsDoneAll() * 100) / (w.getAssignmentsDoneAll() + tasksLeft)), true);
        profile_PRB_left.setTotal(tasksLeft);
        profile_PRB_left.setProgress(tasksLeft, true);
//        (Activity_Base)getActivity().setIma
    }

    public int checkPermissions() {
        int permissions = 3;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return 0;
        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return 2;
        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return 1;
        }
        return permissions;
    }

    private void takePicture() {
        int permissions = checkPermissions(); // 0 - all, 1 - just camera, 2 - just gallery
        switch (permissions) {
            case 3:
                requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.MY_CAMERA_REQUEST_CODE);
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.READ_EXTERNAL_STORAGE_CODE);
                break;
            case 2:
                requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.MY_CAMERA_REQUEST_CODE);
                break;
            case 1:
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.MY_CAMERA_REQUEST_CODE);
                break;
            case 0:
                break;
        }
        permissions = checkPermissions(); // 0 - all, 1 - just camera, 2 - just gallery
        switch (permissions) {
            case 2:
                ImagePicker.Companion.with(this)
                        .galleryOnly()
                        .cropOval()
                        .compress(200)
                        .start();
                break;
            case 1:
                ImagePicker.Companion.with(this)
                        .cameraOnly()
                        .cropOval()
                        .compress(200)
                        .start();
                break;
            case 0:
                ImagePicker.Companion.with(this)
                        .cropOval()
                        .compress(200)
                        .start();
                break;
        }
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.MY_CAMERA_REQUEST_CODE);
//        } else {
//            permissions = 1;
//            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.READ_EXTERNAL_STORAGE_CODE);
//            } else {
//                ImagePicker.Companion.with(this)
//                        .cropOval()
//                        .compress(200)
//                        .start();
//            }
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            myUri = data.getData().toString();
            Glide.with(context)
                    .load(myUri)
                    .into(profile_IMG_pic);
            MyFirebase.getInstance().getFst().getReference(Constants.PROFILE_FOLDER + "123").putFile(data.getData());
        }
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