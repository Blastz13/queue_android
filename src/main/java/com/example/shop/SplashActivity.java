package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.shop.Auth.RegistrationActivity;


public class SplashActivity extends AppCompatActivity {
    SharedPreferences PreferenceStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceStorage = getSharedPreferences("com.example.shop", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        if (PreferenceStorage.contains("JWT_TOKEN")) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
        }
        super.onStart();
    }
}