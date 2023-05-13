package com.example.ce.ui.home.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ce.databinding.AddFragmentBinding;
import com.example.ce.ui.home.viewmodel.SharedViewModel;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ce.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ce.databinding.FragmentDashboardBinding;
import com.example.ce.ui.Database.entity.Order;
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
import java.util.List;
//public class AddFragment extends Fragment {
//
//    private AddFragmentBinding addBinding;
//
//    public AddFragment(){}
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        addBinding = AddFragmentBinding.inflate(inflater, container, false);
//        View view = addBinding.getRoot();
//
//        SharedViewModel model = new
//                ViewModelProvider(getActivity()).get(SharedViewModel.class);
//
//        addBinding.addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String message = addBinding.editText.getText().toString();
//                if (!message.isEmpty() ) {
//                    model.setMessage(message);
//                }
//            }
//        });
//        addBinding.clearButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addBinding.editText.setText("");
//            }
//        });
//        return view;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        addBinding = null;
//    }
//}
public class AddFragment extends Fragment{
    //
//   private final Context context;
    private ArrayList<com.example.ce.ui.dashboard_courier.DashboardViewModel>  courseModelArrayList;
    private OrderViewModel orderViewModel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FragmentDashboardBinding binding;

    int Orderid;
    double Price;
    String Itemname,StartName,EndName,StartDate,Type,userid,Status;
    ArrayList<com.example.ce.ui.dashboard_courier.DashboardViewModel> list = new ArrayList<>();
    ArrayList<com.example.ce.ui.dashboard_courier.DashboardViewModel> orderModelArrayList = new ArrayList<com.example.ce.ui.dashboard_courier.DashboardViewModel>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Date date = new Date();
        com.example.ce.ui.dashboard_courier.DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(com.example.ce.ui.dashboard_courier.DashboardViewModel.class);
        View root = inflater.inflate(R.layout.dashboard_courier, container, false);
        //   binding = FragmentDashboardBinding.inflate(inflater, container, false);
        //    View root = binding.getRoot();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
//        orderViewModel =
//                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(OrderViewModel.class);
//        OrderViewModel.getAllprocessingorder().observe(getViewLifecycleOwner(), new
//                Observer<List<Order>>() {
//                    @Override
//                    public void onChanged(@Nullable final List<Order> orders) {
//                        for (Order temp : orders) {
//                            orderModelArrayList.add(new DashboardViewModel(temp.orderid, temp.itemName, temp.start_mame, temp.terminal_mame,
//                                    temp.start_date, temp.type, temp.courier_id, temp.price, "Waiting..."));
//                        }
//                    }
//                });

//        orderModelArrayList.add(new DashboardViewModel(1, "asdasd", "asdasd", "asdasd",
//                date.toString(), "asdasd", "asdasd", 6,"Waiting"));
//
//        orderModelArrayList.add(new DashboardViewModel(2, "黄宇航", "东南大学苏州", "文荟人才公寓",
//                date.toString(), "Pig", "110", -250,"Waiting"));
        db.collection("Orders")
                .whereEqualTo("Status", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Orderid = document.getLong("Orderid").intValue();
                                Itemname = document.getString("Itemname");
                                StartName = document.getString("StartName");
                                EndName = document.getString("EndName");
                                StartDate = document.getString("StartDate");
                                Type = document.getString("Type");
                                userid = document.getString("userid").toString();
                                Price = document.getDouble("Price");
                                //Status = document.getBoolean("Status").toString();
                                orderModelArrayList.add(new DashboardViewModel(Orderid, Itemname, StartName, EndName, StartDate, Type, userid, Price));
                                Log.d(TAG, "Successful getting documents!" +document.getData());
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
    public void toastMsg(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}