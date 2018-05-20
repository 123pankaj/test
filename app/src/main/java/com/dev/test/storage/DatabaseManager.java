package com.dev.test.storage;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.dev.test.datarepo.DatabaseCallback;
import com.dev.test.entities.Product;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DatabaseManager {
    private static final String DB_NAME = "TestProductDatabase";
    private Context context;
    private static DatabaseManager _instance;
    private TestProductDatabase db;

    public static DatabaseManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new DatabaseManager(context);
        }
        return _instance;
    }

    public DatabaseManager(Context context) {
        this.context = context;
        db = Room.databaseBuilder(context, TestProductDatabase.class, DB_NAME).build();
    }

    @SuppressLint("CheckResult")
    public void getAllProducts(final DatabaseCallback databaseCallback) {
        db.productDao().getAllProducts().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Product>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<Product> data) throws Exception {
                databaseCallback.onProductLoaded(data);

            }
        });
    }

    public void deleteProduct(final DatabaseCallback databaseCallback, final int productId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.productDao().delete(productId);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                databaseCallback.onProductDeleted();
            }

            @Override
            public void onError(Throwable e) {
                databaseCallback.onDataNotAvailable();
            }
        });
    }

    public void updateProduct(final DatabaseCallback databaseCallback, final Product product) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.productDao().update(product);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                databaseCallback.onProductUpdated();
            }

            @Override
            public void onError(Throwable e) {
                databaseCallback.onDataNotAvailable();
            }
        });
    }

    public void addProduct(final DatabaseCallback databaseCallback, final Product product) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.productDao().insertAll(product);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                databaseCallback.onProductAdded();
            }

            @Override
            public void onError(Throwable e) {
                databaseCallback.onDataNotAvailable();
            }
        });
    }

}
