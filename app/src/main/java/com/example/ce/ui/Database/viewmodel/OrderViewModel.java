package com.example.ce.ui.Database.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ce.ui.Database.entity.Order;
import com.example.ce.ui.Database.repository.OrderRepository;

import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OrderViewModel extends AndroidViewModel {
    private OrderRepository oRepository;
    private static LiveData<List<Order>> allorders;
    public OrderViewModel (Application application) {
        super(application);
        oRepository = new OrderRepository(application);
        allorders = oRepository.getAllorders();
    }
    public CompletableFuture<Order> findByIDFuture(final boolean status){
        return oRepository.findByIDFuture(status);
    }
    public static LiveData<List<Order>> getAllorders() {
        return allorders;
    }
    public void insert(Order order) {
        oRepository.insert(order);
    }
    public void update(Order order) {
        oRepository.updateCustomer(order);
    }
}
