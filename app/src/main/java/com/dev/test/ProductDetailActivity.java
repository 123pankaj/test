package com.dev.test;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dev.test.datarepo.DatabaseCallback;
import com.dev.test.entities.Product;
import com.dev.test.storage.DatabaseManager;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDetailActivity extends AppCompatActivity implements DatabaseCallback {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.productImage) AppCompatImageView productImage;
    @BindView(R.id.edProductName) AppCompatEditText edProductName;
    @BindView(R.id.edProductLocation) AppCompatEditText edProductLocation;
    @BindView(R.id.edProductPrice) AppCompatEditText edProductPrice;
    @BindView(R.id.edProductDescription) AppCompatEditText edProductDescription;
    @BindView(R.id.parentLayout) CoordinatorLayout parentLayout;
    @BindView(R.id.submitProduct) AppCompatButton submitProduct;

    @OnClick(R.id.submitProduct)
    void addProductToDb() {
        if (TextUtils.isEmpty(edProductName.getText().toString())) {
            Snackbar.make(parentLayout, "Enter product name.!", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edProductLocation.getText().toString())) {
            Snackbar.make(parentLayout, "Enter product location.!", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edProductPrice.getText().toString())) {
            Snackbar.make(parentLayout, "Enter product price.!", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edProductDescription.getText().toString())) {
            Snackbar.make(parentLayout, "Enter product description.!", Snackbar.LENGTH_SHORT).show();
        } else {
            Product product = new Product(edProductName.getText().toString(),
                    getIntent().getStringExtra("itemImageUrl"),
                    edProductDescription.getText().toString(),
                    edProductLocation.getText().toString(),
                    edProductPrice.getText().toString());

            product.setProductId(getIntent().getIntExtra("itemId", 1));
            DatabaseManager.getInstance(ProductDetailActivity.this).updateProduct(ProductDetailActivity.this, product);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        onInitActivity();
    }

    private void onInitActivity() {
        setSupportActionBar(toolbar);
        setTitle("Product details");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent()!=null){
            edProductName.setEnabled(false);
            edProductLocation.setEnabled(false);
            edProductPrice.setEnabled(false);
            edProductDescription.setEnabled(false);

            edProductName.setText(getIntent().getStringExtra("itemName"));
            edProductLocation.setText(getIntent().getStringExtra("itemLocation"));
            edProductPrice.setText(getIntent().getStringExtra("itemPrice"));
            edProductDescription.setText(getIntent().getStringExtra("itemDescription"));
            productImage.setImageURI(Uri.fromFile(new File(getIntent().getStringExtra("itemImageUrl"))));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem deleteItem = menu.findItem(R.id.action_delete);
        deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                /**
                 * Delete item form db
                 */
                if (getIntent() != null) {
                    DatabaseManager.getInstance(ProductDetailActivity.this).deleteProduct(ProductDetailActivity.this, getIntent().getIntExtra("itemId", 1));
                }
                return false;
            }
        });

        final MenuItem editItem = menu.findItem(R.id.action_edit);
        editItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                /**
                 * edit item in db
                 */
                edProductName.setEnabled(true);
                edProductLocation.setEnabled(true);
                edProductPrice.setEnabled(true);
                edProductDescription.setEnabled(true);
                submitProduct.setVisibility(View.VISIBLE);
                editItem.setVisible(false);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onProductLoaded(List<Product> data) {
        Log.i("Not Used", "Not Used");
    }

    @Override
    public void onProductDeleted() {
        Snackbar.make(parentLayout, "Product deleted Successfully..!", Snackbar.LENGTH_SHORT).show();
        ProductDetailActivity.this.finish();
    }

    @Override
    public void onProductAdded() {
        Log.i("Not Used", "Not Used");
    }

    @Override
    public void onDataNotAvailable() {
        Snackbar.make(parentLayout, "Product not available in records..!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onProductUpdated() {
        Snackbar.make(parentLayout, "Product updated Successfully..!", Snackbar.LENGTH_SHORT).show();
        ProductDetailActivity.this.finish();
    }
}
