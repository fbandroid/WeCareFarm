package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pincode {

@SerializedName("taluka")
@Expose
private String taluka;
@SerializedName("district")
@Expose
private String district;
@SerializedName("state")
@Expose
private String state;

public String getTaluka() {
return taluka;
}

public void setTaluka(String taluka) {
this.taluka = taluka;
}

public String getDistrict() {
return district;
}

public void setDistrict(String district) {
this.district = district;
}

public String getState() {
return state;
}

public void setState(String state) {
this.state = state;
}

}