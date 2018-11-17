package com.jay.test.model.http;

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


    @SerializedName("url_570xN")
    private String image570xN;

    public String getImage570xN() {
        return image570xN;
    }

    public void setImage570xN(String image570xN) {
        this.image570xN = image570xN;
    }
}
