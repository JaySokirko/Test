package com.jay.test.model.http;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Products {

    @SerializedName("results")
    @Expose
    private List<Product> productResults = null;

    public List<Product> getProductResults() {
        return productResults;
    }

    public void setProductResults(List<Product> productResults) {
        this.productResults = productResults;
    }
}
