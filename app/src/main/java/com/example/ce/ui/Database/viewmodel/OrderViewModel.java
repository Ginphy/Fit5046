package com.example.ce.ui.Database.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ce.ui.Database.entity.Order;
import com.example.ce.ui.Database.repository.OrderRepository;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OrderViewModel extends AndroidViewModel {
    private OrderRepository oRepository;
    private static LiveData<List<Order>> allorders;
    private static LiveData<List<Order>> allprocessingorder;
    private FirebaseAuth auth;

    public OrderViewModel (Application application) {
        super(application);
        oRepository = new OrderRepository(application);
        allorders = oRepository.getAllorders();
        allprocessingorder = oRepository.getAllprocessingorder();
    }
//    public CompletableFuture<Order> findByIDFuture(final boolean status){
//        return oRepository.findByIDFuture(status);
//    }
    public static LiveData<List<Order>> getAllorders() {
        return allorders;
    }
    public static LiveData<List<Order>> getAllprocessingorder(){return allprocessingorder;}
    public void insert(Order order) {
        oRepository.insert(order);
    }
    public void update(Order order) {
        oRepository.updateOrder(order);
    }
    public void updateStatus(boolean status, int orderid){oRepository.updateStatus(status, orderid);}

}
