package com.example.zdemoappo.userPart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zdemoappo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MLoginActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextInputEditText emailTV, passwordTV;
    Button loginButton;
    ProgressDialog progressDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlogin);

        emailTV = (TextInputEditText) findViewById(R.id.emailEditText);
        passwordTV = (TextInputEditText) findViewById(R.id.passwordEditText);
        sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        loginButton = (Button) findViewById(R.id.loginButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
    }

    public void goToNext(View view) {
        String email, password;
        email = emailTV.getText().toString().trim();
        password = passwordTV.getText().toString().trim();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter correct Credentials", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog.setMessage("pleaseWait");
            progressDialog.show();

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SharedPreferences.Editor myEdit;
                                myEdit = sharedPreferences.edit();
                                myEdit.putString("UserId", auth.getUid());
                                myEdit.putString("Login", "logged in");
                                myEdit.commit();
                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
        }
    }

    public void signUp(View view) {
        startActivity(new Intent(getApplicationContext(), MSignUpActivity.class));
        finish();
    }
}
