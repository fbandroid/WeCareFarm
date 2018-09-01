package com.codefuelindia.wecarefarm.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderList {

@SerializedName("order")
@Expose
private List<Order> order = null;

public List<Order> getOrder() {
return order;
}

public void setOrder(List<Order> order) {
this.order = order;
}

}