package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetList {

@SerializedName("dist")
@Expose
private Dist dist;
@SerializedName("city")
@Expose
private List<City> city = null;

public Dist getDist() {
return dist;
}

public void setDist(Dist dist) {
this.dist = dist;
}

public List<City> getCity() {
return city;
}

public void setCity(List<City> city) {
this.city = city;
}

}