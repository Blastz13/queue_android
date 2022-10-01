package com.example.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shop.Addresses.Address;
import com.example.shop.Addresses.MyAddressesActivity;
import com.example.shop.Cart.Cart;
import com.example.shop.Cart.CartAdapter;
import com.example.shop.Cart.CartItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity {

    public static List<CartItemModel> cartItemModelList;
    private RecyclerView deliveryItemsRecyclerView;
    private Button changeOrAddAddressButton;
    private Button continueButton;
    private TextView totalAmount;
    private TextView name;
    private TextView address;
    private TextView index;
    private TextView continueShopping;
    private TextView orderId;
    private ConstraintLayout orderConfirmLayout;
    private Dialog payment;
    private String orderID;
    Button payment_visa;
    Button payment_pp;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static final int SELECT_ADDRESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        deliveryItemsRecyclerView = findViewById(R.id.delivery_recyclerview);
        totalAmount = findViewById(R.id.total_cart_amount);
        changeOrAddAddressButton = findViewById(R.id.change_or_add_address_btn);
        name = findViewById(R.id.fullname_order);
        address = findViewById(R.id.address_detail);
        index = findViewById(R.id.index_address_order);
        orderConfirmLayout = findViewById(R.id.order_confirm_layout);
        continueShopping = findViewById(R.id.continue_shopping_btn);
        orderId = findViewById(R.id.order_id);

        payment = new Dialog(DeliveryActivity.this);
        payment.setContentView(R.layout.payment);
        payment.setCancelable(false);
        payment.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        payment.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        orderID = UUID.randomUUID().toString();
        orderId.setText("Order id: " + orderID);

        continueButton = findViewById(R.id.cart_continue_btn);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment.show();
                payment_visa = payment.findViewById(R.id.payment_visa);
                payment_pp = payment.findViewById(R.id.payment_pp);

                payment_pp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payment.dismiss();
                        orderConfirmLayout.setVisibility(View.VISIBLE);
                        Log.d("dbg", "Create order");
                        createOrder();
                        Cart.confirmOrder();
                        continueShopping.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent registerIntent = new Intent(DeliveryActivity.this, MainActivity.class);
                                startActivity(registerIntent);
                            }
                        });
                    }
                });

                payment_visa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payment.dismiss();
                        orderConfirmLayout.setVisibility(View.VISIBLE);
                        createOrder();
                        Cart.confirmOrder();
                        continueShopping.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent registerIntent = new Intent(DeliveryActivity.this, MainActivity.class);
                                startActivity(registerIntent);
                            }
                        });

                    }
                });

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryItemsRecyclerView.setLayoutManager(layoutManager);

        CartAdapter cartAdapter = new CartAdapter(cartItemModelList, totalAmount, false);
        deliveryItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeOrAddAddressButton.setVisibility(View.VISIBLE);
        changeOrAddAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressesIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                addressesIntent.putExtra("MODE", SELECT_ADDRESS);
                DeliveryActivity.this.startActivity(addressesIntent);
            }
        });

        try{
            name.setText(Address.addressesModelList.get(Address.selectedAddress).getName());
            address.setText(Address.addressesModelList.get(Address.selectedAddress).getAddress());
            index.setText(Address.addressesModelList.get(Address.selectedAddress).getIndex());
        }catch (Exception e){

        }
    }

    private void createOrder(){
        for(CartItemModel cartItemModel : cartItemModelList){
            if(cartItemModel.getType() == CartItemModel.CART_ITEM){
                Map<Object, Object> orderDetail = new HashMap<>();
                orderDetail.put("order_id", orderID);
                orderDetail.put("product_id", cartItemModel.getProductId());
                orderDetail.put("user_id", FirebaseAuth.getInstance().getUid());
                orderDetail.put("product_quantity", cartItemModel.getProductQuantity());
                orderDetail.put("discount_price", cartItemModel.getDiscountPrice());
                orderDetail.put("product_price", cartItemModel.getProductPrice());
                orderDetail.put("ordered_date", FieldValue.serverTimestamp());
                orderDetail.put("packed_date", FieldValue.serverTimestamp());
                orderDetail.put("shipping_date", FieldValue.serverTimestamp());
                orderDetail.put("delivered_date", FieldValue.serverTimestamp());
                orderDetail.put("cancelled_date", FieldValue.serverTimestamp());
                orderDetail.put("order_status", "Ordered");
                orderDetail.put("address", address.getText().toString());
                orderDetail.put("name", name.getText().toString());
                orderDetail.put("index", index.getText().toString());

                orderDetail.put("payment_status", "Paid");
                orderDetail.put("ordered_status", "Ordered");

                orderDetail.put("delivery_price", cartItemModel.getDeliveryPrice());

                firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                        .collection("USER_ORDERS").document(orderID).collection("ORDER_ITEMS")
                        .document(cartItemModel.getProductId())
                        .set(orderDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("dbg", "success");
                        }
                        else {
                            Log.d("dbg", "error");

                        }
                    }
                });

            }else {
                Map<Object, Object> orderDetail = new HashMap<>();
                orderDetail.put("order_id", orderID);
                orderDetail.put("user_id", FirebaseAuth.getInstance().getUid());
                orderDetail.put("total_items", cartItemModel.getTotalItems());
                orderDetail.put("total_items_price", cartItemModel.getTotalAmount());
                orderDetail.put("delivery_price", cartItemModel.getDeliveryPrice());
                orderDetail.put("final_total_amount", cartItemModel.getFinalTotalAmount());
                orderDetail.put("saved_amount", cartItemModel.getSavedAmount());
                orderDetail.put("payment_method", "Paid");
                orderDetail.put("order_status", "Ordered");
                orderDetail.put("delivery_price", cartItemModel.getDeliveryPrice());

                firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                        .collection("USER_ORDERS").document(orderID)
                        .set(orderDetail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                }
                                else{

                                }
                            }
                        });
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        name.setText(Address.addressesModelList.get(Address.selectedAddress).getName());
//        address.setText(Address.addressesModelList.get(Address.selectedAddress).getAddress());
//        index.setText(Address.addressesModelList.get(Address.selectedAddress).getIndex());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}