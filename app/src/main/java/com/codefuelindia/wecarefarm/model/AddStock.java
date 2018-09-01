package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddStock {

    public ArrayList<AllProductList> getAllProductLists() {
        return allProductLists;
    }

    public void setAllProductLists(ArrayList<AllProductList> allProductLists) {
        this.allProductLists = allProductLists;
    }

    @SerializedName("stock")
    @Expose
    private ArrayList<AllProductList> allProductLists;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    @SerializedName("fid")
    @Expose
    private String fid;

}
