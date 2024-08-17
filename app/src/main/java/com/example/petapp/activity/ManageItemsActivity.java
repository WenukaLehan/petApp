package com.example.petapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.R;
import com.example.petapp.databinding.ActivityManageItemsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ManageItemsActivity extends AppCompatActivity {

    Spinner category, categoryType;
    ImageView itemImg;
    EditText itemName, itemDescription, itemPrice, itemStar;
    Button addBtn,imgBtn;
    FirebaseFirestore db;
    ArrayList<String> itemIds = new ArrayList<>();
    ArrayList<String> itemList = new ArrayList<>();
    String catId,itemNameS,itemDescriptionS,imgUrl;
    float itemPriceS,itemStarS;
    private Uri imageUri;
    private StorageReference storageReference;
    ProgressBar progressBar;

    ActivityManageItemsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = findViewById(R.id.progressBarMuser);
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        categoryType = findViewById(R.id.catogaryType);
        category = findViewById(R.id.catogaries);
        itemImg = findViewById(R.id.Imgcat);
        itemName = findViewById(R.id.catName);
        itemDescription = findViewById(R.id.productDescription);
        itemPrice = findViewById(R.id.productPrice);
        itemStar = findViewById(R.id.starRate);
        addBtn = findViewById(R.id.BtnAdd);
        imgBtn = findViewById(R.id.seectImgBtn);

        loadTypes();
        categoryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                LoadCatogaries(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                catId = itemIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imgBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 100);
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImg(imageUri);

            }
        });

    }

    private void addItem() {

        itemNameS = itemName.getText().toString();
        itemDescriptionS = itemDescription.getText().toString();
        itemPriceS = Float.parseFloat(itemPrice.getText().toString());
        itemStarS = Float.parseFloat(itemStar.getText().toString());

        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("name", itemNameS);
        itemMap.put("disc", itemDescriptionS);
        itemMap.put("price", itemPriceS);
        itemMap.put("star", itemStarS);
        itemMap.put("image", imgUrl);
        itemMap.put("category_id", catId);
        db.collection("food_items")
                .add(itemMap)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ManageItemsActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(ManageItemsActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            itemImg.setImageURI(imageUri);

        }
    }

    private void uploadImg(Uri imageUri) {

        if (imageUri != null) {
            progressBar.setVisibility(View.VISIBLE);
            String uniqueID = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("Products/" + uniqueID);

            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imgUrl = uri.toString();
                                    progressBar.setVisibility(View.GONE);
                                    addItem();
                                }
                            });
                        }
                    });
        }
    }

    private void LoadCatogaries(String selectedItem) {

        db.collection(selectedItem)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String itemName = document.getString("name");
                            String itemId = document.getId();

                            itemList.add(itemName);
                            itemIds.add(itemId);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        category.setAdapter(adapter);
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });

    }

    private void loadTypes() {
        String[] items = {"food_categories", "nutrition_categories"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryType.setAdapter(adapter);
    }
}