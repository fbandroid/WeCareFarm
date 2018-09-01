package com.codefuelindia.wecarefarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryList {

@SerializedName("Category")
@Expose
private Category category;

public Category getCategory() {
return category;
}

public void setCategory(Category category) {
this.category = category;
}

}