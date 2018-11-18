package com.jay.test.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jay.test.model.Product;

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
