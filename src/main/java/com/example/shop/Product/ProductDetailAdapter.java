package com.example.shop.Product;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProductDetailAdapter extends FragmentPagerAdapter {

    private int totalTabs;

    public ProductDetailAdapter(@NonNull FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //TODO: FIX display fragment tablayout
        switch (position){
            case 0:
                ProductDescriptionFragment productDescriptionFragment = new ProductDescriptionFragment();
                return productDescriptionFragment;
            case 1:
                ProductSpecificationFragment productSpecificationFragment = new ProductSpecificationFragment();
                return productSpecificationFragment;
            case 2:
                ProductDescriptionFragment productDescriptionFragment1 = new ProductDescriptionFragment();
                return productDescriptionFragment1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
