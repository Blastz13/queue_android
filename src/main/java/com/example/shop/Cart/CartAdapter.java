package com.example.shop.Cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shop.Product.ProductDetailActivity;
import com.example.shop.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private boolean showRemoveButton;

    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, boolean showRemoveButton) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showRemoveButton = showRemoveButton;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()){
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new CartItemViewholder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout, parent, false);
                return new CartTotalAmountViewholder(cartTotalView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                String productId = cartItemModelList.get(position).getProductId();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                Long freeCoupons = cartItemModelList.get(position).getFreeCoupons();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String discountPrice = cartItemModelList.get(position).getDiscountPrice();
                Long offersApplied = cartItemModelList.get(position).getOffersApplied();
                boolean inStock = cartItemModelList.get(position).isInStock();

                ((CartItemViewholder)holder).setItemDetails(productId, resource, title, freeCoupons, productPrice, discountPrice, offersApplied, position, inStock);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItem = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int finalTotalAmount;
                int savedAmount = 0;

                for(int i=0; i<cartItemModelList.size(); i++){
                    if(cartItemModelList.get(i).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(i).isInStock()){
                        totalItem++;
                        totalItemPrice += Integer.parseInt(cartItemModelList.get(i).getProductPrice());
                    }
                }
                if(totalItemPrice > 500){
                    deliveryPrice = "Free";
                    finalTotalAmount = totalItemPrice;
                }
                else{
                    deliveryPrice = "5";
                    finalTotalAmount = totalItemPrice + 5;
                }
//                int totalItems = Integer.parseInt(cartItemModelList.get(position).getTotalItems());
                cartItemModelList.get(position).setTotalItems(String.valueOf(totalItem));
                cartItemModelList.get(position).setTotalAmount(String.valueOf(totalItemPrice));
                cartItemModelList.get(position).setDeliveryPrice(deliveryPrice);
                cartItemModelList.get(position).setFinalTotalAmount(String.valueOf(finalTotalAmount));
                cartItemModelList.get(position).setSavedAmount(String.valueOf(savedAmount));
                ((CartTotalAmountViewholder)holder).setTotalAmount(totalItem, totalItemPrice, deliveryPrice, finalTotalAmount, savedAmount);
                break;
            default:
                return;

        }
        if(lastPosition < position){
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.nav_default_exit_anim);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewholder extends RecyclerView.ViewHolder{
        private ImageView productImage;
        private ImageView freeCouponIcon;
        private TextView productTitle;
        private TextView freeCoupons;
        private TextView productPrice;
        private TextView discountPrice;
        private TextView offersApplied;
        private TextView couponsApplied;
        private TextView productQuantity;
        private LinearLayout removeBtn;

        public CartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image_cart);
            freeCouponIcon = itemView.findViewById(R.id.coupon_icon);
            productTitle = itemView.findViewById(R.id.product_title_cart);
            freeCoupons = itemView.findViewById(R.id.coupon_text);
            productPrice = itemView.findViewById(R.id.product_price_cart);
            discountPrice = itemView.findViewById(R.id.discount_price_cart);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            couponsApplied = itemView.findViewById(R.id.coupon_applied_cart);
            productQuantity = itemView.findViewById(R.id.product_quantity_cart);
            removeBtn = itemView.findViewById(R.id.remove_item_cart_btn);
        }

        private void setItemDetails(String productId, String resource, String title, Long countFreeCoupons, String productPriceText, String discountProductPriceText, Long countOffersApplied, int position, boolean inStock){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_home)).into(productImage);
            productTitle.setText(title);
            if (discountProductPriceText.equals("0")){
                discountPrice.setVisibility(View.GONE);
            }
            if(countFreeCoupons > 0){
                freeCoupons.setVisibility(View.VISIBLE);
                freeCouponIcon.setVisibility(View.VISIBLE);
                if(countFreeCoupons == 1){
                    freeCoupons.setText("free " + countFreeCoupons + " coupon");
                }
                else{
                    freeCoupons.setText("free " + countFreeCoupons + " coupons");
                }
            }
            else{
                freeCoupons.setVisibility(View.INVISIBLE);
                freeCouponIcon.setVisibility(View.INVISIBLE);
            }

            if (inStock) {
                productPrice.setText(productPriceText + " ₽");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                discountPrice.setText(discountProductPriceText + " ₽");
            } else {
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.grey));
                discountPrice.setText("");
            }

            if(countOffersApplied > 0){
                offersApplied.setVisibility(View.VISIBLE);
                offersApplied.setText("Товар в наличии");
            }
            else{
                offersApplied.setVisibility(View.INVISIBLE);
            }

            if(showRemoveButton){
                removeBtn.setVisibility(View.VISIBLE);
            }
            else {
                removeBtn.setVisibility(View.GONE);
            }
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ProductDetailActivity.is_cart_query){
                        ProductDetailActivity.is_cart_query = true;
                        Cart.removeItemCart(position);
//                        if(Cart.cartItemModelList.size() == 0) {
                            cartTotalAmount.setText(0 + " ₽");
//                        }
                    }
                }
            });

        }
    }

    class CartTotalAmountViewholder extends RecyclerView.ViewHolder{

        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView savedAmount;

        public CartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);
            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_charge_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);
        }

        private void setTotalAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText, int savedAmountText){
            totalItems.setText("Стоимость: " + totalItemText);
            totalItemPrice.setText(totalItemPriceText + " ₽");
            if(deliveryPriceText.equals("Free")){
                deliveryPrice.setText(deliveryPriceText);
            }
            else {
                deliveryPrice.setText(deliveryPriceText + " ₽");
            }
            totalAmount.setText(totalAmountText + " ₽");
            cartTotalAmount.setText(totalAmountText + " ₽");
            savedAmount.setText("Вы сэкономили " + savedAmountText + " ₽");
            if (totalItemPriceText == 0){
                Cart.cartItemModelList.remove(Cart.cartItemModelList.size()-1);
            }else{
                ;
            }
        }
    }
}
