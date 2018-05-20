package com.dev.test.appcontroller;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.dev.test.storage.TestProductDatabase;

public class TestProductApp extends Application {
    private TestProductDatabase database;
    public static TestProductApp applicationInstance;
    private static final String DATABASE_NAME = "TestProductDatabase";

    @Override
    public void onCreate() {
        super.onCreate();
        applicationInstance = this;
        database = Room.databaseBuilder(getApplicationContext(), TestProductDatabase.class, DATABASE_NAME).build();
    }

    public TestProductDatabase getDB() {
        return database;
    }
}
