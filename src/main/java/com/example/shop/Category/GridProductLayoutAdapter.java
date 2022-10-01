package com.example.shop.Category;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shop.Product.ProductDetailActivity;
import com.example.shop.R;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {

    List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public GridProductLayoutAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @Override
    public int getCount() {
        return horizontalProductScrollModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout, null);
            view.setElevation(0);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailIntent = new Intent(parent.getContext(), ProductDetailActivity.class);
                    productDetailIntent.putExtra("product_id", horizontalProductScrollModelList.get(position).getProductId());
                    parent.getContext().startActivity(productDetailIntent);
                }
            });
            ImageView productImage = view.findViewById(R.id.horizontal_scroll_product_image);
            TextView productTitle = view.findViewById(R.id.horizontal_scroll_product_title);
            TextView productPrice = view.findViewById(R.id.horizontal_scroll_product_price);

            Glide.with(parent.getContext()).load(horizontalProductScrollModelList.get(position).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.ic_home)).into(productImage);
            productTitle.setText(horizontalProductScrollModelList.get(position).getProductTitle());
            productPrice.setText(horizontalProductScrollModelList.get(position).getProductPrice() + " ₽");
        }
        else{
            view = convertView;
        }
        return view;
    }
}
