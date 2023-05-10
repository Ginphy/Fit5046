package com.example.ce.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ce.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ce.databinding.FragmentDashboardBinding;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Date;

public class DashboardFragment extends Fragment{
//
//   private final Context context;
   private ArrayList<DashboardViewModel>  courseModelArrayList;


    private FragmentDashboardBinding binding;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Date date = new Date();
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
     //   binding = FragmentDashboardBinding.inflate(inflater, container, false);
    //    View root = binding.getRoot();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        ArrayList<DashboardViewModel> orderModelArrayList = new ArrayList<DashboardViewModel>();
        orderModelArrayList.add(new DashboardViewModel("Order 1", "Wine", "Suzhou Station", "Suzhou", date, "Order 1", "Order 1", 10));



        OrderAdapter courseAdapter = new OrderAdapter(getActivity(), orderModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(courseAdapter);

    //    final TextView textView = binding.textDashboard;
 //       dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
