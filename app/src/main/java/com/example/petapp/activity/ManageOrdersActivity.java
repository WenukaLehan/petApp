package com.example.petapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petapp.Domain.Order;
import com.example.petapp.R;
import com.example.petapp.adapter.ManageOrderAdapter;
import com.example.petapp.adapter.OrderAdapter;
import com.example.petapp.databinding.ActivityManageOrdersBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ManageOrdersActivity extends AppCompatActivity {

    RecyclerView rv;
    FirebaseFirestore db;
    ActivityManageOrdersBinding binding;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBarOrd);
        rv = findViewById(R.id.orderView);
        initOrders();

    }

    public void initOrders() {
        db.collection("Orders").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Order> list = new ArrayList<>();
                progressBar.setVisibility(View.VISIBLE);
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String OrderId = document.getId();
                    String OrderStatus = document.getString("status");
                    String OrdrImage = document.getString("imageV");
                    String OrderDate = document.getString("date");
                    double OrderPrice = document.getDouble("total");
                    Order order = new Order();
                    order.setId(OrderId);
                    order.setStatus(OrderStatus);
                    order.setImagePath(OrdrImage);
                    order.setDate(OrderDate);
                    order.setPrice(OrderPrice);
                    list.add(order);

                }
                if (!list.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    rv.setAdapter(new ManageOrderAdapter(list));

                }else{
                    progressBar.setVisibility(View.GONE);

                }


            }else{
                Log.d("Wenuka", "Error getting documents: ", task.getException());
            }
        });
    }
}