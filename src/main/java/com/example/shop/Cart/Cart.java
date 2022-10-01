package com.example.shop.Cart;

import androidx.annotation.NonNull;

import com.example.shop.Product.ProductDetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<String> cartList = new ArrayList<>();
    public static List<CartItemModel> cartItemModelList = new ArrayList<>();

    public static void confirmOrder(){
        Map<String, Object> updateCart = new HashMap<>();
        updateCart.put("size_list", 0);

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("CART").set(updateCart)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Cart.cartItemModelList.clear();
                            Cart.cartList.clear();
                        }
                        else{
                            ;
                        }
                    }
                });
    }
    public static void removeItemCart(final int index){
        final String removeProductId = cartList.get(index);
        cartList.remove(index);
        Map<String, Object> updateCartList = new HashMap<>();

        for(int i=0; i < cartList.size(); i++){
            updateCartList.put("product_id_"+i, cartList.get(i));
        }
        updateCartList.put("size_list", (long) cartList.size());
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("CART")
                .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(cartItemModelList.size() != 0){
                        cartItemModelList.remove(index);
                        CartFragment.cartAdapter.notifyDataSetChanged();
                    }
                    if (cartList.size() == 0){
                        cartItemModelList.clear();
                    }
                }
                else {
                    cartList.add(index, removeProductId);

                }
                ProductDetailActivity.is_cart_query = false;
            }
        });
    }

    public static void loadCart(boolean isLoad){
        cartList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("CART").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            for(int i=0; i < (long)task.getResult().get("size_list"); i++){
                                cartList.add(task.getResult().get("product_id_"+i).toString());

                                if(Cart.cartList.contains(ProductDetailActivity.productId)){
                                    ProductDetailActivity.isAddedToCart = true;
                                }
                                else{
                                    ProductDetailActivity.isAddedToCart = false;
                                }

                                if(isLoad){
                                    cartItemModelList.clear();
                                    final String productId = task.getResult().get("product_id_"+i).toString();
                                    firebaseFirestore.collection("PRODUCTS").document(productId)
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                int index = 0;
                                                if(cartList.size() >= 2){
                                                    index = cartList.size()-2;
                                                }
                                                cartItemModelList.add(index, new CartItemModel(CartItemModel.CART_ITEM,
                                                        productId,
                                                        task.getResult().get("product_image_1").toString(),
                                                        task.getResult().get("product_title").toString(),
                                                        (long)1,
                                                        task.getResult().get("product_price").toString(),
                                                        task.getResult().get("product_discount_price").toString(),
                                                        (long)1,
                                                        (long)1,
                                                        (long)1,
                                                        (boolean) task.getResult().get("in_stock")));

                                                if(cartList.size() == 0){
                                                    cartItemModelList.clear();
                                                }
                                                CartFragment.cartAdapter.notifyDataSetChanged();
                                            }
                                            else{
;
                                            }
                                        }
                                    });
                                }
                                else{
                                    ;
                                }
                            }
                            if (cartList.size() >= 1){
                                cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                            }
                        }
                        else{
                            ;
                        }
                    }
                });
    }

}
