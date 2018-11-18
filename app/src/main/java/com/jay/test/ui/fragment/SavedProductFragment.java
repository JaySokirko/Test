package com.jay.test.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jay.test.R;
import com.jay.test.adapter.ProductsAdapter;
import com.jay.test.database.DataBase;
import com.jay.test.ui.activity.DetailedScreenActivity;

import java.util.ArrayList;

public class SavedProductFragment extends Fragment {

    private ProductsAdapter adapter;

    private ArrayList<String> imagesList = new ArrayList<>();
    private ArrayList<String> titlesList = new ArrayList<>();
    private ArrayList<String> descriptionList = new ArrayList<>();
    private ArrayList<String> pricesList = new ArrayList<>();
    private ArrayList<String> currencyCodesList = new ArrayList<>();

    private static final int SPAN_COLUMNS = 2;

    private DataBase dataBase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved_product, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.saved_products);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), SPAN_COLUMNS);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ProductsAdapter(getContext(), titlesList, imagesList, listener());
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        loadData();
    }


    @Override
    public void onPause() {
        super.onPause();

        cleatData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        dataBase.close();
    }


    private void loadData() {

        dataBase = new DataBase(getContext());

        titlesList.addAll(dataBase.getDataFromColumn(DataBase.TITLE));
        imagesList.addAll(dataBase.getDataFromColumn(DataBase.IMAGE));
        descriptionList.addAll(dataBase.getDataFromColumn(DataBase.DESCRIPTION));
        pricesList.addAll(dataBase.getDataFromColumn(DataBase.PRICE));
        currencyCodesList.addAll(dataBase.getDataFromColumn(DataBase.CURRENCY));

        adapter.notifyDataSetChanged();
    }


    private void cleatData(){

        titlesList.clear();
        imagesList.clear();
        descriptionList.clear();
        pricesList.clear();
        currencyCodesList.clear();
    }



    private ProductsAdapter.OnItemClickListener listener() {

        return position -> startActivity(new Intent(getActivity(), DetailedScreenActivity.class)
                .putExtra("image", imagesList.get(position))
                .putExtra("description", descriptionList.get(position))
                .putExtra("title", titlesList.get(position))
                .putExtra("price", pricesList.get(position))
                .putExtra("currency", currencyCodesList.get(position))
                .putExtra("savedProduct", true));
    }
}
