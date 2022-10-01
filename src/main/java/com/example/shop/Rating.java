package com.example.shop;

import androidx.annotation.NonNull;

import com.example.shop.Order.MyOrdersFragment;
import com.example.shop.Order.Order;
import com.example.shop.Product.ProductDetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Rating {
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<String> RatedId = new ArrayList<>();
    public static List<Long> Rating = new ArrayList<>();

    public static void loadRating(){
        if(!ProductDetailActivity.is_rating_query) {
            ProductDetailActivity.is_rating_query = true;
            RatedId.clear();
            Rating.clear();
            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                    .collection("USER_DATA").document("RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        List<String> orderProductsId = new ArrayList<>();
                        for(int i = 0; i < Order.orderItemModelList.size(); i++){
                            orderProductsId.add(Order.orderItemModelList.get(i).getProductId());
                        }
                        for (long i = 0; i < (long) task.getResult().get("size_list"); i++) {
                            RatedId.add(task.getResult().get("product_id_" + i).toString());
                            Rating.add(Long.parseLong(task.getResult().get("rating_" + i).toString()));

                            if (task.getResult().get("product_id_" + i).toString().equals(ProductDetailActivity.productId)) {
                                ProductDetailActivity.InitRating = Integer.parseInt(task.getResult().get("rating_" + i).toString()) - 1;
                                if (ProductDetailActivity.rateNowLayoutContainer != null) {
                                    ProductDetailActivity.setRating(ProductDetailActivity.InitRating);
                                }
                            }
                            if(orderProductsId.contains(task.getResult().get("product_id_" + i).toString())){
                                Order.orderItemModelList.get(orderProductsId.indexOf(task.getResult().get("product_id_" + i).toString())).setRating(Integer.parseInt(task.getResult().get("rating_" + i).toString()) - 1);
                            }
                        }
                        if(MyOrdersFragment.orderAdapter != null){
                            MyOrdersFragment.orderAdapter.notifyDataSetChanged();
                        }
                    } else {
                        ;
                    }
                    ProductDetailActivity.is_rating_query = false;
                }
            });
        }
    }
}
