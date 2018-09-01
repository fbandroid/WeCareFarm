package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowTransactionModel {

    @SerializedName("remaining")
    @Expose
    private String remaining;

    @SerializedName("available")
    @Expose
    private String available;

    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("recieved")
    @Expose
    private String recieved;

    @SerializedName("order_no")
    @Expose
    private String order_no;


    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRecieved() {
        return recieved;
    }

    public void setRecieved(String recieved) {
        this.recieved = recieved;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }


}
