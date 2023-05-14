package com.example.ce.ui.home.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.ce.R;
import androidx.annotation.NonNull;


import com.example.ce.databinding.FragmentDashboardBinding;

import com.example.ce.ui.Database.viewmodel.OrderViewModel;
import com.example.ce.ui.dashboard_courier.DashboardViewModel;
import com.example.ce.ui.dashboard_courier.OrderAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Date;


public class AddFragment extends Fragment{
    //
//   private final Context context;
    private ArrayList<com.example.ce.ui.dashboard_courier.DashboardViewModel>  courseModelArrayList;
    private OrderViewModel orderViewModel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FragmentDashboardBinding binding;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Date date = new Date();
        com.example.ce.ui.dashboard_courier.DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(com.example.ce.ui.dashboard_courier.DashboardViewModel.class);
        View root = inflater.inflate(R.layout.dashboard_courier, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        ArrayList<com.example.ce.ui.dashboard_courier.DashboardViewModel> orderModelArrayList = new ArrayList<com.example.ce.ui.dashboard_courier.DashboardViewModel>();

        db.collection("Orders")
                .whereEqualTo("Status", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                orderModelArrayList.add(new DashboardViewModel(document.getLong("Orderid").intValue(),
                                        document.getString("Itemname"),
                                        document.getString("StartName"),
                                        document.getString("EndName"),
                                        document.getString("StartDate"),
                                        document.getString("Type"),
                                        document.getString("userid").toString(),
                                        document.getDouble("Price"),
                                        document.getBoolean("Status").toString()));
                                Log.d(TAG, "Successful getting documents!" + document.getData());
                                com.example.ce.ui.dashboard_courier.OrderAdapter courseAdapter = new OrderAdapter(getActivity(), orderModelArrayList);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(courseAdapter);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}