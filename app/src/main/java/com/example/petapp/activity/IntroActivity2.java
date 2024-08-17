package com.example.petapp.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.petapp.R;

public class IntroActivity2 extends AppCompatActivity {

    Button stbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        stbtn = findViewById(R.id.getStartedButton);
        stbtn.setOnClickListener(v -> {

            if(isConnected()){
                // Intent to navigate to the next activity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Intent to navigate to the next activity
                        Intent intent = new Intent(IntroActivity2.this, SingupActivity.class);
                        startActivity(intent);
                        finish();  // Optional: finish this activity so user cannot go back to it
                    }
                }, 500);  // Optional: finish this activity so user cannot go back to it

            }else{
                Toast.makeText(this, "Please connect to the internet", Toast.LENGTH_SHORT).show();

            }

        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}