package com.example.workitapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.workitapp.More.Constants;
import com.example.workitapp.Objects.MyFirebase;
import com.example.workitapp.Objects.Worker;
import com.example.workitapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Unauthorized extends MyFragment {

    private View view;
    private Context context;

    Worker worker;

    private ImageView unauth_IMG_meme;
    private TextView unauth_LBL_name;
    private TextView unauth_LBL_auth;
    private ValueEventListener workerChangedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_unauthorized, container, false);
        context = view.getContext();
        findViews();
        initListener();
        getWorker();
        return view;
    }

    private void findViews() {
        unauth_IMG_meme = view.findViewById(R.id.unauth_IMG_meme);
        unauth_LBL_name = view.findViewById(R.id.unauth_LBL_name);
        unauth_LBL_auth = view.findViewById(R.id.unauth_LBL_auth);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(worker.getUid()).removeEventListener(workerChangedListener);
    }

    private void getWorker() {
        FirebaseUser user = MyFirebase.getInstance().getUser();
        String uid = null;
        if (user != null)
            uid = user.getUid();
        if (uid != null) {
            MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(uid).addValueEventListener(workerChangedListener);
        }
    }

    private void initListener() {
        workerChangedListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                worker = snapshot.getValue(Worker.class);
                initViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }

    private void initViews() {
        if (worker.getIsAccepted()) {
            unauth_LBL_name.setText(Constants.AUTH_TEXT + worker.getName());
            setImage(Constants.AUTH_PIC, unauth_IMG_meme);
            unauth_LBL_auth.setText(Constants.AUTH_ACCEPTED);
        } else {
            unauth_LBL_name.setText(Constants.UNAUTH_TEXT + worker.getName());
            setImage(Constants.UNAUTH_PIC, unauth_IMG_meme);
        }

    }
}
