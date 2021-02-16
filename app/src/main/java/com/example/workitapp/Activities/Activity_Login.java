package com.example.workitapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.workitapp.More.Constants;
import com.example.workitapp.Objects.MyFirebase;
import com.example.workitapp.Objects.MySPV;
import com.example.workitapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Activity_Login extends Activity_Base {

    private TextView login_LBL_request;
    private Button login_BTN_sign;
    private ImageView login_IMG_icon;
    private TextInputLayout login_TIL_email;
    private TextInputLayout login_TIL_password;
    private CheckBox login_CBX_remember;

    private FirebaseAuth auth;
    private DatabaseReference ref;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rememberedCheck();

        findViews();
        initViews();
    }

    private void rememberedCheck() {
        if(MySPV.getInstance().getBool(Constants.REMEMBER)) {
            Intent i = new Intent(Activity_Login.this, Activity_Main.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d("aaa", "Log - rememberd.");
            startActivity(i);
            finish();
        }
    }

    private void login() {
        String email = login_TIL_email.getEditText().getText().toString();
        String password = sha256(login_TIL_password.getEditText().getText().toString());
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
            Toast.makeText(Activity_Login.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                successfulLogin();
                            } else {
                                Toast.makeText(Activity_Login.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void successfulLogin() {
        saveRememberStatus();
        clearText();
        Toast.makeText(Activity_Login.this, "Login Successfully.", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Activity_Login.this, Activity_Main.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("aaa", "Log - logged in.");
        startActivity(i);
        finish();
    }

    private void saveRememberStatus() {
        if(login_CBX_remember.isChecked()){
            MySPV.getInstance().putBool(Constants.REMEMBER, true);
            MySPV.getInstance().putString(Constants.UID, FirebaseAuth.getInstance().getCurrentUser().getUid());
            Log.d("aaa", "log - "+FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }

    private void clearText() {
        login_TIL_email.getEditText().getText().clear();
        login_TIL_password.getEditText().getText().clear();
    }

    private void initViews() {
        auth = FirebaseAuth.getInstance();

        login_LBL_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getBaseContext(), "Request has been sent", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Activity_Login.this, Activity_Request.class);
                startActivity(i);
                finish();
            }
        });

        login_BTN_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void findViews() {
        login_LBL_request = findViewById(R.id.login_LBL_request);
        login_BTN_sign = findViewById(R.id.login_BTN_sign);
        login_TIL_email = findViewById(R.id.login_TIL_email);
        login_TIL_password = findViewById(R.id.login_TIL_password);
        login_CBX_remember = findViewById(R.id.login_CBX_remember);
        login_IMG_icon = findViewById(R.id.login_IMG_icon);
    }


    public void requestToast(View view) {
        Toast.makeText(getBaseContext(), "Request has been sent", Toast.LENGTH_SHORT).show();
    }
}