package com.example.ce.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.help.Tip;
import com.example.ce.R;

import java.util.List;


public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> implements View.OnClickListener {

    private final List<Tip> list;
    private final Context context;
    private final RecyclerView rv;
    private OnItemClickListener mOnItemClickListener;

    @Override
    public void onClick(View v) {
        int position = rv.getChildAdapterPosition(v);

        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(rv, v, position, list.get(position));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView parent, View view, int position, Tip data);
    }


    public RvAdapter(Context context, RecyclerView rv, List<Tip> list) {
        this.context = context;
        this.rv = rv;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }

    public void setData(List<Tip> list) {
        if (list == null) return;
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Tip tip = list.get(position);
        ((TextView) holder.itemView).setText(tip.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
