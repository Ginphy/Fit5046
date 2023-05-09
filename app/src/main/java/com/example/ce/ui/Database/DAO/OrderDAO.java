package com.example.ce.ui.Database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ce.ui.Database.entity.Order;

import java.util.List;

@Dao
public interface OrderDAO {
    @Query("SELECT * FROM `Order` ORDER BY user_id ASC")
    LiveData<List<Order>> getAll();
    @Query("SELECT * FROM `Order` WHERE user_id = :user_id")
    Order findByID(int user_id);
    @Insert
    void insert(Order order);
    @Delete
    void delete(Order order);
    @Update
    void updateCustomer(Order order);
}
