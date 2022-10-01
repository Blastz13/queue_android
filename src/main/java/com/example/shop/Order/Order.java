package com.example.shop.Order;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Order {
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<OrderItemModel> orderItemModelList = new ArrayList<>();

    public static void loadOrders(OrderAdapter orderAdapter){
        orderItemModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_ORDERS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot: task.getResult()){
                        if(documentSnapshot.get("user_id").toString().equals(FirebaseAuth.getInstance().getUid()) ) {
                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance()
                                    .getUid()).collection("USER_ORDERS")
                                    .document(documentSnapshot.get("order_id").toString())
                                    .collection("ORDER_ITEMS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(DocumentSnapshot documentSnapshot1: task.getResult()){
                                            firebaseFirestore.collection("PRODUCTS")
                                                    .document(documentSnapshot1.get("product_id").toString())
                                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        String dp;
                                                        if (documentSnapshot.get("delivery_price") != null){
                                                            dp = documentSnapshot.get("delivery_price").toString();
                                                        }else {
                                                            dp = "FREE";
                                                        }
                                                        orderItemModelList.add(new OrderItemModel(task.getResult().get("product_id").toString(),
                                                                task.getResult().get("product_image_1").toString(),
                                                                task.getResult().get("product_title").toString(), documentSnapshot.get("order_status").toString(), documentSnapshot1.get("address").toString(),
                                                                task.getResult().get("product_price").toString(), task.getResult().get("product_discount_price").toString(), documentSnapshot1.getDate("ordered_date"),
                                                                documentSnapshot1.getDate("packed_date"), documentSnapshot1.getDate("shipping_date"),
                                                                documentSnapshot1.getDate("delivered_date"), documentSnapshot1.getDate("cancelled_date"),
                                                                documentSnapshot.get("order_id").toString(), documentSnapshot1.get("name").toString(), documentSnapshot1.get("index").toString(),
                                                                documentSnapshot.get("user_id").toString(), Long.parseLong(documentSnapshot1.get("product_quantity").toString()), dp));
                                                        orderAdapter.notifyDataSetChanged();
                                                    }
                                                    else{
                                                        ;
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    else{
                                        ;
                                    }
                                }
                            });
                        }
                    }
                }
                else {

                }
            }
        });
    }
}
