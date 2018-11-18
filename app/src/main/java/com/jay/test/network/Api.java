package com.jay.test.network;

import com.jay.test.model.Categories;
import com.jay.test.model.Products;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api {

    @GET("taxonomy/categories?api_key=hz1eg53dxyxrqkqg3c6yczby")
    Call<Categories> getCategories();


    @GET
    Call<Products> getProducts(@Url String category);

}
