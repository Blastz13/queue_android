package com.example.shop.Order;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import com.bumptech.glide.Glide;
import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Viewholder> {

    private List<OrderItemModel> orderItemModelList;

    public OrderAdapter(List<OrderItemModel> orderItemModelList) {
        this.orderItemModelList = orderItemModelList;
    }

    @NonNull
    @Override
    public OrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.Viewholder holder, int position) {
        String resource = orderItemModelList.get(position).getProductImage();
        String productId = orderItemModelList.get(position).getProductId();
        int rating = orderItemModelList.get(position).getRating();
        String title = orderItemModelList.get(position).getProductTitle();
        String orderStatus = orderItemModelList.get(position).getDeliveryStatus();
        Date date;
        switch (orderStatus){
            case "Ordered":
                date = orderItemModelList.get(position).getOrderedDate();
                break;
            case "Packed":
                date = orderItemModelList.get(position).getPackedDate();
                break;
            case "Shipped":
                date = orderItemModelList.get(position).getShippedDate();
                break;
            case "Delivered":
                date = orderItemModelList.get(position).getDeliveredDate();
                break;
            case "Cancelled":
                date = orderItemModelList.get(position).getCancelledDate();
                break;
            default:
                date = orderItemModelList.get(position).getCancelledDate();
                break;

        }
        String deliveredDate = orderItemModelList.get(position).getDeliveryStatus();
        holder.setData(resource, title, orderStatus, date, position, rating, productId);

    }

    @Override
    public int getItemCount() {
        return orderItemModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private ImageView deliveryIndicator;
        private TextView productTitle;
        private TextView deliveryStatus;
        private LinearLayout rateNowLayoutContainer;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.order_product_image);
            productTitle = itemView.findViewById(R.id.order_product_title);
            deliveryIndicator = itemView.findViewById(R.id.indicator_status_order);
            deliveryStatus = itemView.findViewById(R.id.delivared_order_date);
            rateNowLayoutContainer = itemView.findViewById(R.id.rate_now_container);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent orderDetailIntent = new Intent(itemView.getContext(), OrderDetailActivity.class);
//                    orderDetailIntent.putExtra("position", position);
//                    itemView.getContext().startActivity(orderDetailIntent);
//                }
//            });
        }
        private void setData(String resource, String title, String orderStatus, Date date, int position, int rating, String productId){
            Glide.with(itemView.getContext()).load(resource).into(productImage);
            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                    .collection("USER_DATA").document("RATINGS").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                for(long i=0; i < Long.parseLong(task.getResult().get("size_list").toString()); i++){
                                    if(task.getResult().get("product_id_"+i).toString().equals(productId)){
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
            productTitle.setText(title);
            if(orderStatus.equals("Cancelled")) {
                deliveryIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.red)));
            }
            else{
                deliveryIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.green)));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderDetailIntent = new Intent(itemView.getContext(), OrderDetailActivity.class);
                    orderDetailIntent.putExtra("position", position);
                    orderDetailIntent.putExtra("rating", rating);
                    itemView.getContext().startActivity(orderDetailIntent);
                }
            });

//            setRating(rating);
//            for(int i = 0; i < rateNowLayoutContainer.getChildCount(); i++){
//                final int starPosition = i;
//                rateNowLayoutContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        setRating(starPosition);
//                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("PRODUCTS").document(productId);
//                        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
//                            @Nullable
//                            @Override
//                            public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                                DocumentSnapshot documentSnapshot = transaction.get(documentReference);
//                                if(rating!=0){
//                                    Long increase = documentSnapshot.getLong("mark_"+(starPosition+1))+1;
//                                    Long decrease = documentSnapshot.getLong("mark_"+(rating+1))-1;
//                                    transaction.update(documentReference, "mark_"+(starPosition+1), increase);
//                                    transaction.update(documentReference, "mark_"+(rating+1), decrease);
//                                }else{
//                                    Long increase = documentSnapshot.getLong("mark_"+(starPosition+1))+1;
//                                    transaction.update(documentReference, "mark_"+(starPosition+1), increase);
//
//                                }
//                                return null;
//                            }
//                        }).addOnSuccessListener(new OnSuccessListener<Object>() {
//                            @Override
//                            public void onSuccess(Object o) {
//                                Order.orderItemModelList.get(position).setRating(starPosition+1);
//                                if(Rating.RatedId.contains(productId)){
//                                    Rating.Rating.set(Rating.RatedId.indexOf(productId), Long.parseLong(String.valueOf(starPosition+1)));
//                                }else{
//                                    Rating.RatedId.add(productId);
//                                    Rating.Rating.add(Long.parseLong(String.valueOf(starPosition+1)));
//                                }
//                            }
//                        });
//                    }
//                });
//            }

            deliveryStatus.setText(orderStatus + " " +String.valueOf(date));
        }
        private void setRating(int starPosition){
            for(int i = 0; i < rateNowLayoutContainer.getChildCount(); i++){
                ImageView starBtn = (ImageView) rateNowLayoutContainer.getChildAt(i);
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                if(i<= starPosition){
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
                }
            }
        }
    }
}
