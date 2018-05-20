package com.dev.test.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int productId;

    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "product_image_url")
    private String productImageUrl;

    @ColumnInfo(name = "product_description")
    private String productDescription;

    @ColumnInfo(name = "product_location")
    private String productLocation;

    @ColumnInfo(name = "product_price")
    private String productCost;

    public Product(String productName, String productImageUrl, String productDescription, String productLocation, String productCost) {
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productDescription = productDescription;
        this.productLocation = productLocation;
        this.productCost = productCost;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }

    public String getProductCost() {
        return productCost;
    }

    public void setProductCost(String productCost) {
        this.productCost = productCost;
    }
}
