package com.codefuelindia.wecarefarm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyOrder implements Parcelable {

    public static final Parcelable.Creator<MyOrder> CREATOR = new Parcelable.Creator<MyOrder>() {
        @Override
        public MyOrder createFromParcel(Parcel source) {
            return new MyOrder(source);
        }

        @Override
        public MyOrder[] newArray(int size) {
            return new MyOrder[size];
        }
    };
    @SerializedName("tid")
    @Expose
    private String tid;
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("fid")
    @Expose
    private String fid;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("shipping_date")
    @Expose
    private String shippingDate;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_type")
    @Expose
    private String orderType;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("grand_total")
    @Expose
    private String grand_total;
    @SerializedName("order_no")
    @Expose
    private String order_no;
    @SerializedName("name")
    @Expose
    private String name;

    public MyOrder() {
    }

    protected MyOrder(Parcel in) {
        this.tid = in.readString();
        this.pid = in.readString();
        this.cid = in.readString();
        this.fid = in.readString();
        this.qty = in.readString();
        this.amount = in.readString();
        this.shippingDate = in.readString();
        this.orderId = in.readString();
        this.orderType = in.readString();
        this.timestamp = in.readString();
        this.id = in.readString();
        this.status = in.readString();
        this.grand_total = in.readString();
        this.order_no = in.readString();
        this.name = in.readString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(String shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tid);
        dest.writeString(this.pid);
        dest.writeString(this.cid);
        dest.writeString(this.fid);
        dest.writeString(this.qty);
        dest.writeString(this.amount);
        dest.writeString(this.shippingDate);
        dest.writeString(this.orderId);
        dest.writeString(this.orderType);
        dest.writeString(this.timestamp);
        dest.writeString(this.id);
        dest.writeString(this.status);
        dest.writeString(this.grand_total);
        dest.writeString(this.order_no);
        dest.writeString(this.name);
    }
}