package com.example.shop.Category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.shop.R;
import com.example.shop.Wish.WishlistAdapter;
import com.example.shop.Wish.WishlistModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllProductsActivity extends AppCompatActivity {

    private RecyclerView allProductsRecyclerView;
    private GridView allProductsGridView;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("category_title"));

        allProductsRecyclerView = findViewById(R.id.all_products_recyclerview);
        allProductsGridView = findViewById(R.id.all_products_gridview);

        int type_layout = getIntent().getIntExtra("type_layout", -1);

        firebaseFirestore = FirebaseFirestore.getInstance();

        if(type_layout == 0){

            allProductsRecyclerView.setVisibility(View.VISIBLE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            allProductsRecyclerView.setLayoutManager(layoutManager);

            List<WishlistModel> wishlistModelList = new ArrayList<>();

            WishlistAdapter wishlistAdapter = new WishlistAdapter(wishlistModelList, false);
            allProductsRecyclerView.setAdapter(wishlistAdapter);
            wishlistAdapter.notifyDataSetChanged();

            firebaseFirestore.collection("CATEGORIES").document("HOME").collection("POPULAR_PRODUCTS").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                    if((long)documentSnapshot.get("type") == 1){
                                        for(int i=1; i <= (long) documentSnapshot.get("count_products"); i++){
                                            wishlistModelList.add(new WishlistModel(documentSnapshot.get("product_id_"+i).toString(),
                                                    documentSnapshot.get("product_image_"+i).toString(),
                                                    Long.parseLong(documentSnapshot.get("product_coupon_"+i).toString()),
                                                    Long.parseLong(documentSnapshot.get("total_rating_"+i).toString()),
                                                    documentSnapshot.get("product_title_"+i).toString(),
                                                    documentSnapshot.get("avg_rating_"+i).toString(),
                                                    documentSnapshot.get("product_price_"+i).toString(),
                                                    documentSnapshot.get("product_discount_price_"+i).toString()));
                                            wishlistAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        }
                    });
        }
        else if (type_layout == 1){

            allProductsGridView.setVisibility(View.VISIBLE);

            List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
            GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductScrollModelList);

            firebaseFirestore.collection("CATEGORIES").document("HOME").collection("POPULAR_PRODUCTS").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                    if((long)documentSnapshot.get("type") == 2){
                                        for(int i=1; i <= (long) documentSnapshot.get("count_products"); i++){
                                            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_id_"+i).toString(),
                                                    documentSnapshot.get("product_image_"+i).toString(),
                                                    documentSnapshot.get("product_title_"+i).toString(),
                                                    documentSnapshot.get("product_price_"+i).toString()));
                                            gridProductLayoutAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        }
                    });

            allProductsGridView.setAdapter(gridProductLayoutAdapter);
            gridProductLayoutAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}