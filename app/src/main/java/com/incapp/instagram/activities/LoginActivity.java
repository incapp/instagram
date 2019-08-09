package com.incapp.instagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.incapp.instagram.R;
import com.incapp.instagram.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewForgotPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editText_password);
        textViewForgotPassword = findViewById(R.id.textView_forgot_password);
        buttonLogin = findViewById(R.id.button_login);
        textViewRegister = findViewById(R.id.textView_register);
        progressBar = findViewById(R.id.progressBar);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        Commented to test private repo.
//        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = editTextEmail.getText().toString();
//
//                if (email.isEmpty()) {
//                    editTextEmail.setError("Required!");
//                    editTextEmail.requestFocus();
//                } else {
//                    Utils.hideKeyboard(LoginActivity.this);
//
//                    FirebaseAuth.getInstance()
//                            .sendPasswordResetEmail(email)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Toast.makeText(LoginActivity.this,
//                                            "Check your email to reset password.", Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(LoginActivity.this,
//                                            e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                }
//            }
//        });
    }

    private void login() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (email.isEmpty()) {
            editTextEmail.setError("Required!");
            editTextEmail.requestFocus();
        } else if (!email.matches(Patterns.EMAIL_ADDRESS.pattern())) {
            editTextEmail.setError("Invalid Email!");
            editTextEmail.requestFocus();
        } else if (password.isEmpty()) {
            editTextPassword.setError("Required!");
            editTextPassword.requestFocus();
        } else if (password.length() < 6) {
            editTextPassword.setError("Password too short!");
            editTextPassword.requestFocus();
        } else {
            Utils.hideKeyboard(LoginActivity.this);

            progressBar.setVisibility(View.VISIBLE);
            buttonLogin.setVisibility(View.GONE);

            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(LoginActivity.this,
                                    "Login Success", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            buttonLogin.setVisibility(View.VISIBLE);

                            Toast.makeText(LoginActivity.this,
                                    e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
