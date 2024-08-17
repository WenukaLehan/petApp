package com.example.petapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.petapp.R;
import com.example.petapp.databinding.ActivityManageCategoriesBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class ManageCategoriesActivity extends AppCompatActivity {

    ActivityManageCategoriesBinding binding;
    EditText catName;
    Spinner catType;
    ImageView catImg;
    Button addBtn, imgBtn;
    FirebaseFirestore db;
    private Uri imageUri;
    private StorageReference storageReference;
    ProgressBar progressBar;
    String catNameS, catTypeS, imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = findViewById(R.id.progressBarMcat);
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        catType = findViewById(R.id.catogaryType);
        catName = findViewById(R.id.catName);
        catImg = findViewById(R.id.Imgcat);
        addBtn = findViewById(R.id.BtnAdd);
        imgBtn = findViewById(R.id.seectImgBtn);

        loadType();
        catType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catTypeS = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        imgBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 10);
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImg(imageUri);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            catImg.setImageURI(imageUri);

        }
    }

    private void loadType() {

        String[] items = {"food_categories", "nutrition_categories"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catType.setAdapter(adapter);

    }

    private void uploadImg(Uri imageUri) {

        if (imageUri != null) {
            progressBar.setVisibility(View.VISIBLE);
            String uniqueID = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("Catogeries/" + uniqueID);

            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imgUrl = uri.toString();
                                    progressBar.setVisibility(View.GONE);
                                    addCat();
                                }
                            });
                        }
                    });
        }
    }

    private void addCat() {

        catNameS = catName.getText().toString();
        db.collection(catTypeS)
                .add(new HashMap<String, Object>() {{
                    put("name", catNameS);
                    put("img", imgUrl);
                }})
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Catogery Added", Toast.LENGTH_SHORT).show();
                    finish();
                });

    }

}















