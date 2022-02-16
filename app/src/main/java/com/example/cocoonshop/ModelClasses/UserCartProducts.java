package com.example.cocoonshop.ModelClasses;

import java.util.List;

public class UserCartProducts {

    private String productName ,productImagePath , key;
    private int productCategory , productNewPrice , productTotalPrice , productTotalQuantity;

    public UserCartProducts() {}


    public UserCartProducts(String productName, String productImagePath, String key, int productCategory, int productNewPrice, int productTotalPrice, int productTotalQuantity) {
        this.productName = productName;
        this.productImagePath = productImagePath;
        this.key = key;
        this.productCategory = productCategory;
        this.productNewPrice = productNewPrice;
        this.productTotalPrice = productTotalPrice;
        this.productTotalQuantity = productTotalQuantity;
    }

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

    public int getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(int productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public int getProductTotalQuantity() {
        return productTotalQuantity;
    }

    public void setProductTotalQuantity(int productTotalQuantity) {
        this.productTotalQuantity = productTotalQuantity;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
