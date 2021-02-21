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
    private FirebaseStorage fst;

    private MyFirebase() {
        fdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        fst = FirebaseStorage.getInstance();
    }

    public static void init() {
        if (instance == null) {
            instance = new MyFirebase();
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
        return auth.getCurrentUser();
    }

    public FirebaseStorage getFst() {
        return fst;
    }
}
