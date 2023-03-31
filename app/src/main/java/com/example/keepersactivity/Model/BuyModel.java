package com.example.keepersactivity.Model;

public class BuyModel {

    public String productName, productQuantity, productUnit, productType, productPrice,profit;

    public BuyModel() {
    }

    public BuyModel(String productName, String productQuantity, String productUnit, String productType, String productPrice, String profit) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productUnit = productUnit;
        this.productType = productType;
        this.productPrice = productPrice;
        this.profit = profit;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }
}
