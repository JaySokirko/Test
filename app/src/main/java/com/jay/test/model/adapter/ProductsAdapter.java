package com.jay.test.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jay.test.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder>{

    private ArrayList<String> productsList;
    private ArrayList<String> imagesList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener callback;

    public ProductsAdapter(Context context, ArrayList<String> productsList,
                           ArrayList<String> imagesList, OnItemClickListener callback) {

        this.layoutInflater = LayoutInflater.from(context);
        this.productsList = productsList;
        this.imagesList = imagesList;
        this.callback = callback;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_products, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.productDescription.setText(productsList.get(position));

        Picasso.get().load(imagesList.get(position)).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productDescription;
        ImageView productImage;

        ViewHolder(View itemView) {
            super(itemView);

            productDescription = itemView.findViewById(R.id.description);
            productImage = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            callback.onItemClick(getAdapterPosition());
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
