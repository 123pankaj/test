package com.dev.test.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dev.test.ProductDetailActivity;
import com.dev.test.R;
import com.dev.test.entities.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {

    private List<Product> dataList;
    private Context _context;

    public ProductListAdapter(Context context) {
        this.dataList = new ArrayList<>();
        this._context = context;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inflator, parent, false);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        Product product = dataList.get(position);
        holder.productName.setText(_context.getString(R.string.name, product.getProductName()));
        holder.productPrice.setText(_context.getString(R.string.price, product.getProductCost()));
        holder.productLocation.setText(_context.getString(R.string.location, product.getProductLocation()));
        holder.productDescription.setText(_context.getString(R.string.description, product.getProductDescription()));
        Glide.with(holder.productImage.getContext()).load(product.getProductImageUrl()).into(holder.productImage);
    }

    public void setValues(List<Product> data) {
        dataList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ProductListViewHolder extends RecyclerView.ViewHolder {
        public @BindView(R.id.productName) AppCompatTextView productName;
        public @BindView(R.id.productPrice) AppCompatTextView productPrice;
        public @BindView(R.id.productLocation) AppCompatTextView productLocation;
        public @BindView(R.id.productDescription) AppCompatTextView productDescription;
        public @BindView(R.id.productImage) AppCompatImageView productImage;

        public ProductListViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                    intent.putExtra("itemId", dataList.get(getAdapterPosition()).getProductId());
                    intent.putExtra("itemName", dataList.get(getAdapterPosition()).getProductName());
                    intent.putExtra("itemLocation", dataList.get(getAdapterPosition()).getProductLocation());
                    intent.putExtra("itemPrice", dataList.get(getAdapterPosition()).getProductCost());
                    intent.putExtra("itemImageUrl", dataList.get(getAdapterPosition()).getProductImageUrl());
                    intent.putExtra("itemDescription", dataList.get(getAdapterPosition()).getProductDescription());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
