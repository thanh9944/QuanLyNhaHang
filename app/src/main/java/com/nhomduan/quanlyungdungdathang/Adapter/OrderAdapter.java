package com.nhomduan.quanlyungdungdathang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlyungdungdathang.Model.DonHang;
import com.nhomduan.quanlyungdungdathang.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<DonHang> list;

    public OrderAdapter(Context context, List<DonHang> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        DonHang donHang = list.get(position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<DonHang> list) {
        this.list=list;
        notifyDataSetChanged();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tenSP, sl;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgProductDonhang);
            tenSP = itemView.findViewById(R.id.tv_tenDonhang);
            sl = itemView.findViewById(R.id.tv_soLuongProductDonhang);
        }
    }
}
