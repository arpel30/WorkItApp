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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyFragment extends Fragment {
    protected View view;
    protected FirebaseUser user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        return null;
    }

    protected void removeListeners(){}

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
