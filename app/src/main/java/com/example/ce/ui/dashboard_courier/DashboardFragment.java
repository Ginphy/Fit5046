package com.example.ce.ui.dashboard_courier;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.ce.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProvider;

import com.example.ce.databinding.FragmentDashboardBinding;
import com.example.ce.ui.Database.entity.Order;
import com.example.ce.ui.Database.viewmodel.OrderViewModel;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends Fragment{
    //
//   private final Context context;
    private ArrayList<com.example.ce.ui.dashboard_courier.DashboardViewModel>  orderModelArrayList;
    private OrderViewModel orderViewModel;


    private FragmentDashboardBinding binding;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Date date = new Date();
        com.example.ce.ui.dashboard_courier.DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(com.example.ce.ui.dashboard_courier.DashboardViewModel.class);
        View root = inflater.inflate(R.layout.dashboard_courier, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        ArrayList<com.example.ce.ui.dashboard_courier.DashboardViewModel> orderModelArrayList = new ArrayList<com.example.ce.ui.dashboard_courier.DashboardViewModel>();
        orderViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(OrderViewModel.class);
        OrderViewModel.getAllprocessingorder().observe(getViewLifecycleOwner(), new
                Observer<List<Order>>() {
                    @Override
                    public void onChanged(@Nullable final List<Order> orders) {
                        for (Order temp : orders) {
                            orderModelArrayList.add(new DashboardViewModel(temp.orderid, temp.itemName, temp.start_mame, temp.terminal_mame,
                                    temp.start_date, temp.type, temp.user_id, temp.price, "Waiting.."));
                        }
                    }
                });

        com.example.ce.ui.dashboard_courier.OrderAdapter courseAdapter = new OrderAdapter(getActivity(), orderModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(courseAdapter);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}