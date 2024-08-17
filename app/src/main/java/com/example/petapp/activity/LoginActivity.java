package com.example.petapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    // Get a reference to the Firebase Database
    FirebaseFirestore db = FirebaseFirestore .getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Button loginbtn;
    EditText emailEditText, passwordEditText;
    TextView frogot;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        frogot = findViewById(R.id.frogotPass);
        loginbtn = findViewById(R.id.loginBtn);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        sessionManager = new SessionManager(getApplicationContext());

        // If user is already logged in, redirect to MainActivity
        if (sessionManager.isLoggedIn()) {
            if(sessionManager.getUserId().equals("R8H8tNQLfxNA4sTvAEztERCg94u2")) {
                startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                finish();
            }else {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
        }

        frogot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();

                if (email.isEmpty()) {
                    emailEditText.setError("Please enter your email");
                    emailEditText.requestFocus();
                    return;
                }
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Check your email to reset your password!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty()) {
                    emailEditText.setError("Please enter your email");
                    emailEditText.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    passwordEditText.setError("Please enter your password");
                    passwordEditText.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                    if (firebaseUser != null) {
                                        firebaseUser.reload().addOnCompleteListener(taskd -> {
                                            if (taskd.isSuccessful()) {
                                                if (firebaseUser.isEmailVerified()) {
                                                    getUserData(firebaseUser.getUid());
                                                } else {
                                                    Toast.makeText(LoginActivity.this, "Please verify your email first.", Toast.LENGTH_SHORT).show();
                                                    firebaseUser.sendEmailVerification();
                                                }
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Failed to reload user.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                } else {
                                    Toast.makeText(LoginActivity.this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void openSingupActivity(View v) {
        Intent intent = new Intent(this, SingupActivity.class);
        startActivity(intent);
    }

    private void getUserData(String userId) {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String namee = document.getString("name");
                            String emaile = document.getString("email");
                            boolean isdeleted = Boolean.TRUE.equals(document.getBoolean("deleted"));

                            if (isdeleted) {
                                db.collection("users").document(userId).delete();
                                FirebaseAuth.getInstance().getCurrentUser().delete();
                                Toast.makeText(LoginActivity.this, "User deleted", Toast.LENGTH_SHORT).show();
                                return;
                            }else{
                                sessionManager.createLoginSession(emaile, namee, userId);
                                Toast.makeText(LoginActivity.this, "login success ", Toast.LENGTH_SHORT).show();
                                if(userId.equals("R8H8tNQLfxNA4sTvAEztERCg94u2")) {
                                    startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                                    finish();
                                }else {
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                }
                            }

                        } else {
                            Toast.makeText(LoginActivity.this, "No such user", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to get user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}