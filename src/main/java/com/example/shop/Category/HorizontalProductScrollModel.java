package com.example.shop.Category;

public class HorizontalProductScrollModel {
    private String productId;
    private String productImage;
    private String productTitle;
    private String productPrice;

    public HorizontalProductScrollModel(String productId, String productImage, String productTitle, String productPrice) {
        this.productId = productId;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
