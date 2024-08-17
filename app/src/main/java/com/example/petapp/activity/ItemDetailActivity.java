package com.example.petapp.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.petapp.Domain.Foods;
import com.example.petapp.Helper.ManagmentCart;
import com.example.petapp.databinding.ActivityItemDetailBinding;

public class ItemDetailActivity extends AppCompatActivity {

    ActivityItemDetailBinding binding;
    private Foods food;
    private ManagmentCart managmentCart;
    int numb = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();

    }

    private void setVariable() {

        managmentCart = new ManagmentCart(this);
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });

        Glide.with(this)
                .load(food.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(60))
                .into(binding.picItem);
        binding.titleTxt.setText(food.getTitle());
        binding.priceTxt.setText("RS:" + food.getPrice());
        binding.descTxt.setText(food.getDescription());
        binding.ratingTxt.setText( food.getStar() + "Rating:");
        binding.ratingBar2.setRating((float) food.getStar());
        binding.totalTxt.setText(numb * food.getPrice() + "Rs:");

        binding.plusBtn.setOnClickListener(v -> {
            numb++;
            binding.numTxt.setText(numb + "");
            binding.totalTxt.setText(numb * food.getPrice() + "Rs:");
        });
        binding.minBtn.setOnClickListener(v -> {
            if (numb > 1) {
                numb--;
                binding.numTxt.setText(numb + "");
                binding.totalTxt.setText(numb * food.getPrice() + "Rs:");
            }
        });

        binding.addToBtn.setOnClickListener(v -> {
            food.setNumberInCart(numb);
            managmentCart.insertFood(food);
        });

    }


    private void getIntentExtra() {
        food =(Foods) getIntent().getSerializableExtra("food");

    }
}