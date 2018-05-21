package com.dev.test.storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dev.test.entities.Product;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class TestProductDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}
