package com.example.petapp.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.petapp.Helper.SessionManager;
import com.example.petapp.R;

public class AdminDashboardActivity extends AppCompatActivity {

    ConstraintLayout users,items,categories,orders,logout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        users = (ConstraintLayout) findViewById(R.id.users);
        items =(ConstraintLayout) findViewById(R.id.items);
        categories = (ConstraintLayout) findViewById(R.id.category);
        orders =(ConstraintLayout) findViewById(R.id.orders);
        logout = (ConstraintLayout) findViewById(R.id.logoutBtn);

        users.setOnClickListener(view -> {
            startActivity(new android.content.Intent(getApplicationContext(), ManageUserActivity.class));
        });
        items.setOnClickListener(view -> {
            startActivity(new android.content.Intent(getApplicationContext(), ManageItemsActivity.class));
        });
        categories.setOnClickListener(view -> {
            startActivity(new android.content.Intent(getApplicationContext(), ManageCategoriesActivity.class));
        });
        orders.setOnClickListener(view -> {
            startActivity(new android.content.Intent(getApplicationContext(), ManageOrdersActivity.class));
        });

        logout.setOnClickListener(view -> {
            SessionManager sessionManager = new SessionManager(getApplicationContext());
            sessionManager.logoutUser();
            startActivity(new android.content.Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

    }
}