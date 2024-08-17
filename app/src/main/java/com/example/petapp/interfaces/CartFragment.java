package com.example.petapp.interfaces;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.petapp.Domain.Foods;
import com.example.petapp.Helper.ChangeNumberItemsListener;
import com.example.petapp.Helper.ManagmentCart;
import com.example.petapp.Helper.SessionManager;
import com.example.petapp.R;
import com.example.petapp.adapter.CartAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;


public class CartFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ManagmentCart managmentCart;
    TextView tot,sum,del,emptyTxt;
    RecyclerView recyclerView;
    ImageView back;
    ScrollView scrollView;
    Button orderBtn;
    private SessionManager sessionManager;
    private FirebaseFirestore db;
    float total;



    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }


    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sum=view.findViewById(R.id.sumPrice);
        del=view.findViewById(R.id.delPrice);
        tot=view.findViewById(R.id.totPrice);
        emptyTxt=view.findViewById(R.id.empty);
        recyclerView=view.findViewById(R.id.recycleCart);
        back=view.findViewById(R.id.backBtn);
        scrollView=view.findViewById(R.id.scroll);
        orderBtn = view.findViewById(R.id.checkBtn);

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
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        managmentCart = new ManagmentCart(getActivity());
        sessionManager = new SessionManager(getContext());
        db = FirebaseFirestore.getInstance();
        //setVariable();

        calculateTotal();
        initCartList();

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order();
            }
        });


    }


    private void order() {


        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        DateFormat tf = new SimpleDateFormat("h:mm a");
        String date = df.format(Calendar.getInstance().getTime());
        String time = tf.format(Calendar.getInstance().getTime());
        String img ="" ;
        Map<String,Object> order = new HashMap<>();
        List<Map> Order = new ArrayList<>();
        ArrayList<Foods> orderList = managmentCart.getListCart();

        for (int i = 0; i < orderList.size(); i++) {
            Map<String,Object>newOrder = new HashMap<>();
            newOrder.put("item",orderList.get(i).getTitle());
            newOrder.put("price",orderList.get(i).getPrice());
            newOrder.put("quantity",orderList.get(i).getNumberInCart());
            newOrder.put("image",orderList.get(i).getImagePath());
            Order.add(newOrder);
            img = orderList.get(i).getImagePath();
        }
        order.put("userId",sessionManager.getUserId());
        order.put("userName",sessionManager.getUserName());
        order.put("total",total);
        order.put("orderItems",Order);
        order.put("date",date);
        order.put("time",time);
        order.put("imageV",img);
        order.put("status","Order Placed");
        /**/

        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_payment, null);
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(scrollView.findViewById(R.id.scroll), Gravity.CENTER, 0, 0);

        // Dim the background
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = 0.7f;
        getActivity().getWindow().setAttributes(layoutParams);

        popupWindow.setOnDismissListener(() -> {
            // Restore background brightness
            layoutParams.alpha = 1.0f;
            getActivity().getWindow().setAttributes(layoutParams);
        });

        Button payBtn = popupView.findViewById(R.id.placeOder);
        CardView payPal = (CardView) popupView.findViewById(R.id.paypalC);
        CardView visa = (CardView) popupView.findViewById(R.id.visaC);
        CardView cod = (CardView) popupView.findViewById(R.id.codC);


        payPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupView.findViewById(R.id.paypal).setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.blue));
                popupView.findViewById(R.id.visa).setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                popupView.findViewById(R.id.cod).setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                order.put("payment","Paypal");
            }
        });
        visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupView.findViewById(R.id.visa).setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.blue));
                popupView.findViewById(R.id.paypal).setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                popupView.findViewById(R.id.cod).setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                order.put("payment","Visa");
            }
        });
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupView.findViewById(R.id.cod).setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.blue));
                popupView.findViewById(R.id.paypal).setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                popupView.findViewById(R.id.visa).setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                order.put("payment","COD");
            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("Orders").add(order).addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), "Order Success", Toast.LENGTH_SHORT).show();
                    managmentCart.clearCart();
                    getFragmentManager().popBackStack();
                });
                popupWindow.dismiss();
            }
        });

    }

    private void initCartList() {

        if(managmentCart.getListCart().isEmpty()){
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else{
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new CartAdapter(managmentCart.getListCart(), managmentCart, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                calculateTotal();
            }
        }));

    }

    private void calculateTotal() {

        DecimalFormat df = new DecimalFormat("#.##");
        float delivery = 380;
        total = Math.round((managmentCart.getTotalFee() + delivery)*100)/100;
        float fee = Math.round(managmentCart.getTotalFee()*100)/100;
        sum.setText("Rs: " + df.format(fee));
        del.setText("Rs: " + df.format(delivery));
        tot.setText("Rs: " + df.format(total));

    }


}






















