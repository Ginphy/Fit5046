package com.example.ce.ui.dashboard_courier;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ce.R;
import com.example.ce.databinding.AddFragmentBinding;
import com.example.ce.ui.Database.entity.Order;
import com.example.ce.ui.Database.viewmodel.OrderViewModel;
import com.example.ce.ui.dashboard_courier.DashboardViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<com.example.ce.ui.dashboard_courier.OrderAdapter.Viewholder> {
    private final Context context;
    private final ArrayList<com.example.ce.ui.dashboard_courier.DashboardViewModel> orderModelArrayList;
    private OrderViewModel orderViewModel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Constructor
    public OrderAdapter(Context context, ArrayList<com.example.ce.ui.dashboard_courier.DashboardViewModel> orderModelArrayList) {
        this.context = context;
        this.orderModelArrayList = orderModelArrayList;
    }

    @NonNull
    @Override
    public com.example.ce.ui.dashboard_courier.OrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_courier, parent, false);
        return new com.example.ce.ui.dashboard_courier.OrderAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.ce.ui.dashboard_courier.OrderAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        DashboardViewModel model = orderModelArrayList.get(position);


        holder.OrderId.setText(""+model.getOrderId());
        holder.ArticleName.setText(model.getArticleId());
        holder.StartPoint.setText(model.getStartPoint());
        holder.EndPoint.setText(model.getEndPoint());
        holder.Date.setText(model.getDatetime());
        holder.ArticleType.setText(model.getArticleType());
        holder.CourierID.setText(model.getuserId());
        holder.OrderPrice.setText(""+model.getOrder_price());


        holder.btnTakingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //RecycleView Button's function
                DocumentReference docRef = db.collection("Orders").document(String.valueOf(model.getOrderId()));
                Map<String, Object> updates = new HashMap<>();
                updates.put("Status", true);
                docRef.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
                view.setBackgroundColor(Color.parseColor("#FF4081"));
                orderViewModel.updateStatus(true, model.getOrderId());
                notifyDataSetChanged();
            }
        });




        //  holder.courseIV.setImageResource(model.getCourse_image());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return orderModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class Viewholder extends RecyclerView.ViewHolder {
        //    private final ImageView courseIV;
        private final TextView OrderId;
        private final TextView ArticleName;
        private final TextView StartPoint;
        private final TextView EndPoint;
        private final TextView Date;
        private final TextView ArticleType;
        private final TextView CourierID;
        private final TextView OrderPrice;

        Button btnTakingOrder;

        public Viewholder(@NonNull View itemView) {
            super(itemView);


            OrderId = itemView.findViewById(R.id.Orderid);
            ArticleName = itemView.findViewById(R.id.articleId);
            StartPoint = itemView.findViewById(R.id.StartPoint);
            EndPoint = itemView.findViewById(R.id.EndPoint);
            Date = itemView.findViewById(R.id.date);
            ArticleType = itemView.findViewById(R.id.articleType);
            CourierID = itemView.findViewById(R.id.CourierId);
            OrderPrice = itemView.findViewById(R.id.order_price);
            this.btnTakingOrder = (Button) itemView.findViewById(R.id.btnTaking);
        }
    }
}