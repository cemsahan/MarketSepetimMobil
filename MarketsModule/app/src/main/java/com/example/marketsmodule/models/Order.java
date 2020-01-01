package com.example.marketsmodule.models;

import java.util.List;

public class Order {
    private String orderKey;
    private Customer customerInfo;
    private String customerKey;
    private String date;
    private String situation;
    private String statement;
    private List<Products> orders;


    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public Order(Customer customerInfo, String customerKey, String date, String situation, String statement, List<Products> orders) {
        this.customerInfo = customerInfo;
        this.customerKey = customerKey;
        this.date = date;
        this.situation = situation;
        this.statement = statement;
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderKey='" + orderKey + '\'' +
                ", customerInfo=" + customerInfo +
                ", customerKey='" + customerKey + '\'' +
                ", date='" + date + '\'' +
                ", situation='" + situation + '\'' +
                ", statement='" + statement + '\'' +
                ", orders=" + orders +
                '}';
    }

    public Customer getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(Customer customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public List<Products> getOrders() {
        return orders;
    }

    public void setOrders(List<Products> orders) {
        this.orders = orders;
    }

    public Order() {
    }
}