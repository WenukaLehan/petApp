package com.example.petapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.Domain.Address;
import com.example.petapp.Helper.SessionManager;
import com.example.petapp.R;
import com.example.petapp.databinding.ActivityAddAddressBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private FirebaseFirestore db;
    EditText address,city,state,country,postalCode,phone;
    Button saveBtn;
    int pos;
    ImageView backBtn;
    boolean isUpdate = false;
    Map<String, Object> addressMaptemp;

    ActivityAddAddressBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        address = findViewById(R.id.emailUser);
        city = findViewById(R.id.nameUser);
        state = findViewById(R.id.pass);
        country = findViewById(R.id.conPass);
        postalCode = findViewById(R.id.postCTxt);
        phone = findViewById(R.id.phoneTxt);
        saveBtn = findViewById(R.id.updateBtn);
        sessionManager = new SessionManager(this);
        db = FirebaseFirestore .getInstance();
        saveBtn.setText("Add Address");

        addressMaptemp = new HashMap<>();
        backBtn = (ImageView) findViewById(R.id.backBtn);


        getIntetExtra();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isUpdate){
                    saveAddress();
                }
                else{
                    updateAddress();
                }
            }
        });

    }

    private void updateAddress() {

        String addressTxt = address.getText().toString();
        String cityTxt = city.getText().toString();
        String stateTxt = state.getText().toString();
        String countryTxt = country.getText().toString();
        String postalCodeTxt = postalCode.getText().toString();
        String phoneTxt = phone.getText().toString();
        String userId = sessionManager.getUserId();
        Map<String, Object> addressMap = new HashMap<>();
        addressMap.put("address", addressTxt);
        addressMap.put("city", cityTxt);
        addressMap.put("state", stateTxt);
        addressMap.put("country", countryTxt);
        addressMap.put("postalCode", postalCodeTxt);
        addressMap.put("phone", phoneTxt);
        DocumentReference addressRef = db.collection("users").document(userId);
        addressRef.update("address", FieldValue.arrayRemove(addressMaptemp));
        addressRef.update("address", FieldValue.arrayUnion(addressMap))
                .addOnSuccessListener(unused -> {
                    Toast.makeText(AddAddressActivity.this, "Address Updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(AddAddressActivity.this, "Failed to update address", Toast.LENGTH_SHORT).show());

    }

    private void getIntetExtra() {
        Address addressI = (Address) getIntent().getSerializableExtra("address");
        pos = getIntent().getIntExtra("pos",0);
        if(addressI != null){
            address.setText(addressI.getAddress());
            city.setText(addressI.getCity());
            state.setText(addressI.getState());
            country.setText(addressI.getCountry());
            postalCode.setText(addressI.getPostalCode());
            phone.setText(addressI.getPhone());
            saveBtn.setText("Update Address");
            isUpdate = true;
            addressMaptemp.put("address", addressI.getAddress());
            addressMaptemp.put("city", addressI.getCity());
            addressMaptemp.put("state", addressI.getState());
            addressMaptemp.put("country", addressI.getCountry());
            addressMaptemp.put("postalCode", addressI.getPostalCode());
            addressMaptemp.put("phone", addressI.getPhone());

        }
    }

    private void saveAddress() {

        String addressTxt = address.getText().toString();
        String cityTxt = city.getText().toString();
        String stateTxt = state.getText().toString();
        String countryTxt = country.getText().toString();
        String postalCodeTxt = postalCode.getText().toString();
        String phoneTxt = phone.getText().toString();
        String userId = sessionManager.getUserId();
        Map<String, Object> addressMap = new HashMap<>();
        addressMap.put("address", addressTxt);
        addressMap.put("city", cityTxt);
        addressMap.put("state", stateTxt);
        addressMap.put("country", countryTxt);
        addressMap.put("postalCode", postalCodeTxt);
        addressMap.put("phone", phoneTxt);

        DocumentReference addressRef = db.collection("users").document(userId);
        addressRef.update("address", FieldValue.arrayUnion(addressMap))
                .addOnSuccessListener(aVoid -> {

                    Toast.makeText(this, "Address added", Toast.LENGTH_SHORT).show();
                    address.setText("");
                    city.setText("");
                    state.setText("");
                    country.setText("");
                    postalCode.setText("");
                    phone.setText("");
                    finish();

                }).addOnFailureListener(e -> {
                    Log.d("TAG", "onFailure: ", e);
                });
    }
}