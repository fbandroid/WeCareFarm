package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerTransRes {

    @SerializedName("approve_price")
    @Expose
    private String approvePrice;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("qty")
    @Expose
    private String qty;

    public String getApprovePrice() {
        return approvePrice;
    }

    public void setApprovePrice(String approvePrice) {
        this.approvePrice = approvePrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
