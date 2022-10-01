package com.example.shop.Order;

import java.util.Date;

public class OrderItemModel {
    private String productId;
    private String productImage;
    private String productTitle;
    private String deliveryStatus;
    private String address;
    private String productPrice;
    private String discountPrice;
    private Date orderedDate;
    private Date packedDate;
    private Date shippedDate;
    private Date deliveredDate;
    private Date cancelledDate;
    private String orderId;
    private String name;
    private String index;
    private String userId;
    private Long productQuantity;
    private int rating = 0;
    private String deliveryPrice;


    public OrderItemModel(String productId, String productImage, String productTitle, String deliveryStatus, String address, String productPrice, String discountPrice, Date orderedDate, Date packedDate, Date shippedDate, Date deliveredDate, Date cancelledDate, String orderId, String name, String index, String userId, Long productQuantity, String deliveryPrice) {
        this.productId = productId;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.deliveryStatus = deliveryStatus;
        this.address = address;
        this.productPrice = productPrice;
        this.discountPrice = discountPrice;
        this.orderedDate = orderedDate;
        this.packedDate = packedDate;
        this.shippedDate = shippedDate;
        this.deliveredDate = deliveredDate;
        this.cancelledDate = cancelledDate;
        this.orderId = orderId;
        this.name = name;
        this.index = index;
        this.userId = userId;
        this.productQuantity = productQuantity;
        this.deliveryPrice = deliveryPrice;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Date getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
    }

    public Date getPackedDate() {
        return packedDate;
    }

    public void setPackedDate(Date packedDate) {
        this.packedDate = packedDate;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public Date getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }
}
