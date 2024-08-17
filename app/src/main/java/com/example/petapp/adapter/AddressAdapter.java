package com.example.petapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petapp.Domain.Address;
import com.example.petapp.R;
import com.example.petapp.activity.AddAddressActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    ArrayList<Address> addresses;
    Context context;

    public AddressAdapter(ArrayList<Address> addresses, Context context) {
        this.addresses = addresses;
        this.context = context;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_address_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        holder.address.setText(addresses.get(position).getAddress());
        holder.city.setText(addresses.get(position).getCity());
        holder.state.setText(addresses.get(position).getState());
        holder.country.setText(addresses.get(position).getCountry());
        holder.postalCode.setText(addresses.get(position).getPostalCode());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddAddressActivity.class);
                intent.putExtra("address", addresses.get(position));
                intent.putExtra("pos", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView address,city,state,country,postalCode;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            city = itemView.findViewById(R.id.city);
            state = itemView.findViewById(R.id.state);
            country = itemView.findViewById(R.id.country);
            postalCode = itemView.findViewById(R.id.postCode);

        }

    }


}
