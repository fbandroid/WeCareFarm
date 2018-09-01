package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PincodeList {

@SerializedName("Pincode")
@Expose
private Pincode pincode;

public Pincode getPincode() {
return pincode;
}

public void setPincode(Pincode pincode) {
this.pincode = pincode;
}

}