package com.example.workitapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.workitapp.R;
import com.example.workitapp.Objects.Worker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Requests extends MyFragment {
    private TextView reqs_LBL_uid;
    private Worker w;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_requests, container, false);

        String uid = "NOT found";
        if (user != null) {
            uid = user.getUid();
        }
        findViews();
        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Worker value = dataSnapshot.getValue(Worker.class);
                    w = value;
                    changeText("Hi " + w.getName());
                    Log.d("pttt", "Value is: " + value);
                } catch (Exception ex) {
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("pttt", "Failed to read value.", error.toException());
            }
        });
        return view;
    }

    private void findViews() {
        reqs_LBL_uid = view.findViewById(R.id.reqs_LBL_uid);
    }

    private void changeText(String uid) {
        reqs_LBL_uid.setText(uid);
    }

}
