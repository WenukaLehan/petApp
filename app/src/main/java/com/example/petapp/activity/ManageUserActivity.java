package com.example.petapp.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petapp.Domain.Musers;
import com.example.petapp.R;
import com.example.petapp.adapter.ManageUserAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ManageUserActivity extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.userView);
        initdetail();
    }

    private void initdetail() {
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Musers> list = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String name = document.getString("name");
                    String email = document.getString("email");
                    String id = document.getId();
                    Musers user = new Musers();
                    user.setName(name);
                    user.setEmail(email);
                    user.setId(id);
                    list.add(user);
                }
                if(!list.isEmpty()) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(ManageUserActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(new ManageUserAdapter(list, ManageUserActivity.this));
                }

            } else {
                Toast.makeText(ManageUserActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}