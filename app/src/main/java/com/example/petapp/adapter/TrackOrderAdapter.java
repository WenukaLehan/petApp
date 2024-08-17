package com.example.petapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petapp.Domain.TrackOrder;
import com.example.petapp.R;

import java.util.ArrayList;

public class TrackOrderAdapter extends RecyclerView.Adapter<TrackOrderAdapter.ViewHolder> {
    Context context;
    ArrayList<TrackOrder> orders;

    public TrackOrderAdapter(Context context, ArrayList<TrackOrder> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public TrackOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.manage_order_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrackOrderAdapter.ViewHolder holder, int position) {
        holder.title.setText(orders.get(position).getName());
        holder.price.setText("Rs:"+orders.get(position).getPrice());
        holder.qty.setText("Qty: "+orders.get(position).getQty());
        Glide.with(context).load(orders.get(position).getImagePath()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,price,qty;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.trackOrderTitle);
            price = itemView.findViewById(R.id.trackOrderPrice);
            qty = itemView.findViewById(R.id.trackOrderQty);
            image = itemView.findViewById(R.id.trackOrderImg);
        }
    }

}
