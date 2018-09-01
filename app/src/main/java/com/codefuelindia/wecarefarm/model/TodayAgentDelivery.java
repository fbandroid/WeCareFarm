package com.codefuelindia.wecarefarm.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.CheckBox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayAgentDelivery implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("pin")
    @Expose
    private String pin;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("profile_img")
    @Expose
    private String profileImg;
    @SerializedName("farmer_shop_name")
    @Expose
    private String farmerShopName;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("house_no")
    @Expose
    private String houseNo;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("society_name")
    @Expose
    private String societyName;
    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("agent_id")
    @Expose
    private String agentId;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;
    @SerializedName("order_no")
    @Expose
    private String orderNo;

    public String getReceived_amount() {
        return received_amount;
    }

    public void setReceived_amount(String received_amount) {
        this.received_amount = received_amount;
    }

    @SerializedName("received_amount")
    private String received_amount;


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked;

    public String getReceivedByPayment() {
        return receivedByPayment;
    }

    public void setReceivedByPayment(String receivedByPayment) {
        this.receivedByPayment = receivedByPayment;
    }

    @SerializedName("received_by_payment")
    @Expose
    private String receivedByPayment;

    public TodayAgentDelivery() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getFarmerShopName() {
        return farmerShopName;
    }

    public void setFarmerShopName(String farmerShopName) {
        this.farmerShopName = farmerShopName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeString(this.password);
        dest.writeString(this.gender);
        dest.writeString(this.dob);
        dest.writeString(this.email);
        dest.writeString(this.pin);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.district);
        dest.writeString(this.profileImg);
        dest.writeString(this.farmerShopName);
        dest.writeString(this.role);
        dest.writeString(this.status);
        dest.writeString(this.timestamp);
        dest.writeString(this.houseNo);
        dest.writeString(this.landmark);
        dest.writeString(this.societyName);
        dest.writeString(this.cid);
        dest.writeString(this.agentId);
        dest.writeString(this.grandTotal);
        dest.writeString(this.orderNo);
        dest.writeString(this.received_amount);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeString(this.receivedByPayment);
    }

    protected TodayAgentDelivery(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.mobile = in.readString();
        this.password = in.readString();
        this.gender = in.readString();
        this.dob = in.readString();
        this.email = in.readString();
        this.pin = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.district = in.readString();
        this.profileImg = in.readString();
        this.farmerShopName = in.readString();
        this.role = in.readString();
        this.status = in.readString();
        this.timestamp = in.readString();
        this.houseNo = in.readString();
        this.landmark = in.readString();
        this.societyName = in.readString();
        this.cid = in.readString();
        this.agentId = in.readString();
        this.grandTotal = in.readString();
        this.orderNo = in.readString();
        this.received_amount = in.readString();
        this.isChecked = in.readByte() != 0;
        this.receivedByPayment = in.readString();
    }

    public static final Creator<TodayAgentDelivery> CREATOR = new Creator<TodayAgentDelivery>() {
        @Override
        public TodayAgentDelivery createFromParcel(Parcel source) {
            return new TodayAgentDelivery(source);
        }

        @Override
        public TodayAgentDelivery[] newArray(int size) {
            return new TodayAgentDelivery[size];
        }
    };
}