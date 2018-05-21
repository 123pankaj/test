package com.dev.test.storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.test.entities.Product;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM product")
    Flowable<List<Product>> getAllProducts();

    @Insert
    void insertAll(Product product);

    @Update
    void update(Product product);

    @Query("DELETE FROM product WHERE productId = :id")
    void delete(int id);
}
