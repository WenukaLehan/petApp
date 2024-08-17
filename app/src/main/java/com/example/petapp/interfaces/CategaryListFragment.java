package com.example.petapp.interfaces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.petapp.Domain.Category;
import com.example.petapp.Helper.SessionManager;
import com.example.petapp.R;
import com.example.petapp.adapter.CategoryAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class CategaryListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    FirebaseFirestore db;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    String collection;
    ImageSlider imageSlider;
    SessionManager sessionManager;
    TextView welcome;

    public CategaryListFragment() {
        // Required empty public constructor
    }


    public static CategaryListFragment newInstance(String param1, String param2) {
        CategaryListFragment fragment = new CategaryListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.categoryView);
        progressBar = view.findViewById(R.id.progressBarCategary);
        imageSlider = view.findViewById(R.id.imageSlider);
        welcome = view.findViewById(R.id.username);
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
        return inflater.inflate(R.layout.fragment_catogeries, container, false);
    }

    @Override
    public void onStart() {
        sessionManager = new SessionManager(getContext());
        welcome.setText("Welcome "+ sessionManager.getUserName());

        Bundle args = getArguments();
        collection = args.getString("collection");

        db = FirebaseFirestore .getInstance();
        super.onStart();

        initCategory();

        //array for dog food slid
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.slide_1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slide_2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slide_3, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slide_4, ScaleTypes.FIT));

        imageSlider.setImageList(imageList, ScaleTypes.FIT);

    }

    private void initCategory() {

        db.collection(collection).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Category> list = new ArrayList<>();
                progressBar.setVisibility(View.VISIBLE);
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String categoryId = document.getId();
                    String categoryName = document.getString("name");
                    String categoryImage = document.getString("image");

                    Category category = new Category();

                    category.setId(categoryId);
                    category.setName(categoryName);
                    category.setImagePath(categoryImage);
                    list.add(category);

                }
                if (!list.isEmpty()) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(new CategoryAdapter(list));
                }
                progressBar.setVisibility(View.GONE);
            }
        });

    }


}