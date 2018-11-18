package com.jay.test.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images {

    @SerializedName("url_170x135")
    @Expose
    private String image170x135;

    public String getImage170x135() {
        return image170x135;
    }

    public void setImage170x135(String image170x135) {
        this.image170x135 = image170x135;
    }
}
