package com.example.drinkshopserver.Models;

import com.google.gson.annotations.SerializedName;

public class OrderResult {

    @SerializedName("OrderId")
    private String OrderId;

    @SerializedName("OrderDate")
    private String Date;

    @SerializedName("Status")
    private String Status;

    @SerializedName("Detail")
    private String Detail ;

    @SerializedName("Price")
    private String Price ;

    @SerializedName("Comment")
    private String Comment;

    @SerializedName("Address")
    private String Address;

    @SerializedName("Phone")
    private String Phone;

    public OrderResult(String OrderId, String status, String detail, String price, String comment, String address, String phone) {
        this.OrderId = OrderId;
        Status = status;
        Detail = detail;
        Price = price;
        Comment = comment;
        Address = address;
        Phone = phone;
    }

    public String getOrderid() {
        return OrderId;
    }

    public String getStatus() {
        return Status;
    }

    public String getDetail() {
        return Detail;
    }

    public String getPrice() {
        return Price;
    }

    public String getComment() {
        return Comment;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhone() {
        return Phone;
    }


}



