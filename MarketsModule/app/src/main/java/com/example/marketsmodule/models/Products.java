package com.example.marketsmodule.models;

public class Products {

    private String productBarcodeNo;
    private String productName;
    private String productUnit;
    private String productCategory;
    private String productQuantity;
    private String productSalePrice;
    private String productComePrice;
    private String productImageUrl=null;
    private int orderQuantity;

    @Override
    public String toString() {
        return "Products{" +
                "productBarcodeNo='" + productBarcodeNo + '\'' +
                ", productName='" + productName + '\'' +
                ", productUnit='" + productUnit + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", productQuantity='" + productQuantity + '\'' +
                ", productSalePrice='" + productSalePrice + '\'' +
                ", productComePrice='" + productComePrice + '\'' +
                ", productImageUrl='" + productImageUrl + '\'' +
                ", orderQuantity=" + orderQuantity +
                '}';
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Products(String productBarcodeNo, String productName, String productUnit, String productCategory, String productQuantity, String productSalePrice, String productComePrice, String productImageUrl) {
        this.productBarcodeNo = productBarcodeNo;
        this.productName = productName;
        this.productUnit = productUnit;
        this.productCategory = productCategory;
        this.productQuantity = productQuantity;
        this.productSalePrice = productSalePrice;
        this.productComePrice = productComePrice;
        this.productImageUrl=productImageUrl;
    }

    public Products() {
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }


    public String getProductBarcodeNo() {
        return productBarcodeNo;
    }

    public void setProductBarcodeNo(String productBarcodeNo) {
        this.productBarcodeNo = productBarcodeNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductSalePrice() {
        return productSalePrice;
    }

    public void setProductSalePrice(String productSalePrice) {
        this.productSalePrice = productSalePrice;
    }

    public String getProductComePrice() {
        return productComePrice;
    }

    public void setProductComePrice(String productComePrice) {
        this.productComePrice = productComePrice;
    }
}
