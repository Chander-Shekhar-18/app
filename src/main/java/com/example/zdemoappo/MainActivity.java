package com.example.zdemoappo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.zdemoappo.userPart.HomeActivity;
import com.example.zdemoappo.userPart.MLoginActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String loginOrNot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        loginOrNot = sharedPreferences.getString("Login", "");
        Thread thread;
        if (loginOrNot.equals("logged in")) {
            thread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } finally {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
        } else {
            thread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Intent intent = new Intent(MainActivity.this, MLoginActivity.class);
                        startActivity(intent);
                        finish();
                    } finally {
                        Intent intent = new Intent(MainActivity.this, MLoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
        }
        thread.start();

    }
}