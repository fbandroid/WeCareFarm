package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {

@SerializedName("id")
@Expose
private String id;
@SerializedName("city")
@Expose
private String city;
@SerializedName("dist")
@Expose
private String dist;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getCity() {
return city;
}

public void setCity(String city) {
this.city = city;
}

public String getDist() {
return dist;
}

public void setDist(String dist) {
this.dist = dist;
}

}