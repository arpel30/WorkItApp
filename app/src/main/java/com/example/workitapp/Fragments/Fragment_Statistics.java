package com.example.workitapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.workitapp.More.Constants;
import com.example.workitapp.More.MyCallBack;
import com.example.workitapp.Objects.MySPV;
import com.example.workitapp.R;
import com.example.workitapp.Objects.Worker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Statistics extends MyFragment {

    private TextView stats_LBL_uid;
    private View view;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Workers");
    private Worker w;

    private MyCallBack callBack;

    public void setCallBack(MyCallBack _callBack) {
        this.callBack = _callBack;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        String uid = "NOT found";
        if(MySPV.getInstance().getBool(Constants.REMEMBER)) {
            uid = MySPV.getInstance().getString(Constants.UID, null);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user !=null) {
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
//                    updateFavoriteProduct();
                    Log.d("pttt", "Value is: " + value);
                } catch (Exception ex) { }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("pttt", "Failed to read value.", error.toException());
            }
        });
        return view;
    }

    private void findViews() {
        Log.d("aaa", "views");
        stats_LBL_uid = view.findViewById(R.id.stats_LBL_uid);
    }

    private void changeText(String uid) {
        stats_LBL_uid.setText(uid);
    }


}
