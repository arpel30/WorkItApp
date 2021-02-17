package com.example.workitapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.workitapp.Objects.MyFirebase;
import com.example.workitapp.R;

public class Fragment_Unauthorized extends MyFragment{

    private View view;
    private Context context;

    private ImageView unauth_IMG_meme;
    private EditText unauth_LBL_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_unauthorized, container, false);
        context = view.getContext();
//        findViews();
//        initListeners();

        return view;
    }

    private void findViews() {
        unauth_IMG_meme = view.findViewById(R.id.unauth_IMG_meme);
        unauth_LBL_name = view.findViewById(R.id.unauth_LBL_name);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        removeListeners();
    }

    @Override
    protected void removeListeners() {
//        MyFirebase.getInstance().getFdb().getReference().removeEventListener(workerChangedListener);
//        MyFirebase.getInstance().getFdb().getReference().removeEventListener(managerChangedListener);
//        MyFirebase.getInstance().getFdb().getReference().removeEventListener(divisionChangedListener);
    }

    private void initListeners() {
//        initWorkerListener();
//        initManagerListener();
//        initDivisionListener();
    }
}
