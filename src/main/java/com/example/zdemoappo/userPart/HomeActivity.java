package com.example.zdemoappo.userPart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.zdemoappo.R;
import com.example.zdemoappo.adapter.CommentsAdapter;
import com.example.zdemoappo.adapter.ProductListAdapter;
import com.example.zdemoappo.model.CommentsModel;
import com.example.zdemoappo.model.ProductsModel;
import com.example.zdemoappo.sellerPart.SLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ImageSlider slider;
    List<SlideModel> slideModels;
    RecyclerView recyclerView;
    List<ProductsModel> productList;
    ProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewByIdFun();
        bottomNavigation();
        getData();
        getSlider();
    }

    private void getSlider() {
        slideModels.add(new SlideModel(R.drawable.slide1, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.slide2, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.slide3, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.slide4, ScaleTypes.CENTER_CROP));
        slider = findViewById(R.id.imageSlider);
        slider.setImageList(slideModels);

    }


    private void getData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                            productList.add(snapshot.toObject(ProductsModel.class));
                        }
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
//        CollectionReference reference = db.collection("Products");
    }

    private void bottomNavigation() {
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.becomeSeller:
                        startActivity(new Intent(getApplicationContext(), SLoginActivity.class));
                    default:
                        return true;
                }
            }
        });
    }

    private void findViewByIdFun() {
        productList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new ProductListAdapter(this, productList);
        slider = (ImageSlider) findViewById(R.id.imageSlider);
        slideModels = new ArrayList<>();

    }
}