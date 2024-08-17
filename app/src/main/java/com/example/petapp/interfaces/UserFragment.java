package com.example.petapp.interfaces;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.petapp.Helper.SessionManager;
import com.example.petapp.R;
import com.example.petapp.activity.AddressActivity;
import com.example.petapp.activity.EditUserActivity;


public class UserFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    LinearLayout notifications, addresses, logOut, UserDocuments;
    Button editBtn;
    private SessionManager sessionManager;
    TextView email,user;

    public UserFragment() {
        // Required empty public constructor
    }


    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notifications = view.findViewById(R.id.Notifications);
        addresses = view.findViewById(R.id.addresses);
        logOut = view.findViewById(R.id.logOut);
        UserDocuments = view.findViewById(R.id.UserDocuments);
        editBtn = view.findViewById(R.id.editBtn);
        email = view.findViewById(R.id.email);
        user = view.findViewById(R.id.username);
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
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        sessionManager = new SessionManager(getContext());
        if (sessionManager.isLoggedIn()) {
            user.setText(sessionManager.getUserName());
            email.setText(sessionManager.getUserEmail());
        }
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), EditUserActivity.class);
                startActivity(intent);

            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifications.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.blue));
                addresses.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                logOut.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                UserDocuments.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
            }
        });

        addresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), AddressActivity.class);
                startActivity(intent);
                addresses.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.blue));
                notifications.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                logOut.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                UserDocuments.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));

            }
        });

        UserDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserDocuments.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.blue));
                notifications.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                logOut.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                addresses.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));

            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.blue));
                notifications.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                addresses.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                UserDocuments.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.white));
                sessionManager.logoutUser();
                getActivity().finish();
            }
        });

    }

}