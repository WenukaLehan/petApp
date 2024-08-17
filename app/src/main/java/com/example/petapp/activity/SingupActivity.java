package com.example.petapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.petapp.Helper.SessionManager;
import com.example.petapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SingupActivity extends AppCompatActivity {

    // Get a reference to the Firebase Database
    FirebaseFirestore db = FirebaseFirestore .getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();



    Button singupbtn;
    EditText nameEditText, emailEditText, passwordEditText, confirmPwdEditText;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_singup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        singupbtn = findViewById(R.id.createAccountButton);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPwdEditText = findViewById(R.id.confirmPwdEditText);

        sessionManager = new SessionManager(getApplicationContext());

        // If user is already logged in, redirect to MainActivity
        if (sessionManager.isLoggedIn()) {
            if(sessionManager.getUserId().equals("R8H8tNQLfxNA4sTvAEztERCg94u2")) {
                startActivity(new Intent(SingupActivity.this, AdminDashboardActivity.class));
                finish();
            }else {
                startActivity(new Intent(SingupActivity.this, HomeActivity.class));
                finish();
            }
        }

        singupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPwd = confirmPwdEditText.getText().toString();

                if (name.isEmpty()){
                    nameEditText.setError("Please enter your name");
                    nameEditText.requestFocus();
                    return;
                }
                if (email.isEmpty()){
                    emailEditText.setError("Please enter your email");
                    emailEditText.requestFocus();
                    return;
                }
                if (password.isEmpty()){
                    passwordEditText.setError("Please enter your password");
                    passwordEditText.requestFocus();
                    return;
                }
                if (confirmPwd.isEmpty()){
                    confirmPwdEditText.setError("Please confirm your password");
                    confirmPwdEditText.requestFocus();
                    return;
                }
                if (!password.equals(confirmPwd)){
                    confirmPwdEditText.setError("Passwords do not match");
                    confirmPwdEditText.requestFocus();
                    return;
                }
                if (password.length() < 6){
                    passwordEditText.setError("Password must be at least 6 characters long");
                    passwordEditText.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    if (firebaseUser != null) {
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        if (user != null) {
                                            user.sendEmailVerification()
                                                    .addOnCompleteListener(verificationTask -> {
                                                        if (verificationTask.isSuccessful()) {
                                                            Toast.makeText(SingupActivity.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                                            saveUserDataToFirestore(user.getUid(), name, email, password);
                                                        } else {
                                                            Toast.makeText(SingupActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }

                                    }
                                } else {
                                    Log.d("FirebaseAuth", "Failed to create user: " + task.getException().getMessage());
                                }
                            }
                        });

            }
        });


    }

    public void openLoginActivity(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void saveUserDataToFirestore(String userId, String name, String email , String password) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("password", password);

        // Add a new document with a generated ID
        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid ->{

                    Toast.makeText(SingupActivity.this, "User Registration successfully", Toast.LENGTH_SHORT).show();


                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                    if (firebaseUser != null) {
                        firebaseUser.reload().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (firebaseUser.isEmailVerified()) {
                                    sessionManager.createLoginSession(email, name, userId);
                                    Toast.makeText(SingupActivity.this, "Email is verified.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SingupActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SingupActivity.this, "Please verify your email first. And lohin again", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(SingupActivity.this, "Failed to reload user.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error writing document", e));
    }




}