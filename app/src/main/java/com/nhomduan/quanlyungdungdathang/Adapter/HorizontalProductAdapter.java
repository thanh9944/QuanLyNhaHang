package com.nhomduan.quanlyungdungdathang.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlyungdungdathang.Interface.OnAddToCard;
import com.nhomduan.quanlyungdungdathang.Interface.OnClickItem;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HorizontalProductAdapter extends RecyclerView.Adapter<HorizontalProductAdapter.PopularViewHolder>{
    private List<Product> productList;
    private OnClickItem onClickItem;
    private OnAddToCard onAddToCard;
    private int type;

    public HorizontalProductAdapter(List<Product> productList, OnClickItem onClickItem, OnAddToCard onAddToCard, int type) {
        this.productList = productList;
        this.onClickItem = onClickItem;
        this.onAddToCard = onAddToCard;
        this.type = type;
    }

    public void setData(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular, parent, false);
        return new PopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductAdapter.PopularViewHolder holder, int position) {
        Product product = productList.get(position);
        if(product == null) {
            return;
        }

        Picasso.get().load(product.getImage()).into(holder.imgProduct);
        holder.tvNameProduct.setText(product.getName());
        holder.tvPriceProduct.setText(OverUtils.numberFormat.format(product.getGia_ban() - (product.getGia_ban() * product.getKhuyen_mai())));
        holder.item.setOnClickListener(v -> onClickItem.onClickItem(product.getId()));

        holder.btnAddProduct.setOnClickListener(v -> onAddToCard.onAddToCard(product));
        if(type == 0) {
            holder.tvSoLuongDanBan.setText(product.getSo_luong_da_ban() + " ps");
            holder.tvNew.setVisibility(View.INVISIBLE);
            holder.tvKhuyenMai.setVisibility(View.INVISIBLE);
        } else if(type == 1) {
            holder.tvSoLuongDanBan.setVisibility(View.GONE);
            holder.tvKhuyenMai.setVisibility(View.VISIBLE);
            holder.tvKhuyenMai.setText("Sale " + (int) (product.getKhuyen_mai() * 100) + "%");
            holder.tvNew.setVisibility(View.INVISIBLE);
        } else if(type == 2) {
            holder.tvSoLuongDanBan.setVisibility(View.GONE);
            holder.tvKhuyenMai.setVisibility(View.INVISIBLE);
            holder.tvNew.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if(productList != null) {
            return productList.size();
        }
        return 0;
    }

    public class PopularViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout item;
        private ImageView imgProduct;
        private TextView tvNameProduct;
        private TextView tvPriceProduct;
        private TextView btnAddProduct;
        private TextView tvSoLuongDanBan;
        private TextView tvKhuyenMai;
        private TextView tvNew;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            btnAddProduct = itemView.findViewById(R.id.btnAddProduct);
            tvSoLuongDanBan = itemView.findViewById(R.id.tvSoLuongDanBan);
            tvKhuyenMai = itemView.findViewById(R.id.tvKhuyenMai);
            tvNew = itemView.findViewById(R.id.tvNew);
        }
    }
}
