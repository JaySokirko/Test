package com.jay.test.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.jay.test.R;
import com.jay.test.adapter.ProductsAdapter;
import com.jay.test.network.Api;
import com.jay.test.network.ApiClient;
import com.jay.test.model.Product;
import com.jay.test.model.Products;
import com.jay.test.observer.LoadDataListener;
import com.jay.test.utils.InternetConnection;
import com.jay.test.ui.dialog.NoInternetConnectionDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoundProductsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ProductsAdapter.OnItemClickListener {


    private ArrayList<String> titlesList = new ArrayList<>();
    private ArrayList<String> imagesList = new ArrayList<>();

    //These arrays will be used to transfer data to detailed activity.
    private ArrayList<String> descriptionsList = new ArrayList<>();
    private ArrayList<String> pricesList = new ArrayList<>();
    private ArrayList<String> currenciesCodesList = new ArrayList<>();

    private int pageIndex = 1;
    private String category;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private ProgressBar bottomProgressBar;
    private ProgressBar centerProgressBar;

    private LoadDataListener loadDataListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_products);

        recyclerView = findViewById(R.id.founded_products);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        bottomProgressBar = findViewById(R.id.bottom_progress_bar);

        centerProgressBar = findViewById(R.id.center_progress_bar);
        centerProgressBar.setVisibility(View.VISIBLE);

        //get a category in which to look for products
        category = getIntent().getStringExtra("category");

        loadDataListener = new LoadDataListener();

        //Check if there is an internet connection.
        if (InternetConnection.isOnline(FoundProductsActivity.this)) {

            loadProducts();
        } else {

            NoInternetConnectionDialog.buildDialog(FoundProductsActivity.this);
            bottomProgressBar.setVisibility(View.GONE);
            centerProgressBar.setVisibility(View.GONE);
        }

        adapter = new ProductsAdapter(FoundProductsActivity.this, titlesList,
                imagesList, FoundProductsActivity.this);
        recyclerView.setAdapter(adapter);

        //Update the data in the adapter when the download is complete
        onDataLoadListener();

        onPaginationListener();

        setSwipeRefreshLayout();

    }


    private void setSwipeRefreshLayout() {

        refreshLayout = findViewById(R.id.refresh_products);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
    }


    private void loadProducts() {

        Api apiService = ApiClient.getClient().create(Api.class);

        String page = String.valueOf(pageIndex);

        String apiKey = "&api_key=l6pdqjuf7hdf97h1yvzadfce";
        String url = "listings/active?includes=Images&category=";
        String address = url + category + "&page=" + page + apiKey;


        apiService.getProducts(address).enqueue(new Callback<Products>() {

            @Override
            public void onResponse(@NonNull Call<Products> call, @NonNull Response<Products> response) {

                if (response.body() != null) {

                    for (Product products : response.body().getProductResults()) {

                        titlesList.add(products.getTitle()
                                .replaceAll("&#39;", "\'")
                                .replaceAll("&quot;", "\"")
                                .replaceAll("&lt;", "<")
                                .replaceAll("&gt;", ">"));

                        imagesList.add(products.getImages().get(0).getImage170x135());

                        descriptionsList.add(products.getDescription()
                                .replaceAll("&#39;", "\'")
                                .replaceAll("&quot;", "\"")
                                .replaceAll("&lt;", "<")
                                .replaceAll("&gt;", ">"));

                        pricesList.add(products.getPrice());

                        currenciesCodesList.add(products.getCurrencyCode());
                    }
                }
                loadDataListener.setDataLoaded(true);
            }

            @Override
            public void onFailure(@NonNull Call<Products> call, @NonNull Throwable t) {

                bottomProgressBar.setVisibility(View.GONE);
                centerProgressBar.setVisibility(View.GONE);

                Snackbar.make(refreshLayout, getResources().getString(R.string.fail_to_load),
                        BaseTransientBottomBar.LENGTH_SHORT);
            }
        });
    }


    private void onPaginationListener() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1)) {

                    bottomProgressBar.setVisibility(View.VISIBLE);

                    pageIndex++;

                    loadProducts();
                }
            }
        });
    }


    @Override
    public void onRefresh() {

        //just go to the next page.
        pageIndex++;

        if (!titlesList.isEmpty() && !imagesList.isEmpty()) {

            titlesList.clear();
            imagesList.clear();
        }

        loadProducts();
    }


    private void onDataLoadListener() {

        loadDataListener.setListener(() -> {

            refreshLayout.setRefreshing(false);

            adapter.notifyDataSetChanged();

            bottomProgressBar.setVisibility(View.GONE);
            centerProgressBar.setVisibility(View.GONE);
        });
    }


    @Override
    public void onItemClick(int position) {

        startActivity(new Intent(FoundProductsActivity.this, DetailedScreenActivity.class)
                .putExtra("image", imagesList.get(position))
                .putExtra("description", descriptionsList.get(position))
                .putExtra("title", titlesList.get(position))
                .putExtra("price", pricesList.get(position))
                .putExtra("currency", currenciesCodesList.get(position)));
    }


}
