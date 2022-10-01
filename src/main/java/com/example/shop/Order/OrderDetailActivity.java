package com.example.shop.Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderDetailActivity extends AppCompatActivity {

    private int position;
    private TextView productTitle;
    private TextView productPrice;
    private TextView productQuantity;
    private ImageView productImage;
    private ImageView orderedIndicator;
    private ImageView packedIndicator;
    private ImageView shippedIndicator;
    private ImageView deliveredIndicator;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;
    private TextView orderedTitle;
    private TextView packedTitle;
    private TextView shippedTitle;
    private TextView deliveredTitle;
    private TextView orderedDate;
    private TextView packedDate;
    private TextView shippedDate;
    private TextView deliveredDate;
    private TextView orderedBody;
    private TextView packedBody;
    private TextView shippedBody;
    private TextView deliveredBody;
    private TextView name;
    private TextView address;
    private TextView index;
    private TextView totalItemsPrice;
    private TextView deliveryPrice;
    private TextView totalPrice;
    private TextView savedAmount;
    private LinearLayout rateNowContainer;
    private int rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Детали заказа");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        position = getIntent().getIntExtra("position", -1);
        rating = getIntent().getIntExtra("rating", -1);
        OrderItemModel orderItemModel = Order.orderItemModelList.get(position);

        productTitle = findViewById(R.id.product_title_order);
        productPrice = findViewById(R.id.product_price_order);
        productQuantity = findViewById(R.id.product_quantity_order);
        productImage = findViewById(R.id.product_image_order);
        orderedIndicator = findViewById(R.id.ordered_indicator);
        packedIndicator = findViewById(R.id.package_indicator);
        shippedIndicator = findViewById(R.id.shipping_indicator);
        deliveredIndicator = findViewById(R.id.delivered_indicator);

        progressBar1 = findViewById(R.id.ordered_package_progressBar);
        progressBar2 = findViewById(R.id.package_shipping_progressBar);
        progressBar3 = findViewById(R.id.progressBar7);

        orderedTitle = findViewById(R.id.ordered_title);
        packedTitle = findViewById(R.id.package_title);
        shippedTitle = findViewById(R.id.shipping_title);
        deliveredTitle = findViewById(R.id.delivered_title);

        orderedDate = findViewById(R.id.ordered_detail);
        packedDate = findViewById(R.id.package_detail);
        shippedDate = findViewById(R.id.shipping_detail);
        deliveredDate = findViewById(R.id.delivered_detail);

        orderedBody = findViewById(R.id.ordered_body);
        packedBody = findViewById(R.id.package_body);
        shippedBody = findViewById(R.id.shipping_body);
        deliveredBody = findViewById(R.id.delivered_body);

        rateNowContainer = findViewById(R.id.rate_now_container);
        name = findViewById(R.id.fullname_order);
        address = findViewById(R.id.address_detail);
        index = findViewById(R.id.index_address_order);

        totalItemsPrice = findViewById(R.id.total_items_price);
        deliveryPrice = findViewById(R.id.delivery_charge_price);
        totalPrice = findViewById(R.id.total_price);
        savedAmount = findViewById(R.id.saved_amount);

        totalItemsPrice.setText(orderItemModel.getProductPrice().toString() + " ₽");
        if(orderItemModel.getDeliveryPrice().equals("Free")){
            deliveryPrice.setText(orderItemModel.getDeliveryPrice());
            totalPrice.setText(totalItemsPrice.getText().toString());
        }else {
            deliveryPrice.setText(orderItemModel.getDeliveryPrice() + " ₽");
            totalPrice.setText((Integer.parseInt(orderItemModel.getProductPrice()) + Integer.parseInt(orderItemModel.getDeliveryPrice())) + " ₽");

        }

        productTitle.setText(orderItemModel.getProductTitle());

        if(orderItemModel.getDiscountPrice().equals("0")){
            productPrice.setText(orderItemModel.getProductPrice() + " ₽");
        }
        else{
            productPrice.setText(orderItemModel.getProductPrice() + " ₽");
        }
        Glide.with(this).load(orderItemModel.getProductImage()).into(productImage);
        switch (orderItemModel.getDeliveryStatus()){
            case "Ordered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                orderedDate.setText(String.valueOf(orderItemModel.getOrderedDate()));

                progressBar1.setVisibility(View.GONE);
                progressBar2.setVisibility(View.GONE);
                progressBar3.setVisibility(View.GONE);

                packedIndicator.setVisibility(View.GONE);
                packedBody.setVisibility(View.GONE);
                packedDate.setVisibility(View.GONE);
                packedTitle.setVisibility(View.GONE);

                shippedIndicator.setVisibility(View.GONE);
                shippedBody.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);
                shippedTitle.setVisibility(View.GONE);

                deliveredIndicator.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);

                break;
            case "Packed":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                orderedDate.setText(String.valueOf(orderItemModel.getOrderedDate()));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                packedDate.setText(String.valueOf(orderItemModel.getPackedDate()));

                progressBar1.setProgress(100);
                progressBar2.setVisibility(View.GONE);
                progressBar3.setVisibility(View.GONE);

                shippedIndicator.setVisibility(View.GONE);
                shippedBody.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);
                shippedTitle.setVisibility(View.GONE);

                deliveredIndicator.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                break;
            case "Shipped":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                orderedDate.setText(String.valueOf(orderItemModel.getOrderedDate()));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                packedDate.setText(String.valueOf(orderItemModel.getPackedDate()));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                shippedDate.setText(String.valueOf(orderItemModel.getShippedDate()));

                progressBar1.setProgress(100);
                progressBar2.setProgress(100);
                progressBar3.setVisibility(View.GONE);


                deliveredIndicator.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                break;
            case "Delivered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                orderedDate.setText(String.valueOf(orderItemModel.getOrderedDate()));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                packedDate.setText(String.valueOf(orderItemModel.getPackedDate()));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                shippedDate.setText(String.valueOf(orderItemModel.getShippedDate()));

                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                deliveredDate.setText(String.valueOf(orderItemModel.getDeliveredDate()));

                progressBar1.setProgress(100);
                progressBar2.setProgress(100);
                progressBar3.setProgress(100);

                break;
            case "Cancelled":
                if(orderItemModel.getPackedDate().after(orderItemModel.getOrderedDate())){
                    if(orderItemModel.getShippedDate().after(orderItemModel.getPackedDate())){
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        orderedDate.setText(String.valueOf(orderItemModel.getOrderedDate()));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        packedDate.setText(String.valueOf(orderItemModel.getPackedDate()));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        shippedDate.setText(String.valueOf(orderItemModel.getShippedDate()));

                        deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        deliveredDate.setText(String.valueOf(orderItemModel.getCancelledDate()));
                        deliveredTitle.setText("Cancelled");
                        deliveredBody.setText("Your order has been cancelled");

                        progressBar1.setProgress(100);
                        progressBar2.setProgress(100);
                        progressBar3.setProgress(100);
                    }else {
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        orderedDate.setText(String.valueOf(orderItemModel.getOrderedDate()));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        packedDate.setText(String.valueOf(orderItemModel.getPackedDate()));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        shippedDate.setText(String.valueOf(orderItemModel.getCancelledDate()));
                        shippedTitle.setText("Cancelled");
                        shippedBody.setText("Your order has been cancelled");

                        progressBar1.setProgress(100);
                        progressBar2.setProgress(100);
                        progressBar3.setVisibility(View.GONE);


                        deliveredIndicator.setVisibility(View.GONE);
                        deliveredBody.setVisibility(View.GONE);
                        deliveredDate.setVisibility(View.GONE);
                        deliveredTitle.setVisibility(View.GONE);
                    }
                }else {
                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    orderedDate.setText(String.valueOf(orderItemModel.getOrderedDate()));

                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    packedDate.setText(String.valueOf(orderItemModel.getCancelledDate()));
                    packedTitle.setText("Cancelled");
                    packedBody.setText("Your order has been cancelled");

                    progressBar1.setProgress(100);
                    progressBar2.setVisibility(View.GONE);
                    progressBar3.setVisibility(View.GONE);

                    shippedIndicator.setVisibility(View.GONE);
                    shippedBody.setVisibility(View.GONE);
                    shippedDate.setVisibility(View.GONE);
                    shippedTitle.setVisibility(View.GONE);

                    deliveredIndicator.setVisibility(View.GONE);
                    deliveredBody.setVisibility(View.GONE);
                    deliveredDate.setVisibility(View.GONE);
                    deliveredTitle.setVisibility(View.GONE);
                }
                break;
            default:
//                date = orderItemModelList.get(position).getCancelledDate();
                break;

        }

        name.setText(orderItemModel.getName());
        address.setText(orderItemModel.getAddress());
        index.setText(orderItemModel.getIndex());

//        Rating.loadRating();
        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("RATINGS").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            for(long i=0; i < Long.parseLong(task.getResult().get("size_list").toString()); i++){
                                if(task.getResult().get("product_id_"+i).toString().equals(orderItemModel.getProductId())){
                                    Integer rating = Integer.parseInt(task.getResult().get("rating_"+i).toString());
                                    setRating(rating-1);
                                    break;
                                }
                            }
                        }
                        else {
                            ;
                        }
                    }
                });
//        for(int i = 0; i < rateNowContainer.getChildCount(); i++){
//            final int starPosition = i;
//            rateNowContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setRating(starPosition);
//                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("PRODUCTS").document(orderItemModel.getProductId());
//                    FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
//                        @Nullable
//                        @Override
//                        public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);
//                            if(rating!=0){
//                                Long increase = documentSnapshot.getLong("mark_"+(starPosition+1))+1;
//                                Long decrease = documentSnapshot.getLong("mark_"+(rating+1))-1;
//                                transaction.update(documentReference, "mark_"+(starPosition+1), increase);
//                                transaction.update(documentReference, "mark_"+(rating+1), decrease);
//                            }else{
//                                Long increase = documentSnapshot.getLong("mark_"+(starPosition+1))+1;
//                                transaction.update(documentReference, "mark_"+(starPosition+1), increase);
//
//                            }
//                            return null;
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<Object>() {
//                        @Override
//                        public void onSuccess(Object o) {
//                            Order.orderItemModelList.get(position).setRating(starPosition+1);
//                            if(Rating.RatedId.contains(orderItemModel.getProductId())){
//                                Rating.Rating.set(Rating.RatedId.indexOf(orderItemModel.getProductId()), Long.parseLong(String.valueOf(starPosition+1)));
//                            }else{
//                                Rating.RatedId.add(orderItemModel.getProductId());
//                                Rating.Rating.add(Long.parseLong(String.valueOf(starPosition+1)));
//                            }
//                        }
//                    });
//                }
//            });
//        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRating(int starPosition){
        for(int i = 0; i < rateNowContainer.getChildCount(); i++){
            ImageView starBtn = (ImageView) rateNowContainer.getChildAt(i);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if(i<= starPosition){
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }
}