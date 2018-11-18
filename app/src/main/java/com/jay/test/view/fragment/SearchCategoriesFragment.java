package com.jay.test.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.jay.test.R;
import com.jay.test.model.http.Api;
import com.jay.test.model.http.ApiClient;
import com.jay.test.model.http.Categories;
import com.jay.test.model.http.Category;
import com.jay.test.observer.LoadDataListener;
import com.jay.test.utils.InternetConnection;
import com.jay.test.view.activity.FoundProductsActivity;
import com.jay.test.view.dialog.NoInternetConnectionDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchCategoriesFragment extends Fragment {


    private ListView categoriesListView;
    private EditText searchEditText;
    private FrameLayout parentLayout;
    private ProgressBar progressBar;

    private ArrayList<String> categoriesList = new ArrayList<>();

    private ArrayAdapter<String> adapter;

    private Context context;

    private LoadDataListener loadDataListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_product, container, false);

        categoriesListView = rootView.findViewById(R.id.categories_list);
        searchEditText = rootView.findViewById(R.id.search_categories);
        parentLayout = rootView.findViewById(R.id.parent_layout);
        progressBar = rootView.findViewById(R.id.progress_bar);

        loadDataListener = new LoadDataListener();

        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, categoriesList);

        onEditTextChangeListener();

        onCategorySelectListener();

        categoriesListView.setAdapter(adapter);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        progressBar.setVisibility(View.VISIBLE);

        //Check if there is an internet connection.
        if (InternetConnection.isOnline(context)) {

            loadData();
        } else {
            NoInternetConnectionDialog.buildDialog(context);
            progressBar.setVisibility(View.GONE);
        }

        //Update the data in the adapter when the download is complete
        onDataLoadListener();
    }


    private void loadData(){

        Api apiService =
                ApiClient.getClient().create(Api.class);

            apiService.getCategories().enqueue(new Callback<Categories>() {
                @Override
                public void onResponse(@NonNull Call<Categories> call, @NonNull Response<Categories> response) {

                    if (response.body() != null) {
                        for (Category results : response.body().getResults()) {

                            categoriesList.add(results.getName().replaceAll("_"," "));
                        }
                    }
                    //Notify when download is complete
                    loadDataListener.setDataLoaded(true);
                }

                @Override
                public void onFailure(@NonNull Call<Categories> call, @NonNull Throwable t) {

                    Snackbar.make(parentLayout,getResources().getString(R.string.fail_to_load),
                            BaseTransientBottomBar.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.GONE);
                }
            });
    }



    private void onCategorySelectListener(){

        categoriesListView.setOnItemClickListener((parent, view, position, id) -> {

            String category = (String) categoriesListView.getItemAtPosition(position);
            category = category.replaceAll(" ","_");

            //Pass selected category to FoundProductsActivity
            startActivity(new Intent(getContext(), FoundProductsActivity.class)
                    .putExtra("category",category));

        });
    }



    private void onEditTextChangeListener(){

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Filter the list by typing characters
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }



    private void onDataLoadListener(){

        loadDataListener.setListener(() -> {

            adapter.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);
        });
    }
}
