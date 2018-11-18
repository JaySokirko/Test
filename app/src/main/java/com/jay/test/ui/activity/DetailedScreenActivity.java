package com.jay.test.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jay.test.R;
import com.jay.test.database.DataBase;
import com.squareup.picasso.Picasso;

public class DetailedScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private String image;
    private String title;
    private String description;
    private String price;
    private String currencyCode;

    private boolean isAlReadySaved;
    private boolean isAlreadyDeleted;

    //This activity can be opened from the FoundProductActivity or SavedProductFragment.
    private boolean isActivityOpenFromSavedProductFragment;

    private FrameLayout parentLayout;
    private FloatingActionButton saveBtn;
    private DataBase dataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_screen);

        parentLayout = findViewById(R.id.parent_layout);
        saveBtn = findViewById(R.id.save);

        isActivityOpenFromSavedProductFragment = getIntent().getBooleanExtra("savedProduct", false);

        //If this activity is opened from a SavedProductsFragment,
        //then set delete icon to floating action button.
        if (isActivityOpenFromSavedProductFragment) {
            saveBtn.setImageResource(R.drawable.ic_action_delete);
        }

        isAlReadySaved = false;
        isAlreadyDeleted = false;

        image = getIntent().getStringExtra("image");
        title = getIntent().getStringExtra("title");

        description = getIntent().getStringExtra("description");

        price = getIntent().getStringExtra("price");
        currencyCode = getIntent().getStringExtra("currency");

        setDataToViews();

        dataBase = new DataBase(DetailedScreenActivity.this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataBase.close();
    }


    @SuppressLint("SetTextI18n")
    private void setDataToViews() {

        ImageView imageView = findViewById(R.id.image);
        TextView titleTextView = findViewById(R.id.title);
        TextView descriptionTextView = findViewById(R.id.description);
        TextView priceTextView = findViewById(R.id.price);
        TextView currencyTextView = findViewById(R.id.currency);

        Picasso.get().load(image).into(imageView);
        titleTextView.setText(title);
        descriptionTextView.setText(description);
        priceTextView.setText("Price: " + price);
        currencyTextView.setText("Currency code: " + currencyCode);
    }


    private void saveProduct() {

        if (!isAlReadySaved) {

            Snackbar.make(parentLayout, getResources().getString(R.string.saved),
                    Snackbar.LENGTH_SHORT).show();

            dataBase.insertData(title, image, description, price, currencyCode);

            saveBtn.setImageResource(R.drawable.ic_saved);

        } else {

            Snackbar.make(parentLayout, getResources().getString(R.string.already_saved),
                    Snackbar.LENGTH_SHORT).show();
        }

        isAlReadySaved = true;
    }


    private void deleteProduct() {

        if (!isAlreadyDeleted) {

            Snackbar.make(parentLayout, getResources().getString(R.string.deleted),
                    Snackbar.LENGTH_SHORT).show();

            dataBase.deleteFromDataBase(title);

        } else {

            Snackbar.make(parentLayout, getResources().getString(R.string.already_deleted),
                    Snackbar.LENGTH_SHORT).show();
        }

        isAlreadyDeleted = true;

    }



    @Override
    public void onClick(View view) {

        if (isActivityOpenFromSavedProductFragment) {

            deleteProduct();

        } else {

            saveProduct();
        }
    }

}
