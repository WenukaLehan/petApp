package com.example.petapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.petapp.Domain.Foods;
import com.example.petapp.Helper.ChangeNumberItemsListener;
import com.example.petapp.Helper.ManagmentCart;
import com.example.petapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewHolder> {
    ArrayList<Foods> cartList;
    private ManagmentCart managmentCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    public CartAdapter(ArrayList<Foods> cartList, ManagmentCart managmentCart, ChangeNumberItemsListener changeNumberItemsListener) {
        this.cartList = cartList;
        this.managmentCart = managmentCart;
        this.changeNumberItemsListener = changeNumberItemsListener;
    }


    @NonNull
    @Override
    public CartAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        DecimalFormat df = new DecimalFormat("#.##");
        holder.titleName.setText(cartList.get(position).getTitle());
        holder.itPrice.setText("Rs:" + df.format(cartList.get(position).getPrice()*cartList.get(position).getNumberInCart()));
        holder.num.setText(cartList.get(position).getNumberInCart() + "");
        Glide.with(holder.itemView.getContext())
                .load(cartList.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.picOfItem);

        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart.plusNumberItem(cartList, position, new ChangeNumberItemsListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart.minusNumberItem(cartList, position, new ChangeNumberItemsListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });
        holder.remBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart.removeItem(cartList, position, new ChangeNumberItemsListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView picOfItem;
        TextView titleName,plusBtn,minusBtn,itPrice;
        TextView num;
        ConstraintLayout remBtn;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            picOfItem = itemView.findViewById(R.id.OrderImage);
            titleName = itemView.findViewById(R.id.orderStat);
            plusBtn = itemView.findViewById(R.id.addBtn);
            minusBtn = itemView.findViewById(R.id.remBtn);
            num = itemView.findViewById(R.id.itemCount);
            itPrice = itemView.findViewById(R.id.totPrice);
            remBtn = itemView.findViewById(R.id.track);

        }
    }
}
