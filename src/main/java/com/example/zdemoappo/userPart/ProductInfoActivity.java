package com.example.zdemoappo.userPart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zdemoappo.R;
import com.example.zdemoappo.adapter.CommentsAdapter;
import com.example.zdemoappo.model.CommentsModel;
import com.example.zdemoappo.model.ImageModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductInfoActivity extends AppCompatActivity {

    String productName, category, imageUrl, description;
    TextView productNameTV, categoryTV, descriptionTV;
    ImageView imageView;
    RecyclerView commentsRecyclerView;
    EditText commentET;
    Button addCommentButton;
    List<CommentsModel> commentsModelList;
    CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        findViewByIdFun();
        getValue();
        setValue();
        setImage();
        getComments();
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommentFun();
            }
        });
    }

    private void addCommentFun() {

        if (!commentET.getText().toString().isEmpty()) {

            CommentsModel m = new CommentsModel(commentET.getText().toString());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Products").document(productName).collection("Comments")
                    .add(m)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            commentET.setText("");
                            Toast.makeText(getApplicationContext(), "Comment Successfully added", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void setImage() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("ImagePath").document(productName);

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            ImageModel m = documentSnapshot.toObject(ImageModel.class);
                            Picasso.get().load(m.getUrl()).into(imageView);
                        }
                    }
                });
    }

    private void getComments() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*CollectionReference collectionReference = */db.collection("Products").document(productName).collection("Comments")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                            commentsModelList.add(snapshot.toObject(CommentsModel.class));
                        }
                        commentsRecyclerView.setAdapter(commentsAdapter);
                        commentsAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void findViewByIdFun() {
        productNameTV = (TextView) findViewById(R.id.productNameI);
        categoryTV = (TextView) findViewById(R.id.categoryTvI);
        descriptionTV = (TextView) findViewById(R.id.descriptionTv);
        imageView = (ImageView) findViewById(R.id.productImageI);
        commentsModelList = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(this, commentsModelList);
        commentsRecyclerView = (RecyclerView) findViewById(R.id.commentsRecyclerView);
        commentET = (EditText) findViewById(R.id.commentsET);
        addCommentButton = (Button) findViewById(R.id.addCommentButton);
    }

    private void setValue() {
        SpannableString content = new SpannableString(productName);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        productNameTV.setText(content);
        categoryTV.setText("Category: " + category);
        descriptionTV.setText("Description: \n\t\t" + description);
    }

    private void getValue() {
        Intent intent = getIntent();
        productName = intent.getStringExtra("productName");
        category = intent.getStringExtra("categoryName");
        imageUrl = intent.getStringExtra("imageUrl");
        description = intent.getStringExtra("description");
    }
}