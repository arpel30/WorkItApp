package com.example.workitapp.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.workitapp.More.Constants;
import com.example.workitapp.Objects.MyFirebase;
import com.example.workitapp.Objects.Worker;
import com.example.workitapp.R;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.vaibhavlakhera.circularprogressview.CircularProgressView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Profile extends MyFragment {

    private View view;
    private Context context;

    private CircleImageView profile_IMG_pic;
    private TextView profile_LBL_name;
    private TextView profile_LBL_email;
    private TextView profile_LBL_division;
    private TextView profile_LBL_position;
    private TextView profile_LBL_date;
    private CircularProgressView profile_PRB_week;
    private CircularProgressView profile_PRB_allTime;
    private CircularProgressView profile_PRB_left;

    private Worker w;
    private String myUri;
    private ValueEventListener workerChanged;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = view.getContext();
        findViews();
        initListener();
        getWorkers();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(w.getUid()).removeEventListener(workerChanged);
    }

    private void getWorkers() {

        FirebaseUser user = MyFirebase.getInstance().getUser();
        String uid = null;
        if (user != null)
            uid = user.getUid();
        if (uid != null) {
            MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(uid).addValueEventListener(workerChanged);
        }
    }

    private void initViews() {
        Log.d("aaa", "init : " + w.getImgUrl());
        if (!w.getImgUrl().equals(Constants.DEFAULT))
            setImage(w.getImgUrl(), profile_IMG_pic, context);
        else
            profile_IMG_pic.setImageResource(Constants.PROFILE_DEFAULT); // vector drawable
        profile_IMG_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        int tasksLeft = w.getAssignments().size();
        profile_LBL_name.setText(w.getName());
        profile_LBL_email.setText(w.getEmail());
        profile_LBL_position.setText(getPosition(w.getType()));
        profile_LBL_division.setText("Division : " + w.getDivisionID());
        profile_LBL_date.setText("Starting Date : " + w.getStartDate().toString());
        if (tasksLeft == 0) {
            profile_PRB_week.setProgress(100, true);
            profile_PRB_allTime.setProgress(100, true);
        } else {
            profile_PRB_week.setProgress((int) ((w.getAssignmentsDoneWeek() * 100) / (w.getAssignmentsDoneWeek() + tasksLeft)), true);
            profile_PRB_allTime.setProgress((int) ((w.getAssignmentsDoneAll() * 100) / (w.getAssignmentsDoneAll() + tasksLeft)), true);
        }
        profile_PRB_left.setTotal(tasksLeft);
        profile_PRB_left.setProgress(tasksLeft, true);
    }

    private String getPosition(int type) {
        switch (type) {
            case Constants.MANAGER_ID:
                return "Manager";
            case Constants.HR_ID:
                return "HR Worker";
        }
        return "Worker";
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
    }

    private void initListener() {
        workerChanged = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                w = snapshot.getValue(Worker.class);
                if (w.getIsAccepted())
                    initViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            myUri = data.getData().toString();
            MyFirebase.getInstance().getFst().getReference(Constants.PROFILE_FOLDER + w.getUid()).putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    getUri();
                }
            });
        }
    }

    private void getUri() {
        MyFirebase.getInstance().getFst().getReference().child(Constants.PROFILE_FOLDER + w.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                myUri = uri.toString();
                MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(w.getUid()).child(Constants.IMG_URL).setValue(myUri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
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
        profile_LBL_position = view.findViewById(R.id.profile_LBL_position);
    }
}