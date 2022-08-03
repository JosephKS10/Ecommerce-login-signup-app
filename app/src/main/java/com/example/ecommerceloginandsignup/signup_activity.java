package com.example.ecommerceloginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class signup_activity extends AppCompatActivity {

    EditText email;
    EditText signup_password;
    EditText signup_full_name;
    EditText signup_phone_number;
    Button signup_btn;
    TextView login_activity_open;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = (EditText) findViewById(R.id.signup_email_address);
        signup_password = (EditText) findViewById(R.id.signup_password);
        signup_full_name = (EditText) findViewById(R.id.full_name);
        signup_phone_number = (EditText) findViewById(R.id.phone_number);
        signup_btn = (Button) findViewById(R.id.signup_button);
        login_activity_open = (TextView) findViewById(R.id.login_activity_open);
        progressBar = (ProgressBar) findViewById(R.id.signup_progressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        login_activity_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_activity_open.setAlpha(0);
                OpenLoginActivity();
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_address = email.getText().toString().trim();
                String password = signup_password.getText().toString().trim();
                String full_name = signup_full_name.getText().toString().trim();
                String phone = signup_phone_number.getText().toString().trim();


                if(TextUtils.isEmpty(email_address)){
                    email.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    signup_password.setError("Password is required");
                    return;
                }
                if(password.length() < 8){
                    signup_password.setError("Password must atleast contain 8 characters");
                    return;
                }
                if (TextUtils.isEmpty(full_name)){
                    signup_full_name.setError("Name is required");
                    return;
                }

                if (TextUtils.isEmpty(phone)){
                    signup_phone_number.setError("Phone number is required");
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);
                hide_keyboard(v);

                //registering the user
                firebaseAuth.createUserWithEmailAndPassword(email_address, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });


    }

    private void OpenLoginActivity() {
        Intent intent = new Intent(getApplicationContext(),login_activity.class);
        startActivity(intent);
        finish();
    }
    private void hide_keyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }

}