package com.example.ce.ui.Database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Database;

import com.example.ce.ui.Database.DAO.OrderDAO;
import com.example.ce.ui.Database.database.OrderDatabase;
import com.example.ce.ui.Database.entity.Order;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class OrderRepository {
    private OrderDAO orderDao;
    private LiveData<List<Order>> allorders;
    private LiveData<List<Order>> allprocessingorder;
    private List<Order> allOrders;
    public OrderRepository(Application application) {
        OrderDatabase db = OrderDatabase.getInstance(application);
        orderDao = db.orderDao();
        allorders = orderDao.getAll();
//        allOrders = orderDao.getAllOrders();
        allprocessingorder = orderDao.getProcessingOrder(false);
    }

    // Room executes this query on a separate thread
    public LiveData<List<Order>> getAllorders() {
        return allorders;
    }
    public LiveData<List<Order>> getAllprocessingorder() {return  allprocessingorder;}
    public List<Order> getAllOrders(){return allOrders;}
    public void updateStatus(boolean status, int orderid){
        OrderDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {orderDao.updateStatus(status, orderid);
            }
        });
    }
    public void insert(final Order order) {
        OrderDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                orderDao.insert(order);
            }
        });
    }

    public void delete(final Order order) {
        OrderDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                orderDao.delete(order);
            }
        });
    }

    public void updateOrder(final Order order) {
        OrderDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                orderDao.updateOrder(order);
            }
        });
    }


//    public CompletableFuture<Order> findByIDFuture(final boolean status) {
//        return CompletableFuture.supplyAsync(new Supplier<Order>() {
//            @Override
//            public Order get() {
//                return orderDao.findByID(status);
//            }
//        }, OrderDatabase.databaseWriteExecutor);
//    }
}
