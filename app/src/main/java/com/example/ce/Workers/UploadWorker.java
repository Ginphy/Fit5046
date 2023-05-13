package com.example.ce.Workers;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ce.R;
import com.example.ce.ui.Database.DAO.OrderDAO;
import com.example.ce.ui.Database.database.OrderDatabase;
import com.example.ce.ui.Database.entity.Order;
import com.example.ce.ui.Database.viewmodel.OrderViewModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadWorker extends Worker {
    private OrderDAO orderDAO;
    private OrderViewModel orderViewModel;
    private FirebaseFirestore database;
    public UploadWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        OrderDatabase db = OrderDatabase.getInstance(appContext);
        orderDAO = db.orderDao();
        database = FirebaseFirestore.getInstance();

    }

    private static final String TAG = UploadWorker.class.getSimpleName();

    @NonNull
    @Override
    public Result doWork() {

        Context applicationContext = getApplicationContext();

        try {


            List<Order> OrderList = (List<Order>) orderDAO.getAll();
            for (Order order : OrderList) {
                DocumentReference docRef = database.collection("Orders").document(String.valueOf(order.orderid));
                Map<String, Object> data = new HashMap<>();
                data.put("Orderid", order.orderid);
                data.put("userid", order.user_id);
                data.put("Itemname", order.itemName);
                data.put("StartDate", order.start_date);
                data.put("StartName", order.start_mame);
                data.put("EndName", order.terminal_mame);
                data.put("Couier", order.courier_id);
                data.put("Type", order.type);
                data.put("Price", order.price);
                data.put("Status", order.status);
                data.put("Description", order.description);
                docRef.set(data);
            }
            return Result.success();
        } catch (Throwable throwable) {

            // Technically WorkManager will return Result.failure()
            // but it's best to be explicit about it.
            // Thus if there were errors, we're return FAILURE
            Log.e(TAG, "Error upload order information", throwable);
            return Result.failure();
        }
    }
}