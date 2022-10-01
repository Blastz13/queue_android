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
import com.example.shop.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryModel> categoryModelList;

    public CategoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        String ico = categoryModelList.get(position).getCategoryIco();
        String title = categoryModelList.get(position).getCategoryTitle();
        holder.setCategoryIco(ico);
        holder.setCategory(title);
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView categoryIco;
        private TextView categoryTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIco = itemView.findViewById(R.id.category_ico);
            categoryTitle = itemView.findViewById(R.id.category_title);
        }

        private void setCategoryIco(String url){
            if(!url.equals("null")) {
                Glide.with(itemView.getContext()).load(url).apply(new RequestOptions().placeholder(R.drawable.ic_home)).into(categoryIco);
            }
        }

        private void setCategory(String title){
            categoryTitle.setText(title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent categoryIntent = new Intent(itemView.getContext(), CategoryActivity.class);
                    categoryIntent.putExtra("CategoryTitle", categoryTitle.getText());
                    itemView.getContext().startActivity(categoryIntent);
                }
            });
        }
    }
}
