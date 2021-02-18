package com.example.workitapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.workitapp.More.MyCallBack;
import com.example.workitapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyFragment extends Fragment {
    protected View view;

    protected FirebaseDatabase database = FirebaseDatabase.getInstance();
    protected DatabaseReference myRef = database.getReference("Workers");
    protected MyCallBack callBack;
    protected FirebaseUser user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();

        return null;
    }

    protected void removeListeners(){}

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        removeListeners();
//    }

    protected void setImage(int id, ImageView view){
        Glide
                .with(this)
                .load(id)
                .fitCenter()
                .into(view);
    }

    protected void setImage(String uri, ImageView view, Context context){
        Glide
                .with(context.getApplicationContext())
                .load(uri)
                .fitCenter()
                .into(view);
    }
}
