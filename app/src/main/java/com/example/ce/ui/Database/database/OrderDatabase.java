package com.example.ce.ui.Database.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ce.ui.Database.DAO.OrderDAO;
import com.example.ce.ui.Database.entity.Order;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Order.class}, version = 1, exportSchema = false)
public abstract class OrderDatabase extends RoomDatabase {
    public abstract OrderDAO orderDao();
    private static OrderDatabase INSTANCE;
    //we create an ExecutorService with a fixed thread pool so we can later run database operations asynchronously on a background thread.
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    //A synchronized method in a multi threaded environment means that two threadsare not allowed to access data at the same time
    public static synchronized OrderDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            OrderDatabase.class, "OrderDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}