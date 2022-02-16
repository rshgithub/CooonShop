package com.example.cocoonshop.ModelClasses.RecyclerModels;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class ProductItem {

    private String productName, productImagePath, productDescription;
    private int productCategory, productNewPrice, productOldPrice;
    private boolean isBestSell;
    private String key;

    //-------------------------------------------------------------------------------------------------------------------------


    public ProductItem(String key ,String productName , String productImagePath, String productDescription, int productCategory, int productNewPrice, int productOldPrice, boolean isBestSell) {
        this.productName = productName;
        this.productImagePath = productImagePath;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.productNewPrice = productNewPrice;
        this.productOldPrice = productOldPrice;
        this.isBestSell = isBestSell;
        this.key = key;
    }

    public ProductItem(String key, String productName, String productImagePath, int productCategory, int productNewPrice) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.productNewPrice = productNewPrice;
        this.productImagePath = productImagePath;
        this.key=key;
    }

    public ProductItem() {
    }
    //-------------------------------------------------------------------------------------------------------------------------


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImagePath() {
        return productImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
    }

    public int getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(int productCategory) {
        this.productCategory = productCategory;
    }

    public int getProductNewPrice() {
        return productNewPrice;
    }

    public void setProductNewPrice(int productNewPrice) {
        this.productNewPrice = productNewPrice;
    }

    public int getProductOldPrice() {
        return productOldPrice;
    }

    public void setProductOldPrice(int productOldPrice) {
        this.productOldPrice = productOldPrice;
    }

    public boolean isBestSell() {
        return isBestSell;
    }

    public void setBestSell(boolean bestSell) {
        isBestSell = bestSell;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
