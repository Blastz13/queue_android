package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        View navHost = findViewById(R.id.navigationHostFragment);
        ImageView noInternetImage = findViewById(R.id.no_internet_image);

        if(networkInfo == null){
            navHost.setVisibility(View.GONE);
            noInternetImage.setVisibility(View.VISIBLE);
        }
        else{
            noInternetImage.setVisibility(View.GONE);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.navigationHostFragment);
//        NavController navController = navHostFragment.getNavController();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navigationHostFragment);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.cartFragment, R.id.wishFragment, R.id.accountFragment, R.id.orderFragment, R.id.orderDetailFragment).build();
        if(getIntent().getBooleanExtra("is_cart", false)){
            navController.navigate(R.id.cartFragment);
        }

    }
}