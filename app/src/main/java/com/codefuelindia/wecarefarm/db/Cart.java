package com.codefuelindia.wecarefarm.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "cart")
public class Cart {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "pid")
    @NonNull
    private String pid = "";
    @ColumnInfo(name = "cid")
    @NonNull
    private String cid = "";
    @ColumnInfo(name = "orderDate")
    @NonNull
    private String orderDate = "";
    @ColumnInfo(name = "status")
    private int status;




    @Ignore
    @ColumnInfo(name = "totalQty")
    private double totalQty;


    public double getAvlQty() {
        return avlQty;
    }

    public void setAvlQty(double avlQty) {
        this.avlQty = avlQty;
    }

    @ColumnInfo(name = "avl_qty")
    private double avlQty = 0.0;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    @ColumnInfo(name = "gid")
    private String gid;


    @NonNull
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(@NonNull String unitName) {
        this.unitName = unitName;
    }

    @ColumnInfo(name = "unitName")
    @NonNull
    private String unitName;


    @NonNull
    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(@NonNull double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @ColumnInfo(name = "unitPrice")
    @NonNull
    private double unitPrice;


    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    @ColumnInfo(name = "qty")
    @NonNull
    private double qty;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @ColumnInfo(name = "pr_name")
    private String productName = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
