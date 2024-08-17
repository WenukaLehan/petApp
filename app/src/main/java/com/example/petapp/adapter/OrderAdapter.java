package com.example.petapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.petapp.Domain.Order;
import com.example.petapp.R;
import com.example.petapp.activity.OrderTrackActivity;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    ArrayList<Order> orders;
    Context context;

    public OrderAdapter(ArrayList<Order> list) {
        this.orders = list;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate( R.layout.order_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {

        Order order = orders.get(position);
        holder.date.setText(order.getDate());
        holder.status.setText(order.getStatus());
        holder.price.setText("RS:" + order.getPrice());
        Glide.with(context)
                .load(order.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(60))
                .into(holder.image);

        if (order.getStatus().equals("Delivered")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
            holder.layout.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
            holder.lg.setOutlineSpotShadowColor(context.getResources().getColor(R.color.green));

        } else if (order.getStatus().equals("Cancelled")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
            holder.layout.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
            holder.lg.setOutlineSpotShadowColor(context.getResources().getColor(R.color.red));

        } else if (order.getStatus().equals("On the way")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.blue));
            holder.layout.setBackgroundTintList(context.getResources().getColorStateList(R.color.blue));
            holder.lg.setOutlineSpotShadowColor(context.getResources().getColor(R.color.blue));

        }else{
            holder.status.setTextColor(context.getResources().getColor(R.color.black));
            holder.layout.setBackgroundTintList(context.getResources().getColorStateList(R.color.black));
            holder.lg.setOutlineSpotShadowColor(context.getResources().getColor(R.color.black));

        }
        holder.itemView.setOnClickListener(v -> {
            if(order.getStatus().equals("Delivered")){
                Toast.makeText(context, "This order Allready Delivered", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, OrderTrackActivity.class);
                intent.putExtra("order",  orders.get(position));
                context.startActivity(intent);

            }else if(order.getStatus().equals("Cancelled")){
                Toast.makeText(context, "This order Allready Cancelled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, OrderTrackActivity.class);
                intent.putExtra("order",  orders.get(position));
                context.startActivity(intent);

            }else{
                Intent intent = new Intent(context, OrderTrackActivity.class);
                intent.putExtra("order",  orders.get(position));
                context.startActivity(intent);
            }

        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView status,price,date;
        ImageView image;
        ConstraintLayout layout,lg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.orderStat);
            price = itemView.findViewById(R.id.totPrice);
            image = itemView.findViewById(R.id.OrderImage);
            date = itemView.findViewById(R.id.date);
            layout = itemView.findViewById(R.id.track);
            lg = itemView.findViewById(R.id.lg);
        }
    }
}
