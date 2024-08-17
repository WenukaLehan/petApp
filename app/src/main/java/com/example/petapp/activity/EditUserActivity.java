package com.example.petapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.Helper.SessionManager;
import com.example.petapp.R;
import com.example.petapp.databinding.ActivityEditUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicBoolean;

public class EditUserActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    EditText emailUser, nameUser, pass, conPass;
    Button update;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    ActivityEditUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailUser = findViewById(R.id.emailUser);
        nameUser = findViewById(R.id.nameUser);
        pass = findViewById(R.id.pass);
        conPass = findViewById(R.id.conPass);
        update = findViewById(R.id.updateBtn);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        sessionManager = new SessionManager(this);
        initdetals();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
    }

    private void updateUser() {
        String email = emailUser.getText().toString();
        String name = nameUser.getText().toString();
        String password = pass.getText().toString();
        String conPassword = conPass.getText().toString();

        if (!password.isEmpty() || !conPassword.isEmpty()) {
            if(password.equals(conPassword)){
                firebaseUser.updatePassword(password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Password Updated", Toast.LENGTH_SHORT).show();
                            }
                        });
            }else{

                conPass.setError("Password does not match");
            }
        }

        if (!email.isEmpty() ) {
            if(!email.equals(sessionManager.getUserEmail())){
                firebaseUser.updateEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Email Updated", Toast.LENGTH_SHORT).show();
                                sessionManager.createLoginSession(email, name, firebaseUser.getUid());
                            }
                        });
            }

        }

        if (!name.isEmpty()) {
            if(!name.equals(sessionManager.getUserName())){
                db.collection("users").document(firebaseUser.getUid())
                        .update("name", name)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Name Updated", Toast.LENGTH_SHORT).show();
                                sessionManager.createLoginSession(email, name, firebaseUser.getUid());
                            }
                        });
            }
        }

    }

    private void initdetals() {
        emailUser.setText(sessionManager.getUserEmail());
        nameUser.setText(sessionManager.getUserName());

    }
}