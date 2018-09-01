package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllProductList {

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


    @SerializedName("product_id")
    @Expose
    private String product_id;

    @SerializedName("tid")
    @Expose
    private String tid;
    @SerializedName("buy_rate")
    @Expose
    private String buy_rate;
    @SerializedName("approx_qty")
    @Expose
    private String approx_qty;
    @SerializedName("addStockQty")
    @Expose
    private String addStockQty;
    @SerializedName("approve")
    @Expose
    private String approve;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    private boolean isChecked;

    public String getBuy_rate() {
        return buy_rate;
    }

    public void setBuy_rate(String buy_rate) {
        this.buy_rate = buy_rate;
    }

    public String getApprox_qty() {
        return approx_qty;
    }

    public void setApprox_qty(String approx_qty) {
        this.approx_qty = approx_qty;
    }

    public String getAddStockQty() {
        return addStockQty;
    }

    public void setAddStockQty(String addStockQty) {
        this.addStockQty = addStockQty;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}