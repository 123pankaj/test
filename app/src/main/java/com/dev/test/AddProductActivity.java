package com.dev.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.dev.test.entities.Product;
import com.dev.test.storage.DatabaseCallback;
import com.dev.test.storage.DatabaseManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class AddProductActivity extends AppCompatActivity implements DatabaseCallback {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.productImage) AppCompatImageView productImage;
    @BindView(R.id.pickImage) AppCompatImageView pickImage;
    @BindView(R.id.edProductName) AppCompatEditText edProductName;
    @BindView(R.id.edProductLocation) AppCompatEditText edProductLocation;
    @BindView(R.id.edProductPrice) AppCompatEditText edProductPrice;
    @BindView(R.id.edProductDescription) AppCompatEditText edProductDescription;
    @BindView(R.id.addProductParentLayout) CoordinatorLayout addProductParentLayout;
    private String productPhotoUrl;


    @OnClick(R.id.pickImage)
    void addImage() {
        requestPermissions();
    }


    @OnClick(R.id.submitProduct)
    void addProductToDb() {
        if (TextUtils.isEmpty(edProductName.getText().toString())) {
            Snackbar.make(addProductParentLayout, "Enter product name.!", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edProductLocation.getText().toString())) {
            Snackbar.make(addProductParentLayout, "Enter product location.!", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edProductPrice.getText().toString())) {
            Snackbar.make(addProductParentLayout, "Enter product price.!", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edProductDescription.getText().toString())) {
            Snackbar.make(addProductParentLayout, "Enter product description.!", Snackbar.LENGTH_SHORT).show();
        } else {
            addNewProduct();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);
        onInitActivity();
    }

    private void onInitActivity() {
        setSupportActionBar(toolbar);
        setTitle("Add New Product");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @SuppressLint("StaticFieldLeak")
    private void addNewProduct() {
        Product product = new Product(edProductName.getText().toString(),
                productPhotoUrl,
                edProductDescription.getText().toString(),
                edProductLocation.getText().toString(),
                edProductPrice.getText().toString());
        DatabaseManager.getInstance(this).addProduct(this, product);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Snackbar.make(addProductParentLayout, "Something went wrong.!", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                if (imageFile != null) {
                    productPhotoUrl = imageFile.getPath();
                    productImage.setImageURI(Uri.fromFile(imageFile));
                    System.out.println("File path is -> " + imageFile.getPath());
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(AddProductActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        EasyImage.clearConfiguration(this);
        super.onDestroy();
    }

    @Override
    public void onProductLoaded(List<Product> data) {
        Log.i("Add product screen", "Not used");
    }

    @Override
    public void onProductDeleted() {
        Log.i("Add product screen", "Not used");
    }

    @Override
    public void onProductAdded() {
        Snackbar.make(addProductParentLayout, "Product add successfully.!", Snackbar.LENGTH_SHORT).show();
        AddProductActivity.this.finish();
    }

    @Override
    public void onDataNotAvailable() {
        Snackbar.make(addProductParentLayout, "Something went wrong.!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onProductUpdated() {
        Log.i("Add product screen", "Not used");
    }


    private void requestPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Snackbar.make(addProductParentLayout, "All permissions are granted!", Snackbar.LENGTH_LONG).show();
                            EasyImage.openChooserWithDocuments(AddProductActivity.this, "Choose Option.", 0);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Snackbar.make(addProductParentLayout, "This app needs permission to use this feature. You can grant them in app settings!", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Snackbar.make(addProductParentLayout, "Error occurred! ", Snackbar.LENGTH_LONG).show();
                    }
                })
                .onSameThread()
                .check();
    }
}
