package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

@SerializedName("id")
@Expose
private String id;
@SerializedName("category_name")
@Expose
private String categoryName;
@SerializedName("status")
@Expose
private String status;
@SerializedName("img")
@Expose
private String img;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getCategoryName() {
return categoryName;
}

public void setCategoryName(String categoryName) {
this.categoryName = categoryName;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getImg() {
return img;
}

public void setImg(String img) {
this.img = img;
}

}