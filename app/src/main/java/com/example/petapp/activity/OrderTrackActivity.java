package com.example.petapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.example.petapp.Domain.Address;
import com.example.petapp.Domain.Order;
import com.example.petapp.Domain.TrackOrder;
import com.example.petapp.R;
import com.example.petapp.adapter.TrackOrderAdapter;
import com.example.petapp.databinding.ActivityOrderTrackBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderTrackActivity extends AppCompatActivity {

    ActivityOrderTrackBinding binding;
    ImageView trackImg;
    TextView  totPrice, date;
    RecyclerView recyclerView;
    Button confirmBtn;
    private Order order;
    ProgressBar trackProgress;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderTrackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        trackProgress = findViewById(R.id.progressBarTrackImg);
        trackImg = findViewById(R.id.trackImg);
        totPrice = findViewById(R.id.priceTxtTot);
        date = findViewById(R.id.dateOrder);
        recyclerView = findViewById(R.id.orderDataView);
        confirmBtn = findViewById(R.id.coBtn);

        getExtraIntent();
        setData();
        loadOrders();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmBtn.getText().toString().equals("Confirm Order Received")){
                    deliverOrder(order.getId());
                } else {
                    cancleOrder(order.getId());
                }
            }
        });

    }

    private void loadOrders() {
        ArrayList<TrackOrder> list = new ArrayList<>();
        db.collection("Orders").document(order.getId()).get().addOnCompleteListener(task -> {
            List<Map<String, Object>> Orders = new ArrayList<>();
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    ArrayList<Map> OrdersMap = (ArrayList<Map>) document.getData().get("orderItems");
                    if(OrdersMap != null){
                        for(int i = 0; i < OrdersMap.size(); i++){
                            Map<String, Object> entry = OrdersMap.get(i);
                            Orders.add(entry);
                        }
                    }
                }
            }

            if (Orders != null) {
                for(int i = 0; i < Orders.size(); i++){
                    TrackOrder trackOrder = new TrackOrder();
                    trackOrder.setImagePath(Orders.get(i).get("image").toString());
                    trackOrder.setPrice(Orders.get(i).get("price").toString());
                    trackOrder.setQty(Orders.get(i).get("quantity").toString());
                    trackOrder.setName(Orders.get(i).get("item").toString());
                    list.add(trackOrder);
                }
            }
            if(list != null){
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(new TrackOrderAdapter(this, list));
            }
        });

    }

    private void deliverOrder(String id) {
        db.collection("Orders").document(id).update("status","Delivered").addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(this, "Status updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void cancleOrder(String id) {
        db.collection("Orders").document(id).update("status","Cancelled").addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(this, "Order cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void setData() {

        totPrice.setText("Rs: "+order.getPrice());
        date.setText(order.getDate().toString());

        if(order.getStatus().equals("On the way")){
            confirmBtn.setVisibility(View.VISIBLE);
            confirmBtn.setText("Confirm Order Received");

            trackProgress.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    trackProgress.setVisibility(View.GONE);
                    trackImg.setImageResource(R.drawable.on_the_way);
                }
            }, 1000);


        } else if ( order.getStatus().equals("Order Placed")) {
            confirmBtn.setVisibility(View.VISIBLE);
            confirmBtn.setText("Cancle Order");
            trackProgress.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    trackProgress.setVisibility(View.GONE);
                    trackImg.setImageResource(R.drawable.order_placed);
                }
            }, 1000);

        } else if(order.getStatus().equals("Cancelled")) {
            confirmBtn.setVisibility(View.GONE);

            trackProgress.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    trackProgress.setVisibility(View.GONE);
                    trackImg.setImageResource(R.drawable.order_cancle);
                }
            }, 1000);


        }else{
            confirmBtn.setVisibility(View.GONE);

            trackProgress.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    trackProgress.setVisibility(View.GONE);
                    trackImg.setImageResource(R.drawable.order_deliver);
                }
            }, 1000);
        }


    }

    private void getExtraIntent() {
        order = (Order) getIntent().getSerializableExtra("order");
    }
}