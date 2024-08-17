package com.example.petapp.adapter;

import android.content.Context;
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
import com.example.petapp.Domain.Order;
import com.example.petapp.R;
import com.example.petapp.activity.ManageOrdersActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ManageOrderAdapter extends RecyclerView.Adapter<ManageOrderAdapter.MyViewHolder> {
    Context context;
    ArrayList<Order> orderList;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    ManageOrdersActivity activity;

    public ManageOrderAdapter(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ManageOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ManageOrderAdapter.MyViewHolder holder, int position) {

        holder.orderStat.setText(orderList.get(position).getStatus());
        holder.totPrice.setText("Rs:"+orderList.get(position).getPrice());
        holder.date.setText(orderList.get(position).getDate());
        Glide.with(context)
                .load(orderList.get(position).getImagePath())
                .centerCrop()
                .into(holder.image);
        if(orderList.get(position).getStatus().equals("Delivered")){
            holder.track.setVisibility(View.GONE);
            holder.orderStat.setTextColor(context.getResources().getColor(R.color.green));

        } else if ( orderList.get(position).getStatus().equals("On the way")) {
            holder.track.setVisibility(View.VISIBLE);
            holder.statTxt.setText("Cancel ");
            holder.track.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
            holder.orderStat.setTextColor(context.getResources().getColor(R.color.blue));

        } else if ( orderList.get(position).getStatus().equals("Order Placed")) {
            holder.track.setVisibility(View.VISIBLE);
            holder.statTxt.setText("Deliver");
            holder.track.setBackgroundTintList(context.getResources().getColorStateList(R.color.black));
            holder.orderStat.setTextColor(context.getResources().getColor(R.color.black));

        } else{
            holder.track.setVisibility(View.GONE);
            holder.orderStat.setTextColor(context.getResources().getColor(R.color.red));
        }
        holder.track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderList.get(position).getStatus().equals("On the way")){
                    cancleOrder(orderList.get(position).getId());

                }
                if(orderList.get(position).getStatus().equals("Order Placed")){
                    deliverOrder(orderList.get(position).getId());

                }

        }});

    }

    private void deliverOrder(String id) {
        db.collection("Orders").document(id).update("status","On the way").addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(context, "Order delivering", Toast.LENGTH_SHORT).show();
                activity.initOrders();
            }
        });
    }

    private void cancleOrder(String id) {
        db.collection("Orders").document(id).update("status","Cancelled").addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(context, "Order cancelled", Toast.LENGTH_SHORT).show();
                activity.initOrders();
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderStat, totPrice, date,statTxt;
        ImageView image;
        ConstraintLayout track;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            orderStat = itemView.findViewById(R.id.orderStat);
            totPrice = itemView.findViewById(R.id.totPrice);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.OrderImage);
            track = itemView.findViewById(R.id.track);
            statTxt = itemView.findViewById(R.id.statTxt);

        }
    }

}
