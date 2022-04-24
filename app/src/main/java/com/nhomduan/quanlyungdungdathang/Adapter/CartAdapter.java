package com.nhomduan.quanlyungdungdathang.Adapter;

import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nhomduan.quanlyungdungdathang.Activity.CartActivity;
import com.nhomduan.quanlyungdungdathang.Activity.HomeActivity;
import com.nhomduan.quanlyungdungdathang.Dao.ProductDao;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterUpdateObject;
import com.nhomduan.quanlyungdungdathang.Interface.OnChangeSoLuongItem;
import com.nhomduan.quanlyungdungdathang.Interface.OnClickItem;
import com.nhomduan.quanlyungdungdathang.Model.GioHang;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<GioHang> gioHangList;
    private OnClickItem onClickItem;
    private OnChangeSoLuongItem onChangeSoLuongItem;

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public CartAdapter(Context context, List<GioHang> gioHangList, OnClickItem onClickItem, OnChangeSoLuongItem onChangeSoLuongItem) {
        this.context = context;
        this.gioHangList = gioHangList;
        this.onClickItem = onClickItem;
        this.onChangeSoLuongItem = onChangeSoLuongItem;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<GioHang> gioHangList) {
        this.gioHangList = gioHangList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GioHang gioHang = gioHangList.get(position);
        if (gioHang == null) {
            return;
        }
        viewBinderHelper.bind(holder.swipeRevealLayoutCart, gioHang.getMa_sp());
        holder.tvQuantity.setText(String.valueOf(gioHang.getSo_luong()));
        ProductDao.getInstance().getProductByIdListener(gioHang.getMa_sp(), new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                if(obj != null) {
                    Product product = (Product) obj;
                    Picasso.get()
                            .load(product.getImage())
                            .placeholder(R.drawable.ic_image)
                            .into(holder.imgProduct);
                    holder.tvNameProduct.setText(product.getName());
                    if(!product.getTrang_thai().equals(OverUtils.HOAT_DONG)) {
                        holder.tvStop.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvStop.setVisibility(View.INVISIBLE);
                    }

                    if (product.getKhuyen_mai() > 0) {
                        holder.tvPriceProduct.setPaintFlags(holder.tvPriceProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.tvPriceProduct.setText(OverUtils.numberFormat.format(product.getGia_ban() * gioHang.getSo_luong()));
                    } else {
                        holder.tvPriceProduct.setVisibility(View.GONE);
                    }
                    float tienTTSP = (product.getGia_ban() - (product.getKhuyen_mai() * product.getGia_ban()));
                    holder.tvSalePriceProduct.setText(OverUtils.numberFormat.format(tienTTSP * gioHang.getSo_luong()) + " VNĐ");
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(context, ERROR_MESSAGE);
            }
        });

        // Tăng giảm số lượng sản phẩm
        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gioHang.getSo_luong() > 1) {
                    gioHang.setSo_luong(gioHang.getSo_luong() - 1);
                    onChangeSoLuongItem.onChangeItem(position, gioHang);
                }
            }
        });
        holder.btnIncrease.setOnClickListener(v -> {
            if (gioHang.getSo_luong() < 50) {
                gioHang.setSo_luong(gioHang.getSo_luong() + 1);
                onChangeSoLuongItem.onChangeItem(position, gioHang);
            }
        });

        holder.layoutDelete.setOnClickListener(v -> onClickItem.onDeleteItem(gioHang));

        holder.layoutProduct.setOnClickListener(v -> onClickItem.onClickItem(gioHang.getMa_sp()));

    }

    @Override
    public int getItemCount() {
        if (gioHangList != null) {
            return gioHangList.size();
        }
        return 0;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        private SwipeRevealLayout swipeRevealLayoutCart;
        private LinearLayout layoutDelete;
        private ConstraintLayout layoutProduct;
        private ImageView imgProduct;
        private TextView tvNameProduct;
        private TextView tvPriceProduct;
        private TextView tvSalePriceProduct;
        private TextView btnDecrease;
        private TextView tvQuantity;
        private TextView btnIncrease;
        private ImageView tvStop;





        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeRevealLayoutCart = itemView.findViewById(R.id.swipeRevealLayoutCart);
            layoutDelete = itemView.findViewById(R.id.layout_delete);
            layoutProduct = itemView.findViewById(R.id.layout_product);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            tvSalePriceProduct = itemView.findViewById(R.id.tvSalePriceProduct);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            tvStop = itemView.findViewById(R.id.tvStop);
        }
    }
}
