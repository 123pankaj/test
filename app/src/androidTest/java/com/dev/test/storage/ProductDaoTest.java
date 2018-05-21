package com.dev.test.storage;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.dev.test.entities.Product;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

@RunWith(AndroidJUnit4.class)
public class ProductDaoTest {
    private ProductDao mProductDao;
    private TestProductDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, TestProductDatabase.class).build();
        mProductDao = mDb.productDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void writeProductAndReadInList() throws Exception {
        mProductDao.insertAll(new Product("test", "test", "test", "test", "test"));
        Flowable<List<Product>> dataList = mProductDao.getAllProducts();
        Assert.assertNotNull(dataList);
        dataList.subscribe(new Consumer<List<Product>>() {
            @Override
            public void accept(List<Product> products) throws Exception {
                Assert.assertEquals(products.size(), 1);
            }
        });
    }

    @Test
    public void shouldDeleteProduct() {
        mProductDao.insertAll(new Product("test", "test", "test", "test", "test"));
        Flowable<List<Product>> dataList = mProductDao.getAllProducts();
        Assert.assertNotNull(dataList);
        dataList.subscribe(new Consumer<List<Product>>() {
            @Override
            public void accept(List<Product> products) throws Exception {
                mProductDao.delete(products.get(0).getProductId());
            }
        });
    }

}
