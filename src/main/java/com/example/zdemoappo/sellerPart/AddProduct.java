package com.example.zdemoappo.sellerPart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zdemoappo.R;
import com.example.zdemoappo.model.ImageModel;
import com.example.zdemoappo.model.ProductsModel;
import com.example.zdemoappo.userPart.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class AddProduct extends AppCompatActivity {

    Button uploadImageButton, uploadButton;
    EditText nameET, categoryET, descriptionET;
    private final int PICK_IMAGE_REQUEST = 2;
    ImageView uploadedImageView;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri filePath;
    Uri downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        bottomNavigation();
        findViewByIdFun();
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image..."), PICK_IMAGE_REQUEST);
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
    }

    private void uploadData() {
        if (nameET.getText().toString().trim().isEmpty() || categoryET.getText().toString().trim().isEmpty() || descriptionET.getText().toString().trim().isEmpty() || filePath == null) {
            Toast.makeText(getApplicationContext(), "Please fill all fields...", Toast.LENGTH_SHORT).show();
        } else {

            uploadImage();
            ProductsModel model = new ProductsModel(nameET.getText().toString().trim(), descriptionET.getText().toString().trim(), categoryET.getText().toString());

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("Products")
                    .document(nameET.getText().toString())
                    .set(model)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            categoryET.setText("");
                            descriptionET.setText("");
                            Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadedImageView.setVisibility(View.VISIBLE);
                uploadedImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void findViewByIdFun() {
        uploadImageButton = (Button) findViewById(R.id.uploadImageButton);
        uploadButton = (Button) findViewById(R.id.uploadButton);
        nameET = (EditText) findViewById(R.id.productNameET);
        categoryET = (EditText) findViewById(R.id.categoryET);
        uploadedImageView = (ImageView) findViewById(R.id.uploadedImage);
        uploadedImageView.setVisibility(View.GONE);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        descriptionET = (EditText) findViewById(R.id.descriptionET);
        downloadUrl = null;
    }

    private void bottomNavigation() {
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.becomeSeller);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    default:
                        return true;
                }
            }
        });
    }

    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/" + nameET.getText().toString());

            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!urlTask.isSuccessful()) ;
                                    downloadUrl = urlTask.getResult();

                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    String titleName = nameET.getText().toString();

                                    ImageModel model = new ImageModel(nameET.getText().toString(), downloadUrl.toString());

                                    db.collection("ImagePath")
                                            .document(nameET.getText().toString())
                                            .set(model)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    filePath = null;
                                    uploadedImageView.setImageDrawable(null);

                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Image Uploaded!!", Toast.LENGTH_LONG).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
        }
    }
}