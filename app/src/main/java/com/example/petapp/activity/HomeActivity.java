package com.example.petapp.activity;


import android.annotation.SuppressLint;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.example.petapp.R;
import com.example.petapp.interfaces.CartFragment;
import com.example.petapp.interfaces.HomeFragment;
import com.example.petapp.interfaces.OrdersFragment;
import com.example.petapp.interfaces.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    Fragment selectedFragment = null;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentManager = getSupportFragmentManager();
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // Handle home click
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_bag:
                    // Handle bag click
                    selectedFragment = new CartFragment();
                    break;
                case R.id.navigation_orders:
                    // Handle orders click
                    selectedFragment = new OrdersFragment();
                    break;
                case R.id.navigation_profile:
                    // Handle profile click
                    selectedFragment = new UserFragment();
                    break;

            }
            if (selectedFragment != null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;

        });

        // Load default fragment if needed
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home); // Set default item
        }

    }
}