package com.nhomduan.quanlyungdungdathang.Fragment;


import static com.nhomduan.quanlyungdungdathang.Activity.FlashActivity.userLogin;
import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlyungdungdathang.Adapter.FavoriteProductAdapter;
import com.nhomduan.quanlyungdungdathang.Dao.ProductDao;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterUpdateObject;
import com.nhomduan.quanlyungdungdathang.Interface.ItemTouchHelpListener;
import com.nhomduan.quanlyungdungdathang.LocalDatabase.LocalUserDatabase;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.nhomduan.quanlyungdungdathang.Utils.RecyclerViewItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class LikeProductFragment extends Fragment implements ItemTouchHelpListener {

    private RecyclerView rcvSanPhamYeuThich;
    private LinearLayout viewRoot;

    private List<Product> productList;
    private FavoriteProductAdapter favoriteProductAdapter;
    List<String> maSanPhamYeuThichList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_like_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpListSPYeuThich();
    }

    private void initView(View view) {
        rcvSanPhamYeuThich = view.findViewById(R.id.rcvSanPhamYeuThich);
        viewRoot = view.findViewById(R.id.viewRoot);
    }

    private void setUpListSPYeuThich() {
        productList = new ArrayList<>();
        favoriteProductAdapter = new FavoriteProductAdapter(getContext(), productList);
        rcvSanPhamYeuThich.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvSanPhamYeuThich.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rcvSanPhamYeuThich.setAdapter(favoriteProductAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback =
                new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvSanPhamYeuThich);


        UserDao.getInstance().getSanPhamYeuThichOfUser(userLogin, new IAfterGetAllObject() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void iAfterGetAllObject(Object obj) {
                maSanPhamYeuThichList = (List<String>) obj;
                favoriteProductAdapter.notifyDataSetChanged();
                productList.clear();
                for (String maSP : maSanPhamYeuThichList) {
                    ProductDao.getInstance().getProductById(maSP, new IAfterGetAllObject() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void iAfterGetAllObject(Object obj) {
                            if (obj != null) {
                                Product product = (Product) obj;
                                productList.add(product);
                                if (product.getId() != null) {
                                    boolean duplicate = false;
                                    for (int i = 0; i < productList.size(); i++) {
                                        if (product.getId().equals(productList.get(i).getId())) {
                                            duplicate = true;
                                        }
                                    }
                                    if (!duplicate) {
                                        productList.add(product);
                                    }
                                    favoriteProductAdapter.notifyItemInserted(productList.size() - 1);
                                }
                            }
                        }

                        @Override
                        public void onError(DatabaseError error) {
                            OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                        }
                    });
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof FavoriteProductAdapter.ViewHolder) {
            final Product product = productList.get(viewHolder.getBindingAdapterPosition());

            // remove item
            // update list liked product
            maSanPhamYeuThichList.remove(product.getId());
            userLogin.setMa_sp_da_thich(maSanPhamYeuThichList);
            UserDao.getInstance().updateUser(userLogin, userLogin.toMapSPDaThich());

            // update rate of product
            product.setRate(product.getRate() - 1);
            ProductDao.getInstance().updateProduct(product, product.toMapRate());

            // undo item
            Snackbar snackbar = Snackbar.make(viewRoot, product.getName() + " removed !", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // update list liked product
                    maSanPhamYeuThichList.add(product.getId());
                    userLogin.setMa_sp_da_thich(maSanPhamYeuThichList);
                    UserDao.getInstance().updateUser(userLogin, userLogin.toMapSPDaThich());

                    // update rate of product
                    product.setRate(product.getRate() + 1);
                    ProductDao.getInstance().updateProduct(product, product.toMapRate());
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}