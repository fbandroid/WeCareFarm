package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dist {

@SerializedName("id")
@Expose
private String id;
@SerializedName("district")
@Expose
private String district;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getDistrict() {
return district;
}

public void setDistrict(String district) {
this.district = district;
}

}