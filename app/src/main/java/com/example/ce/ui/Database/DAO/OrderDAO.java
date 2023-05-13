package com.example.ce.ui.Database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ce.ui.Database.entity.Order;
import com.example.ce.ui.Database.viewmodel.OrderViewModel;

import java.util.List;

@Dao
public interface OrderDAO {
    @Query("SELECT * FROM `Order`WHERE user_id = :user_id")
    LiveData<List<Order>> getAll(String user_id);

    @Query("UPDATE `Order` SET status = :status WHERE orderid = :orderid")
    void updateStatus(boolean status, int orderid);

    @Query("SELECT * FROM `Order` WHERE status = :status")
    LiveData<List<Order>> getProcessingOrder(boolean status);
    //Order findByID(boolean status);

    @Insert
    void insert(Order order);

    @Delete
    void delete(Order order);

    @Update
    void updateOrder(Order order);
}
