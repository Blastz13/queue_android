package com.example.shop.Category;

public class CategoryModel {

    private String categoryIco;
    private String categoryTitle;

    public CategoryModel(String categoryIco, String categoryTitle) {
        this.categoryIco = categoryIco;
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryIco() {
        return categoryIco;
    }

    public void setCategoryIco(String categoryIco) {
        this.categoryIco = categoryIco;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
