package com.example.workitapp.Objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.workitapp.More.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;

public class MyFirebase {
    private static MyFirebase instance;
    private FirebaseAuth auth;
    private FirebaseDatabase fdb;
    private FirebaseUser user;
    private FirebaseStorage fst;
    private ArrayList<Worker> workers;
    private Worker worker;


    private MyFirebase(Context context) {
        fdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        fst = FirebaseStorage.getInstance();
        if (auth != null)
            user = auth.getCurrentUser();
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new MyFirebase(context.getApplicationContext());
        }
    }

    public static MyFirebase getInstance() {
        return instance;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseDatabase getFdb() {
        return fdb;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public FirebaseStorage getFst() {
        return fst;
    }
}
