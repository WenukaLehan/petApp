package com.example.petapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petapp.Domain.Musers;
import com.example.petapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageUserAdapter extends RecyclerView.Adapter<ManageUserAdapter.ViewHolder> {
    ArrayList<Musers> users;
    Context context;

    public ManageUserAdapter(ArrayList<Musers> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ManageUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.manage_user_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ManageUserAdapter.ViewHolder holder, int position) {

        Musers user = users.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteuser(user.getId());
                users.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    private void deleteuser(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("deleted", true);
        db.collection("users").document(id).update(map)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(context, "User Deleted", Toast.LENGTH_SHORT).show();
                    }
                });

    }



    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,email;
        ConstraintLayout del;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTxtU);
            email = itemView.findViewById(R.id.emailTxtU);
            del = itemView.findViewById(R.id.deleteU);
        }
    }
}
