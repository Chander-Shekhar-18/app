package com.example.zdemoappo.sellerPart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.zdemoappo.R;
import com.example.zdemoappo.userPart.HomeActivity;
import com.example.zdemoappo.userPart.MLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SSignUpActivity extends AppCompatActivity {

    TextInputEditText nameET, emailET, passwordET;
    SharedPreferences sharedPreferences;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssign_up);

        nameET = (TextInputEditText) findViewById(R.id.nameEditText);
        emailET = (TextInputEditText) findViewById(R.id.emailEditText);
        passwordET = (TextInputEditText) findViewById(R.id.passwordEditText);
        sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
    }

    public void goToNext(View view) {

        if (nameET.getText().toString().trim().isEmpty() && passwordET.getText().toString().trim().isEmpty() && emailET.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter full form...", Toast.LENGTH_SHORT).show();
        } else {

            auth.createUserWithEmailAndPassword(emailET.getText().toString().trim(), passwordET.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String key = auth.getUid();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", nameET.getText().toString().trim());
                            userData.put("email", emailET.getText().toString().trim());
                            userData.put("password", passwordET.getText().toString().trim());

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            db.collection("seller")
                                    .document(key)
                                    .set(userData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            SharedPreferences.Editor myEdit;
                                            myEdit = sharedPreferences.edit();
                                            myEdit.putString("SellerId", auth.getUid());
                                            myEdit.putString("SellerLogin", "logged in");
                                            myEdit.commit();
                                            Toast.makeText(getApplicationContext(), "Seller successfully registered", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Something went wrong: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    public void login(View view) {
        startActivity(new Intent(getApplicationContext(), SLoginActivity.class));
        finish();
    }
}