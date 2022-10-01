package com.example.shop.Product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop.R;

import java.util.List;

public class ProductSpecificationAdapter extends RecyclerView.Adapter<ProductSpecificationAdapter.ViewHolder> {

    private List<ProductSpecificationModel> productSpecificationModelList;

    public ProductSpecificationAdapter(List<ProductSpecificationModel> productSpecificationModelList) {
        this.productSpecificationModelList = productSpecificationModelList;
    }

    @NonNull
    @Override
    public ProductSpecificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_specification_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSpecificationAdapter.ViewHolder holder, int position) {
        String specificationTitle = productSpecificationModelList.get(position).getSpecificationName();
        String specificationValues = productSpecificationModelList.get(position).getSpecificationValue();
        holder.setSpecification(specificationTitle, specificationValues);
    }

    @Override
    public int getItemCount() {
        return productSpecificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView specificationName;
        private TextView specificationValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            specificationName =itemView.findViewById(R.id.spec_name);
            specificationValue = itemView.findViewById(R.id.spec_value);
        }

        private void setSpecification(String specificationTitle, String specificationValues){
            specificationName.setText(specificationTitle);
            specificationValue.setText(specificationValues);
        }
    }
}
