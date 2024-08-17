package com.example.petapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.petapp.Domain.Foods;
import com.example.petapp.R;
import com.example.petapp.activity.ItemDetailActivity;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    ArrayList<Foods> foods;
    Context context;

    public FoodListAdapter(ArrayList<Foods> foods) {
        this.foods = foods;
    }

    @NonNull
    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate( R.layout.viewhoalder_food_list_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.ViewHolder holder, int position) {

        holder.title.setText(foods.get(position).getTitle());
        holder.price.setText("RS:" + foods.get(position).getPrice());
        holder.rate.setText("" + foods.get(position).getStar());
        Glide.with(context)
                .load(foods.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(60))
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("food", foods.get(position));
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, price, rate;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.itemTitel);
            price = itemView.findViewById(R.id.itemPrice);
            rate = itemView.findViewById(R.id.rateNum);
            image = itemView.findViewById(R.id.itemPic);

        }
    }
}
