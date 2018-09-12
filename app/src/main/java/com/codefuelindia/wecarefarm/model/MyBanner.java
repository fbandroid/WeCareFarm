package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyBanner {

    @SerializedName("banner")
    @Expose
    private List<Banner> banner = null;

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

}