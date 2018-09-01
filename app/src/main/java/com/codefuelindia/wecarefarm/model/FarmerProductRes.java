package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerProductRes {

    @SerializedName("tid")
    @Expose
    private String tid;

    @SerializedName("product_id")
    @Expose
    private String productId;

    @SerializedName("g_id")
    @Expose
    private String gId;

    @SerializedName("approve")
    @Expose
    private String approve;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("transaction_qty")
    @Expose
    private String transactionQty;

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

    @SerializedName("main_qty")
    @Expose
    private String mainQty;

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

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getGId() {
        return gId;
    }

    public void setGId(String gId) {
        this.gId = gId;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTransactionQty() {
        return transactionQty;
    }

    public void setTransactionQty(String transactionQty) {
        this.transactionQty = transactionQty;
    }

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

    public String getMainQty() {
        return mainQty;
    }

    public void setMainQty(String mainQty) {
        this.mainQty = mainQty;
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


}
