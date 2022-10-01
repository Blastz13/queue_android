package com.example.shop.Product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop.Cart.Cart;
import com.example.shop.Cart.CartItemModel;
import com.example.shop.MainActivity;
import com.example.shop.R;
import com.example.shop.Rating;
import com.example.shop.SearchActivity;
import com.example.shop.Wish.WishFragment;
import com.example.shop.Wish.WishlistModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    public static boolean is_rating_query = false;
    public static boolean is_cart_query = false;
    public static boolean fromSearch = false;

    private ViewPager productImagesViewPager;
    private TabLayout viewPagerIndicator;
    private DocumentSnapshot documentSnapshot;
    private FloatingActionButton addToWishListBtn;
    private static boolean isAddedToWishList = false;
    public static boolean isAddedToCart = false;
    private ViewPager productDetailViewPager;
    private TabLayout productDetailTabLayout;
    public static LinearLayout rateNowLayoutContainer;
    public static int InitRating;
    private FirebaseFirestore firebaseFirestore;
    private TextView productTitle;
    private TextView productRatingPreview;
    private TextView productTotalRating;
    private TextView productPrice;
    private TextView productDiscountPrice;
    private TextView productAvailable;
    private TextView productTotalRatings;
    private TextView productRatingMark1;
    private TextView productRatingMark2;
    private TextView productRatingMark3;
    private TextView productRatingMark4;
    private TextView productRatingMark5;
    private TextView totalRatingFigure;
    private TextView averageRating;
    private TextView productDescription;
    private ProgressBar progressBarMark1;
    private ProgressBar progressBarMark2;
    private ProgressBar progressBarMark3;
    private ProgressBar progressBarMark4;
    private ProgressBar progressBarMark5;
    private FirebaseUser currentUser;
    private LinearLayout ratingNumbersContainer;
    private LinearLayout addToCartBtn;
    public static String productId;
    public static DocumentSnapshot tempDocumentSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager = findViewById(R.id.product_images_viewpager);
        viewPagerIndicator = findViewById(R.id.view_pager_indicator);
        addToWishListBtn = findViewById(R.id.add_to_wish_btn);
        productDetailViewPager = findViewById(R.id.product_detail_viewpager_tab);
        productDetailTabLayout = findViewById(R.id.product_detail_tablayout);
        productTitle = findViewById(R.id.product_title);
        productRatingPreview = findViewById(R.id.product_rating_preview);
        productTotalRating = findViewById(R.id.total_rating);
        productTotalRatings = findViewById(R.id.total_ratings);
        productPrice = findViewById(R.id.product_price);
        productDiscountPrice = findViewById(R.id.product_discount_price);
        productAvailable = findViewById(R.id.product_available);
        ratingNumbersContainer = findViewById(R.id.rating_numbers_container);
        productRatingMark1 = findViewById(R.id.product_rating_mark_1);
        productRatingMark2 = findViewById(R.id.product_rating_mark_2);
        productRatingMark3 = findViewById(R.id.product_rating_mark_3);
        productRatingMark4 = findViewById(R.id.product_rating_mark_4);
        productRatingMark5 = findViewById(R.id.product_rating_mark_5);
        totalRatingFigure = findViewById(R.id.total_rating_figure);
        progressBarMark1 = findViewById(R.id.progressBar_mark_1);
        progressBarMark2 = findViewById(R.id.progressBar_mark_2);
        progressBarMark3 = findViewById(R.id.progressBar_mark_3);
        progressBarMark4 = findViewById(R.id.progressBar_mark_4);
        progressBarMark5 = findViewById(R.id.progressBar_mark_5);
        averageRating = findViewById(R.id.average_rating);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        productDescription = findViewById(R.id.product_description);

        firebaseFirestore = FirebaseFirestore.getInstance();
        InitRating = -1;
        List<String> productImages = new ArrayList<>();

        ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);

                firebaseFirestore.collection("PRODUCTS").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                                if(documentSnapshot.get("product_id").toString().equals(getIntent().getStringExtra("product_id"))) {
                                    tempDocumentSnapshot = documentSnapshot;
                                    for (int i = 1; i <= (long) documentSnapshot.get("count_image_product"); i++) {
                                        productImages.add(documentSnapshot.get("product_image_" + i).toString());
                                    }
                                    productId = documentSnapshot.get("product_id").toString();
                                    productTitle.setText(documentSnapshot.get("product_title").toString());
                                    productRatingPreview.setText(documentSnapshot.get("avg_rating").toString());
                                    averageRating.setText(documentSnapshot.get("avg_rating").toString());
                                    productTotalRating.setText(documentSnapshot.get("total_rating").toString() + " Оценок");
                                    if(documentSnapshot.get("product_discount_price").toString().equals("0")){
                                        productPrice.setText(documentSnapshot.get("product_price").toString() + " ₽");
                                        productDiscountPrice.setVisibility(View.GONE);

                                    }
                                    else{
                                        productPrice.setText(documentSnapshot.get("product_price").toString() + " ₽");
                                        productDiscountPrice.setText(documentSnapshot.get("product_discount_price").toString() + " ₽");
                                    }

                                    productDescription.setText(documentSnapshot.get("product_description").toString());
                                    productAvailable.setText(documentSnapshot.get("product_available").toString());
                                    productTotalRatings.setText(documentSnapshot.get("total_rating").toString());

                                    productRatingMark1.setText(documentSnapshot.get("mark_1").toString());
                                    productRatingMark2.setText(documentSnapshot.get("mark_2").toString());
                                    productRatingMark3.setText(documentSnapshot.get("mark_3").toString());
                                    productRatingMark4.setText(documentSnapshot.get("mark_4").toString());
                                    productRatingMark5.setText(documentSnapshot.get("mark_5").toString());
                                    totalRatingFigure.setText(documentSnapshot.get("total_rating").toString());

                                    progressBarMark1.setMax(Integer.parseInt(documentSnapshot.get("total_rating").toString()));
                                    progressBarMark1.setProgress(Integer.parseInt(documentSnapshot.get("mark_1").toString()));
                                    progressBarMark2.setMax(Integer.parseInt(documentSnapshot.get("total_rating").toString()));
                                    progressBarMark2.setProgress(Integer.parseInt(documentSnapshot.get("mark_2").toString()));
                                    progressBarMark3.setMax(Integer.parseInt(documentSnapshot.get("total_rating").toString()));
                                    progressBarMark3.setProgress(Integer.parseInt(documentSnapshot.get("mark_3").toString()));
                                    progressBarMark4.setMax(Integer.parseInt(documentSnapshot.get("total_rating").toString()));
                                    progressBarMark4.setProgress(Integer.parseInt(documentSnapshot.get("mark_4").toString()));
                                    progressBarMark5.setMax(Integer.parseInt(documentSnapshot.get("total_rating").toString()));
                                    progressBarMark5.setProgress(Integer.parseInt(documentSnapshot.get("mark_5").toString()));
                                    if (WishFragment.wl.size() == 0){
                                        WishFragment.loadWishList();
                                    }
                                    if(Rating.Rating.size() == 0){
                                        Rating.loadRating();
                                    }
                                    if(Cart.cartList.size() == 0){
                                        Cart.loadCart(false);
                                    }
                                    if(Rating.RatedId.contains(productId)){
                                        int index = Rating.RatedId.indexOf(productId);
                                        InitRating = Integer.parseInt(String.valueOf(Rating.Rating.get(index))) - 1;
                                        setRating(InitRating);
                                    }

                                    if(Cart.cartList.contains(getIntent().getStringExtra("product_id"))){
                                        isAddedToCart = true;
                                    }
                                    else{
                                        isAddedToCart = false;
                                    }

                                    if(WishFragment.wl.contains(getIntent().getStringExtra("product_id"))){
                                        isAddedToWishList = true;
                                        addToWishListBtn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                                    }
                                    else{
                                        isAddedToWishList = false;
                                    }
                                    if((boolean) documentSnapshot.get("in_stock")){
                                        addToCartBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast toast = Toast.makeText(getApplicationContext(),
                                                        "Товар добавлен в корзину", Toast.LENGTH_LONG);
                                                toast.show();

                                                if(!is_cart_query){
                                                    is_cart_query = true;
                                                    if(isAddedToCart){
                                                        is_cart_query = false;
                                                    }
                                                    else{
                                                        Map<String, Object> addProduct = new HashMap<>();
                                                        addProduct.put("product_id_" + String.valueOf(Cart.cartList.size()), productId);
                                                        addProduct.put("size_list", Long.parseLong(String.valueOf(Cart.cartList.size()))+1);

                                                        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                                                                .collection("USER_DATA").document("CART")
                                                                .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    if(Cart.cartItemModelList.size() != 0){
                                                                        Cart.cartItemModelList.add(0 , new CartItemModel(CartItemModel.CART_ITEM,
                                                                                productId,
                                                                                tempDocumentSnapshot.get("product_image_1").toString(),
                                                                                tempDocumentSnapshot.get("product_title").toString(),
                                                                                (long)1,
                                                                                tempDocumentSnapshot.get("product_price").toString(),
                                                                                tempDocumentSnapshot.get("product_discount_price").toString(),
                                                                                (long)1,
                                                                                (long)1,
                                                                                (long)1,
                                                                                (boolean)tempDocumentSnapshot.get("in_stock")));
                                                                    }
                                                                    isAddedToCart = true;
                                                                    Cart.cartList.add(productId);
                                                                    is_cart_query = false;

                                                                }
                                                                else{
                                                                    is_cart_query = false;
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        });

                                    }
                                    else {
                                        TextView outStock = (TextView) addToCartBtn.getChildAt(0);
                                        outStock.setText("Товар закончился");
                                        outStock.setTextColor(getResources().getColor(R.color.black));
                                        outStock.setCompoundDrawables(null, null, null, null);
                                    }
                                    productImagesAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        else{
                            ;
                        }
                    }
                });

        productImagesViewPager.setAdapter(productImagesAdapter);
        viewPagerIndicator.setupWithViewPager(productImagesViewPager, true);

        addToWishListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddedToWishList){
                    int index = WishFragment.wl.indexOf(productId);
                    WishFragment.removeWishList(index);
                    isAddedToWishList = false;
                    addToWishListBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                }
                else{

                        WishFragment.wishlistModelList.add(new WishlistModel(productId,
                                tempDocumentSnapshot.get("product_image_1").toString(),
                                1,
                                Long.parseLong(tempDocumentSnapshot.get("total_rating").toString()),
                                tempDocumentSnapshot.get("product_title").toString(),
                                tempDocumentSnapshot.get("avg_rating").toString(),
                                tempDocumentSnapshot.get("product_price").toString(),
                                tempDocumentSnapshot.get("product_discount_price").toString()));
                    Map<String, Object> add_product_id = new HashMap<>();
                    for(int j=0; j < WishFragment.wl.size(); j++){
                        add_product_id.put("product_id_"+j, WishFragment.wl.get(j));
                    }
                    add_product_id.put("product_id_"+String.valueOf(WishFragment.wl.size()), productId);
                    String f = FirebaseAuth.getInstance().getUid();

                    firebaseFirestore.collection("USERS").document(f).collection("USER_DATA").document("WISHLIST")
                            .set(add_product_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Map<String, Object> update_size_list = new HashMap<>();
                                update_size_list.put("size_list", (long)(WishFragment.wl.size()+1));
                                firebaseFirestore.collection("USERS").document(f).collection("USER_DATA").document("WISHLIST")
                                        .update(update_size_list).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            isAddedToWishList = true;
                                            addToWishListBtn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                                            WishFragment.wl.add(productId);
                                        }else{
                                            ;
                                        }
                                    }
                                });
                            }else{
                       ;
                            }
                        }
                    });
                }
            }
        });

        ProductDetailAdapter productDetailAdapter = new ProductDetailAdapter(getSupportFragmentManager(), 1);

        rateNowLayoutContainer = findViewById(R.id.rate_now_container);
        for(int i = 0; i < rateNowLayoutContainer.getChildCount(); i++){
            final int starPosition = i;
            rateNowLayoutContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!is_rating_query) {
                        is_rating_query = true;
                        setRating(starPosition);
                        Map<String, Object> updateRatingProducts = new HashMap<>();
                        if (Rating.RatedId.contains(productId)) {
                            TextView[] mas = {productRatingMark1, productRatingMark2, productRatingMark3, productRatingMark4 ,productRatingMark5};
                            updateRatingProducts.put("mark_" + (InitRating + 1), Long.parseLong(mas[InitRating].getText().toString())-1);
                            updateRatingProducts.put("mark_" + (starPosition + 1),  Long.parseLong(mas[starPosition].getText().toString())+1);
                            updateRatingProducts.put("avg_rating", String.valueOf(calculateAvgRating()));
                        } else {
                            Log.d("dbg", "No_Contains");
                            updateRatingProducts.put("mark_" + (starPosition + 1), Long.parseLong(String.valueOf(Integer.parseInt(tempDocumentSnapshot.get("mark_" + (starPosition + 1)).toString()) + 1)));
                            updateRatingProducts.put("avg_rating", String.valueOf(calculateAvgRating()));
                            updateRatingProducts.put("total_rating", Long.parseLong(tempDocumentSnapshot.get("total_rating").toString()) + 1);
                            InitRating = starPosition;
                        }


                            firebaseFirestore.collection("PRODUCTS").document(productId).update(updateRatingProducts).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> tempRating = new HashMap<>();

                                        if (Rating.RatedId.contains(productId)) {
                                            tempRating.put("rating_"+Rating.RatedId.indexOf(productId), (long)starPosition+1);
                                        }
                                        else{
                                            tempRating.put("size_list", (long)Rating.RatedId.size()+1);
                                            tempRating.put("product_id_" + Rating.RatedId.size(), productId);
                                            tempRating.put("rating_" + Rating.RatedId.size(), (long) starPosition + 1);
                                        }

                                        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                                                .collection("USER_DATA").document("RATINGS")
                                                .update(tempRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    TextView[] mas = {productRatingMark1, productRatingMark2, productRatingMark3, productRatingMark4 ,productRatingMark5};
                                                    ProgressBar[] prg = {progressBarMark1, progressBarMark2, progressBarMark3, progressBarMark4 ,progressBarMark5};

                                                    if (Rating.RatedId.contains(productId)) {
                                                        Rating.Rating.set(Rating.RatedId.indexOf(productId), (long)starPosition+1);
                                                        mas[starPosition].setText(String.valueOf(Integer.parseInt(mas[starPosition].getText().toString())+1));
                                                        mas[InitRating].setText(String.valueOf(Integer.parseInt(mas[InitRating].getText().toString())-1));

                                                        TextView oldRating = (TextView) ratingNumbersContainer.getChildAt(5-InitRating - 1);
                                                        TextView newRating = (TextView) ratingNumbersContainer.getChildAt(5-InitRating - 1);
                                                        oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString())-1));
                                                        newRating.setText(String.valueOf(Integer.parseInt(newRating.getText().toString())+1));
                                                    }
                                                    else {


                                                        Rating.RatedId.add(productId);
                                                        Rating.Rating.add((long) starPosition + 1);
                                                        mas[starPosition].setText(String.valueOf(Integer.parseInt(mas[starPosition].getText().toString())+1));

                                                        productTotalRatings.setText(String.valueOf(Long.parseLong(tempDocumentSnapshot.get("total_rating").toString()) + 1));
                                                        productTotalRating.setText(String.valueOf(Long.parseLong(tempDocumentSnapshot.get("total_rating").toString()) + 1));
                                                        totalRatingFigure.setText(String.valueOf(Long.parseLong(tempDocumentSnapshot.get("total_rating").toString()) + 1));
                                                        totalRatingFigure.setText(String.valueOf(Long.parseLong(tempDocumentSnapshot.get("total_rating").toString()) + 1));

                                                    }
                                                    for(int i=0; i <=4; i++){
                                                        prg[i].setMax(Integer.parseInt(totalRatingFigure.getText().toString()));
                                                        prg[i].setProgress(Integer.parseInt(mas[i].getText().toString()));
                                                    }

                                                    InitRating = starPosition;
                                                    averageRating.setText(String.valueOf(calculateAvgRating()));
                                                    productRatingPreview.setText(String.valueOf(calculateAvgRating()));
                                                } else {
                                                    setRating(InitRating);
                                                }
                                                is_rating_query = false;
                                            }
                                        });
                                    } else {
                                        is_rating_query = false;
                                        setRating(InitRating);
                                    }
                                }
                            });
                    }
                }
            });
        }
    }

    public static void setRating(int starPosition) {
        if (starPosition > -1) {
            for (int i = 0; i < rateNowLayoutContainer.getChildCount(); i++) {
                ImageView starBtn = (ImageView) rateNowLayoutContainer.getChildAt(i);
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                if (i <= starPosition) {
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
                }
            }
        }
    }

    private float calculateAvgRating(){
        float totalStars = 0;
        TextView[] mas = {productRatingMark1, productRatingMark2, productRatingMark3, productRatingMark4 ,productRatingMark5};
        for(int i=1; i < 6; i++){
            totalStars += (Long.parseLong(mas[i-1].getText().toString()))*i;
        }
        float total_count = Long.parseLong(productTotalRatings.getText().toString());
        if (total_count == 0){
            total_count+=1;
        }
        float avg_rating = totalStars/total_count;
        avg_rating = (float) (Math.floor(avg_rating*10)/10.0);
        Map<String, Object> tempRating = new HashMap<>();
        tempRating.put("avg_rating", String.valueOf(avg_rating));
        firebaseFirestore.collection("PRODUCTS").document(productId).update(tempRating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    ;
                }
            }
        });

        return avg_rating;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_cart_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        else if (id == R.id.main_search_icon){
            if(fromSearch){
                finish();
            }else {
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
            }
            return true;
        }
        else if (id == R.id.main_cart_icon){
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra("is_cart", true);
            this.startActivity(mainIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSearch = false;
    }
}