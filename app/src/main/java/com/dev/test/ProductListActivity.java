package com.dev.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.dev.test.adapter.ProductListAdapter;
import com.dev.test.entities.Product;
import com.dev.test.storage.DatabaseCallback;
import com.dev.test.storage.DatabaseManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductListActivity extends AppCompatActivity implements DatabaseCallback {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.parentLayout) CoordinatorLayout parentLayout;
    @BindView(R.id.productViewList) RecyclerView productViewList;
    private ProductListAdapter _adapter;

    @OnClick(R.id.fab)
    void addProduct() {
        startActivity(new Intent(ProductListActivity.this, AddProductActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        onInitActivity();
    }

    @SuppressLint("CheckResult")
    private void onInitActivity() {
        setSupportActionBar(toolbar);
        setTitle("Product list");
        productViewList.setHasFixedSize(true);
        productViewList.setLayoutManager(new LinearLayoutManager(this));
        _adapter = new ProductListAdapter(ProductListActivity.this);
        productViewList.setAdapter(_adapter);

        /**
         * Getting products from database
         */
        DatabaseManager.getInstance(ProductListActivity.this).getAllProducts(this);
    }

    @Override
    public void onProductLoaded(List<Product> data) {
        Log.i("Product list screen", data.toString());
        if (data.isEmpty()){
            Snackbar.make(parentLayout, "Please add products", Snackbar.LENGTH_LONG).show();
        }
        _adapter.setValues(data);
    }

    @Override
    public void onProductDeleted() {
        Log.i("Not Used", "Not Used");
    }

    @Override
    public void onProductAdded() {
        Log.i("Not Used", "Not Used");
    }

    @Override
    public void onDataNotAvailable() {
        Log.i("Not Used", "Not Used");
    }

    @Override
    public void onProductUpdated() {
        Log.i("Not Used", "Not Used");
    }
}
