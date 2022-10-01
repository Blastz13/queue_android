package com.example.shop.Category;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shop.Product.ProductDetailActivity;
import com.example.shop.R;

import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {

    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.ViewHolder holder, int position) {
        String resource = horizontalProductScrollModelList.get(position).getProductImage();
        String title = horizontalProductScrollModelList.get(position).getProductTitle();
        String price = horizontalProductScrollModelList.get(position).getProductPrice();
        String productId = horizontalProductScrollModelList.get(position).getProductId();
        holder.setProductImage(resource);
        holder.setProductTitle(title);
        holder.setProductPrice(price);
        holder.setProductId(productId);
    }

    @Override
    public int getItemCount() {
        return horizontalProductScrollModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private TextView productTitle;
        private TextView productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.horizontal_scroll_product_image);
            productTitle = itemView.findViewById(R.id.horizontal_scroll_product_title);
            productPrice = itemView.findViewById(R.id.horizontal_scroll_product_price);
        }

        public void setProductImage(String resource) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_home)).into(productImage);
        }

        public void setProductTitle(String title) {
            productTitle.setText(title);
        }

        public void setProductPrice(String price) {
            productPrice.setText(price + " ₽");
        }

        public void setProductId(String productId) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailIntent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                    productDetailIntent.putExtra("product_id", productId);
                    itemView.getContext().startActivity(productDetailIntent);
                }
            });
        }
    }
}
