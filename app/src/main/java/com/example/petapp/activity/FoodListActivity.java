package com.example.petapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petapp.Domain.Foods;
import com.example.petapp.R;
import com.example.petapp.adapter.FoodListAdapter;
import com.example.petapp.databinding.ActivityFoodListBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FoodListActivity extends AppCompatActivity {

    ActivityFoodListBinding binding;
    private RecyclerView.Adapter adapter;
    private String categoryId;
    private String categoryTitle;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = FirebaseFirestore .getInstance();

        getIntentExtra();
        initList();
    }

    private void initList() {
        binding.progressBarL.setVisibility(View.VISIBLE);
        ArrayList<Foods> list = new ArrayList<>();

        db.collection("food_items").whereEqualTo("category_id", categoryId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String itemName = document.getString("name");
                            String itemImage = document.getString("image");
                            double itemPrice = document.getDouble("price");
                            String itemDescription = document.getString("disc");
                            double itemStar = document.getDouble("star");
                            String itemID = document.getId();

                            Foods foods = new Foods();

                            foods.setTitle(itemName);
                            foods.setImagePath(itemImage);
                            foods.setPrice(itemPrice);
                            foods.setDescription(itemDescription);
                            foods.setStar(itemStar);
                            foods.setId(itemID);
                            foods.setCategory(categoryTitle);

                            list.add(foods);

                        }
                        if (!list.isEmpty()) {

                            binding.foodListView.setLayoutManager(new GridLayoutManager(this, 2));
                            binding.foodListView.setAdapter(new FoodListAdapter(list));
                        }
                        binding.progressBarL.setVisibility(View.GONE);
                    } else {
                        binding.progressBarL.setVisibility(View.GONE);
                        Toast.makeText(this, categoryId, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getIntentExtra() {
        categoryId = getIntent().getStringExtra("categoryId");
        categoryTitle = getIntent().getStringExtra("categoryTitle");
        binding.catTitleTxt.setText(categoryTitle);

        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}