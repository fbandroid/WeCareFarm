package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CollectedOrder {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    @Expose
    private String id;


    public ArrayList<TodayAgentDelivery> getTodayAgentDeliveries() {
        return todayAgentDeliveries;
    }

    public void setTodayAgentDeliveries(ArrayList<TodayAgentDelivery> todayAgentDeliveries) {
        this.todayAgentDeliveries = todayAgentDeliveries;
    }

    @SerializedName("collected_order")
    @Expose
    private ArrayList<TodayAgentDelivery> todayAgentDeliveries;
}
