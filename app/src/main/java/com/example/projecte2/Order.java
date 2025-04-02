package com.example.projecte2;

public class Order {
    private String orderId;
    private String date;
    private String amount;

    public Order(String orderId, String date, String amount) {
        this.orderId = orderId;
        this.date = date;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }
}