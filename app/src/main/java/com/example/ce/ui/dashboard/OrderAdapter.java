package com.example.ce.ui.dashboard;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.example.ce.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<DashboardViewModel> orderModelArrayList;

    // Constructor
    public OrderAdapter(Context context, ArrayList<DashboardViewModel> orderModelArrayList) {
        this.context = context;
        this.orderModelArrayList = orderModelArrayList;
    }

    @NonNull
    @Override
    public OrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        DashboardViewModel model = orderModelArrayList.get(position);


        holder.OrderId.setText(model.getOrderId());
        holder.ArticleName.setText(model.getArticleId());
        holder.StartPoint.setText(model.getStartPoint());
        holder.EndPoint.setText(model.getEndPoint());
        holder.Date.setText(model.getDatetime());
        holder.ArticleType.setText(model.getArticleType());
        holder.CourierID.setText(model.getCourierId());
        holder.OrderPrice.setText(""+model.getOrder_price());




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
        }
    }
}

