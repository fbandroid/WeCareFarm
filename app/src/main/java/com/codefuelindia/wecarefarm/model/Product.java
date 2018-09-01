package com.codefuelindia.wecarefarm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product implements Parcelable {

    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("product_img")
    @Expose
    private String productImg;
    @SerializedName("thumb_img")
    @Expose
    private String thumbImg;
    @SerializedName("unit_price")
    @Expose
    private String unitPrice;
    @SerializedName("unit_name")
    @Expose
    private String unitName;
    @SerializedName("unit_val")
    @Expose
    private String unitVal;
    @SerializedName("f_id")
    @Expose
    private String fId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("category")
    @Expose
    private String category;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    @SerializedName("gid")
    @Expose
    private String gid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getThumbImg() {
        return thumbImg;
    }

    public void setThumbImg(String thumbImg) {
        this.thumbImg = thumbImg;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitVal() {
        return unitVal;
    }

    public void setUnitVal(String unitVal) {
        this.unitVal = unitVal;
    }

    public String getFId() {
        return fId;
    }

    public void setFId(String fId) {
        this.fId = fId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pid);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.shortDescription);
        dest.writeString(this.qty);
        dest.writeString(this.productImg);
        dest.writeString(this.thumbImg);
        dest.writeString(this.unitPrice);
        dest.writeString(this.unitName);
        dest.writeString(this.unitVal);
        dest.writeString(this.fId);
        dest.writeString(this.status);
        dest.writeString(this.timestamp);
        dest.writeString(this.category);
    }

    public Product() {
    }

    protected Product(Parcel in) {
        this.pid = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.shortDescription = in.readString();
        this.qty = in.readString();
        this.productImg = in.readString();
        this.thumbImg = in.readString();
        this.unitPrice = in.readString();
        this.unitName = in.readString();
        this.unitVal = in.readString();
        this.fId = in.readString();
        this.status = in.readString();
        this.timestamp = in.readString();
        this.category = in.readString();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}