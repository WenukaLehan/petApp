package com.example.petapp.interfaces;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.petapp.Domain.Category;
import com.example.petapp.Domain.Order;
import com.example.petapp.R;
import com.example.petapp.adapter.CategoryAdapter;
import com.example.petapp.adapter.OrderAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;


public class OrdersFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    TextView emptyO;

    public OrdersFragment() {
        // Required empty public constructor
    }


    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBarOrder);
        recyclerView = view.findViewById(R.id.orderItems);
        emptyO = view.findViewById(R.id.emptyOrder);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        db = FirebaseFirestore.getInstance();
        loadOrders();
    }

    private void loadOrders() {
        db.collection("Orders").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Order> list = new ArrayList<>();
                progressBar.setVisibility(View.VISIBLE);
                emptyO.setVisibility(View.GONE);
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String OrderId = document.getId();
                    String OrderStatus = document.getString("status");
                    String OrdrImage = document.getString("imageV");
                    String OrderDate = document.getString("date");
                    double OrderPrice = document.getDouble("total");
                    Order order = new Order();
                    order.setId(OrderId);
                    order.setStatus(OrderStatus);
                    order.setImagePath(OrdrImage);
                    order.setDate(OrderDate);
                    order.setPrice(OrderPrice);
                    list.add(order);

                }
                if (!list.isEmpty()) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(new OrderAdapter(list));
                    progressBar.setVisibility(View.GONE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    emptyO.setVisibility(View.VISIBLE);
                }


            }else{
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
    }
}