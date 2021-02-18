package com.example.workitapp.Activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workitapp.More.Constants;
import com.example.workitapp.Objects.MyFirebase;
import com.example.workitapp.Objects.Request;
import com.example.workitapp.R;
import com.example.workitapp.Objects.Worker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_Request extends Activity_Base {

    private TextInputLayout request_TIL_email;
    private TextInputLayout request_TIL_password;
    private TextInputLayout request_TIL_name;
    private TextInputLayout request_TIL_division;
    private Button request_BTN_send;
    private TextView request_LBL_signIn;

    private OnCompleteListener requestCompleteListener;
    private FirebaseAuth auth;
    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        findViews();
        initViews();
    }

    private void register(Worker w) {
        auth.createUserWithEmailAndPassword(w.getEmail(), w.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                w.setUid(uid);
                                clearText();

//                                ref = MyFirebase.getInstance().getFdb().getReference();
                                ref = FirebaseDatabase.getInstance().getReference();
                                ref.child(Constants.REQUESTS_PATH).child(uid).setValue(new Request(uid));
                                ref.child(Constants.WORKER_PATH).child(uid).setValue(w).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getBaseContext(), "Request has been sent.", Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(Activity_Request.this, "Request has been sent.", Toast.LENGTH_SHORT).show();
                                            Log.d("aaa", "Req - Request has been sent.");
//                                            FirebaseAuth auth = FirebaseAuth.getInstance();
                                            if (auth != null) {
                                                FirebaseUser user = auth.getCurrentUser();
                                                auth.signOut();
                                            }
                                            Intent i = new Intent(Activity_Request.this, Activity_Login.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);
                                            finish();
                                        } else
                                            Toast.makeText(Activity_Request.this, "Cannot sign user.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else
                            Toast.makeText(Activity_Request.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearText() {
        request_TIL_name.getEditText().getText().clear();
        request_TIL_password.getEditText().getText().clear();
        request_TIL_email.getEditText().getText().clear();
        request_TIL_division.getEditText().getText().clear();
    }

//    private HashMap<String, Worker> getWorkerHashMap(Worker w, String uid) {
//        HashMap<String, Worker> workerMap = new HashMap<>();
//        workerMap.put(uid, w);
//        return workerMap;
//    }

    private void initViews() {
        auth = FirebaseAuth.getInstance();

        request_BTN_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerBtn();
            }
        });
        request_LBL_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_Request.this, Activity_Login.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void registerBtn() {
        String name = request_TIL_name.getEditText().getText().toString();
        String password = request_TIL_password.getEditText().getText().toString();
        String email = request_TIL_email.getEditText().getText().toString();
        String divisionText = request_TIL_division.getEditText().getText().toString();
        int division = 0;
        try {
            division = Integer.parseInt(divisionText);
        } catch (NumberFormatException e) {
            Toast.makeText(getBaseContext(), "Division ID must be a number !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (validCreds(name, password, email, divisionText)) {
            password = sha256(password);
            Worker w = new Worker(name, email, password, division);
            register(w);
        } else {
            Toast.makeText(Activity_Request.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validCreds(String name, String password, String email, String divisionText) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email) || TextUtils.isEmpty(divisionText))
            return false;
        return true;
    }

    private void findViews() {
        request_TIL_division = findViewById(R.id.request_TIL_division);
        request_TIL_name = findViewById(R.id.request_TIL_name);
        request_TIL_password = findViewById(R.id.request_TIL_password);
        request_TIL_email = findViewById(R.id.request_TIL_email);
        request_BTN_send = findViewById(R.id.request_BTN_send);
        request_LBL_signIn = findViewById(R.id.request_LBL_signIn);
    }
}