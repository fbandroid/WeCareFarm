package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayCollectionModel {

@SerializedName("id")
@Expose
private String id;
@SerializedName("cid")
@Expose
private String cid;
@SerializedName("agent_id")
@Expose
private String agentId;
@SerializedName("grand_total")
@Expose
private String grandTotal;
@SerializedName("status")
@Expose
private String status;
@SerializedName("order_no")
@Expose
private String orderNo;
@SerializedName("timestamp")
@Expose
private String timestamp;
@SerializedName("remarks")
@Expose
private String remarks;
@SerializedName("cancelled_by")
@Expose
private String cancelledBy;
@SerializedName("cancel_byid")
@Expose
private String cancelByid;
@SerializedName("shipping_date")
@Expose
private String shippingDate;
@SerializedName("received_by_payment")
@Expose
private String receivedByPayment;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getCid() {
return cid;
}

public void setCid(String cid) {
this.cid = cid;
}

public String getAgentId() {
return agentId;
}

public void setAgentId(String agentId) {
this.agentId = agentId;
}

public String getGrandTotal() {
return grandTotal;
}

public void setGrandTotal(String grandTotal) {
this.grandTotal = grandTotal;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getOrderNo() {
return orderNo;
}

public void setOrderNo(String orderNo) {
this.orderNo = orderNo;
}

public String getTimestamp() {
return timestamp;
}

public void setTimestamp(String timestamp) {
this.timestamp = timestamp;
}

public String getRemarks() {
return remarks;
}

public void setRemarks(String remarks) {
this.remarks = remarks;
}

public String getCancelledBy() {
return cancelledBy;
}

public void setCancelledBy(String cancelledBy) {
this.cancelledBy = cancelledBy;
}

public String getCancelByid() {
return cancelByid;
}

public void setCancelByid(String cancelByid) {
this.cancelByid = cancelByid;
}

public String getShippingDate() {
return shippingDate;
}

public void setShippingDate(String shippingDate) {
this.shippingDate = shippingDate;
}

public String getReceivedByPayment() {
return receivedByPayment;
}

public void setReceivedByPayment(String receivedByPayment) {
this.receivedByPayment = receivedByPayment;
}

}