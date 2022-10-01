package com.example.shop.Category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;


import com.example.shop.R;
import com.example.shop.SearchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends AppCompatActivity {

    private GridView categoryGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String title = getIntent().getStringExtra("CategoryTitle");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        categoryGridView = findViewById(R.id.category_grid_view);


        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();


        GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductScrollModelList);

        firebaseFirestore.collection("PRODUCTS").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                if(documentSnapshot.get("category_name").toString().equals(title)) {
                                    horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_id").toString(),
                                            documentSnapshot.get("product_image_1").toString(),
                                            documentSnapshot.get("product_title").toString(),
                                            documentSnapshot.get("product_price").toString()));
                                    gridProductLayoutAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });

        categoryGridView.setAdapter(gridProductLayoutAdapter);
        gridProductLayoutAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_search_icon){
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}