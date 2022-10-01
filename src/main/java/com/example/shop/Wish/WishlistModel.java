package com.example.shop.Wish;

import java.util.ArrayList;

public class WishlistModel {
    private String productId;
    private String productImage;
    private long freeCoupons;
    private long totalRating;
    private String productTitle;
    private String rating;
    private String productPrice;
    private String productDiscountPrice;
    private ArrayList<String> tags;

    public WishlistModel(String productId, String productImage, long freeCoupons, long totalRating, String productTitle, String rating, String productPrice, String productDiscountPrice) {
        this.productId = productId;
        this.productImage = productImage;
        this.freeCoupons = freeCoupons;
        this.totalRating = totalRating;
        this.productTitle = productTitle;
        this.rating = rating;
        this.productPrice = productPrice;
        this.productDiscountPrice = productDiscountPrice;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
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

    public long getFreeCoupons() {
        return freeCoupons;
    }

    public void setFreeCoupons(long freeCoupons) {
        this.freeCoupons = freeCoupons;
    }

    public long getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(long totalRating) {
        this.totalRating = totalRating;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDiscountPrice() {
        return productDiscountPrice;
    }

    public void setProductDiscountPrice(String productDiscountPrice) {
        this.productDiscountPrice = productDiscountPrice;
    }
}
