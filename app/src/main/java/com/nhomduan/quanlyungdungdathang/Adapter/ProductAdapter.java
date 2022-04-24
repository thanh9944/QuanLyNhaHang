package com.nhomduan.quanlyungdungdathang.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlyungdungdathang.Activity.ShowProductActivity;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    List<Product> productList;

    public ProductAdapter(Context context, List<Product> list) {
        this.context = context;
        this.productList = list;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        Picasso.get()
                .load(product.getImage())
                .placeholder(R.drawable.ic_image)
                .into(holder.imgProduct);
        holder.tvNameProduct.setText(product.getName());
        holder.tvTimeProduct.setText(product.getThoiGianCheBien() + " phút");
        holder.tvSoNguoiThichSP.setText(String.valueOf(product.getRate()));
        holder.tvSoNguoiMuaSP.setText(String.valueOf(product.getSo_luong_da_ban()));

        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(locale);
        holder.tvPriceProduct.setText(currencyFormat.format((int) (product.getGia_ban() - product.getGia_ban() * product.getKhuyen_mai())) + " VNĐ");

        holder.viewHolderProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowProductActivity.class);
                intent.putExtra("productId", product.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView tvNameProduct, tvTimeProduct, tvPriceProduct;
        ConstraintLayout viewHolderProduct;
        TextView tvSoNguoiThichSP;
        TextView tvSoNguoiMuaSP;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvTimeProduct = itemView.findViewById(R.id.tvTimeProduct);
            viewHolderProduct = itemView.findViewById(R.id.viewHolderProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            tvSoNguoiThichSP = itemView.findViewById(R.id.tvSoNguoiThichSP);
            tvSoNguoiMuaSP = itemView.findViewById(R.id.tvSoNguoiMuaSP);
        }
    }
}
