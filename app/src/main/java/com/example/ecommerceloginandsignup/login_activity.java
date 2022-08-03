package com.example.ecommerceloginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_activity extends AppCompatActivity {

    EditText login_email;
    EditText login_password;
    Button login_btn;
    TextView signup_activity_open;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        ExistingUserCheck(firebaseAuth);

        login_email = (EditText) findViewById(R.id.login_email_address);
        login_password = (EditText) findViewById(R.id.login_password);
        login_btn = (Button) findViewById(R.id.login_button);
        signup_activity_open = (TextView) findViewById(R.id.signup_activity_open);
        progressBar = (ProgressBar) findViewById(R.id.login_progressBar);


        signup_activity_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_activity_open.setAlpha(0);
                OpenSignupActivity();
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_address = login_email.getText().toString().trim();
                String password = login_password.getText().toString().trim();

                if(TextUtils.isEmpty(email_address)){
                    login_email.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    login_password.setError("Password is required");
                    return;
                }
                if(password.length() < 8){
                    login_password.setError("Password must atleast contain 8 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                hide_keyboard(v);
                // authenticate the user
                firebaseAuth.signInWithEmailAndPassword(email_address,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"login Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });


    }

    private void OpenSignupActivity() {
        Intent intent = new Intent(getApplicationContext(),signup_activity.class);
        startActivity(intent);
        finish();
    }

    private void hide_keyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }

    private void ExistingUserCheck(FirebaseAuth firebaseAuth){
        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

    }
}