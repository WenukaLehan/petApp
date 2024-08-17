package com.example.petapp.interfaces;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.petapp.Helper.SessionManager;
import com.example.petapp.R;
import com.example.petapp.activity.KnowladgeActivity;


public class HomeFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    ViewPager viewPager;

    public HomeFragment() {
        // Required empty public constructor
    }



    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onStart() {
        super.onStart();
        SessionManager sessionManager = new SessionManager(getContext());
        TextView textView = getView().findViewById(R.id.email);
        textView.setText(sessionManager.getUserEmail());

        getView().findViewById(R.id.dogFoodsFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("collection", "food_categories" );
                Fragment f = new CategaryListFragment();
                f.setArguments(bundle);
                loadFragment(f);
            }
        });

        getView().findViewById(R.id.nutFoodsFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("collection", "nutrition_categories" );
                Fragment f = new CategaryListFragment();
                f.setArguments(bundle);
                loadFragment(f);
            }
        });

        getView().findViewById(R.id.knowladge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), KnowladgeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

}