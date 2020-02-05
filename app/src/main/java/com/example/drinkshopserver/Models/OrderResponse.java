package com.example.drinkshopserver.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponse {

    @SerializedName("error")
    private String message;
    @SerializedName("Orders")
    private List<Order> orders;

    public OrderResponse(String message, List<Order> orders) {
        this.message = message;
        this.orders = orders;
    }

    public OrderResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
