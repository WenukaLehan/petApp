package com.example.petapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petapp.Domain.Address;
import com.example.petapp.Helper.SessionManager;
import com.example.petapp.R;
import com.example.petapp.adapter.AddressAdapter;
import com.example.petapp.databinding.ActivityAddressBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    RecyclerView recyclerView;
    ConstraintLayout constraintLayout;
    ActivityAddressBinding binding;
    private SessionManager sessionManager;
    TextView emtyAddress;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = findViewById(R.id.progressBarAddress);
        emtyAddress = findViewById(R.id.emptyTxt);
        sessionManager = new SessionManager(this);
        db = FirebaseFirestore .getInstance();
        constraintLayout = findViewById(R.id.addAddress);
        recyclerView = findViewById(R.id.addressesList);
        initAddress();

        binding.addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initAddress() {
        ArrayList<Address> list = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        DocumentReference docRef = db.collection("users").document(sessionManager.getUserId());
                docRef.get().addOnCompleteListener(task -> {
                            List<Map<String, Object>> Addres = new ArrayList<>();
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            ArrayList<Map> AddresMap = (ArrayList<Map>) document.getData().get("address");
                            if(AddresMap != null){
                                for(int i = 0; i < AddresMap.size(); i++){
                                    Map<String, Object> entry = AddresMap.get(i);
                                    Addres.add(entry);
                                }
                            }
                        }else{
                            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                            if (Addres != null) {
                                for (int i = 0; i < Addres.size(); i++) {
                                    Address address = new Address();
                                    address.setAddress(Addres.get(i).get("address").toString());
                                    address.setCity(Addres.get(i).get("city").toString());
                                    address.setCountry(Addres.get(i).get("country").toString());
                                    address.setState(Addres.get(i).get("state").toString());
                                    address.setPostalCode(Addres.get(i).get("postalCode").toString());
                                    address.setPhone(Addres.get(i).get("phone").toString());

                                    list.add(address);
                                }
                            }
                            if(!list.isEmpty()){
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.VERTICAL, false));
                        AddressAdapter addressAdapter = new AddressAdapter(list, binding.getRoot().getContext());
                        recyclerView.setAdapter(addressAdapter);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
                        emtyAddress.setVisibility(View.VISIBLE);

                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show());
    }
}