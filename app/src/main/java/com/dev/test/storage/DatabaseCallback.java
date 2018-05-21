package com.dev.test.storage;

import com.dev.test.entities.Product;

import java.util.List;

public interface DatabaseCallback {
    void onProductLoaded(List<Product> data);

    void onProductDeleted();

    void onProductAdded();

    void onDataNotAvailable();

    void onProductUpdated();
}
