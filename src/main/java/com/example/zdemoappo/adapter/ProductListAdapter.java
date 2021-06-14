package com.example.zdemoappo.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zdemoappo.model.ImageModel;
import com.example.zdemoappo.userPart.ProductInfoActivity;
import com.example.zdemoappo.R;
import com.example.zdemoappo.model.ProductsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter {

    Context context;
    List<ProductsModel> list = new ArrayList<>();

    public ProductListAdapter(Context context, List<ProductsModel> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, categoryTV;
        ImageView productImage;
        FirebaseFirestore db;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = (TextView) itemView.findViewById(R.id.productNameTv);
            categoryTV = (TextView) itemView.findViewById(R.id.categoryTv);
            productImage = (ImageView) itemView.findViewById(R.id.productImageD);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl);
            db = FirebaseFirestore.getInstance();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_list_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ProductsModel model = list.get(position);

        viewHolder.nameTV.setText(model.getTitle());
        viewHolder.categoryTV.setText(model.getCategory());

        DocumentReference documentReference = viewHolder.db.collection("ImagePath").document(model.getTitle());

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            ImageModel m = documentSnapshot.toObject(ImageModel.class);
                            Picasso.get().load(m.getUrl()).into(viewHolder.productImage);
                        }
                    }
                });

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductInfoActivity.class);
                intent.putExtra("productName", model.getTitle());
                intent.putExtra("categoryName", model.getCategory());
                intent.putExtra("imageUrl", model.getImageUrl());
                intent.putExtra("description", model.getDescription());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
