package com.nhomduan.quanlyungdungdathang.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlyungdungdathang.Activity.ShowProductActivity;
import com.nhomduan.quanlyungdungdathang.Interface.OnClickItem;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterProductAdapter extends RecyclerView.Adapter<FilterProductAdapter.FilterProductViewHolder> implements Filterable {
    //
    private List<Product> productList = new ArrayList<>();
    private List<Product> mainProductList;
    private OnClickItem onClickItem;

    public FilterProductAdapter(List<Product> mainProductList, OnClickItem onClickItem) {
        this.mainProductList = mainProductList;
        this.onClickItem = onClickItem;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Product> mainProductList) {
        this.mainProductList = mainProductList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilterProductAdapter.FilterProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_product,parent,false);
        return new FilterProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterProductAdapter.FilterProductViewHolder holder, int position) {
        Product product = productList.get(position);
        Picasso.get()
                .load(product.getImage())
                .placeholder(R.drawable.ic_image)
                .into(holder.imgProduct);
        holder.tvNameProduct.setText(product.getName());
        holder.tvTimeProduct.setText(product.getThoiGianCheBien() +" min");
        holder.tvSoNguoiThichSP.setText(String.valueOf(product.getRate()));
        holder.tvSoNguoiMuaSP.setText(String.valueOf(product.getSo_luong_da_ban()));

        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(locale);
        holder.tvPriceProduct.setText(currencyFormat.format((int)(product.getGia_ban()-product.getGia_ban()*product.getKhuyen_mai()))+" VNĐ");

        holder.viewHolderProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onClickItem(product.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(productList != null) {
            return productList.size();
        }
        return 0;
    }
//test
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                List<Product> tempList = new ArrayList<>();
                if(!strSearch.isEmpty()) {
                    for(Product pr : mainProductList) {
                        if(pr.getName().toLowerCase().contains(strSearch.toLowerCase().trim())) {
                            tempList.add(pr);
                        }
                    }
                }
                productList = tempList;
                FilterResults filterResults = new FilterResults();
                filterResults.values = productList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productList = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    protected static class FilterProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvNameProduct,tvTimeProduct,tvPriceProduct;
        ConstraintLayout viewHolderProduct;

        private TextView tvSoNguoiThichSP;
        private TextView tvSoNguoiMuaSP;

        public FilterProductViewHolder(@NonNull View itemView) {
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
