package com.nhomduan.quanlyungdungdathang.Fragment;


import static com.nhomduan.quanlyungdungdathang.Activity.FlashActivity.userLogin;
import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlyungdungdathang.Activity.ProductActivity;
import com.nhomduan.quanlyungdungdathang.Activity.SearchActivity;
import com.nhomduan.quanlyungdungdathang.Activity.ShowProductActivity;
import com.nhomduan.quanlyungdungdathang.Adapter.CategoryAdapter;
import com.nhomduan.quanlyungdungdathang.Adapter.HorizontalProductAdapter;
import com.nhomduan.quanlyungdungdathang.Dao.GioHangDao;
import com.nhomduan.quanlyungdungdathang.Dao.ProductDao;
import com.nhomduan.quanlyungdungdathang.Dao.ProductTypeDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterInsertObject;
import com.nhomduan.quanlyungdungdathang.Interface.OnAddToCard;
import com.nhomduan.quanlyungdungdathang.Interface.OnClickItem;
import com.nhomduan.quanlyungdungdathang.Interface.UpdateRecyclerView;
import com.nhomduan.quanlyungdungdathang.LocalDatabase.LocalUserDatabase;
import com.nhomduan.quanlyungdungdathang.Model.GioHang;
import com.nhomduan.quanlyungdungdathang.Model.LoaiSP;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements UpdateRecyclerView, OnClickItem, OnAddToCard {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RecyclerView recyclerViewCategoryList;

    private CategoryAdapter categoryAdapter;
    private List<LoaiSP> loaiSPList;
    private TextView tvTenNguoiDung;
    private TextView tvTimKiem;

    private RecyclerView recyclerViewPopular;
    private List<Product> popularProductList;
    private HorizontalProductAdapter popularProductAdapter;

    private ImageView imgSanPhamKhuyenMais;
    private RecyclerView rcvSanPhamKhuyenMai;
    private List<Product> khuyenMaiProductList;
    private HorizontalProductAdapter khuyenMaiAdapter;

    private ImageView imgMoiNhat;
    private RecyclerView rcvSanPhamMoiNhat;
    private List<Product> moiNhatProductList;
    private HorizontalProductAdapter moiNhatAdapter;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpTvHoTen();
        setUpTvTimKiem();
        recyclerViewCategory();
        setUpRcvPhoBien();
        setUpRcvKhuyenMai();
        setUpRcvMoiNhat();
    }

    private void setUpRcvMoiNhat() {
        moiNhatProductList = new ArrayList<>();
        moiNhatAdapter = new HorizontalProductAdapter(moiNhatProductList, this, this, OverUtils.TYPE_SP_MOI_ADAPTER);
        rcvSanPhamMoiNhat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rcvSanPhamMoiNhat.setAdapter(moiNhatAdapter);
        ProductDao.getInstance().getSanPhamMoi(10, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                moiNhatProductList = (List<Product>) obj;
                moiNhatAdapter.setData(moiNhatProductList);
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });

    }

    private void setUpRcvKhuyenMai() {
        khuyenMaiProductList = new ArrayList<>();
        khuyenMaiAdapter =
                new HorizontalProductAdapter(khuyenMaiProductList, this, this, OverUtils.TYPE_KHUYEN_MAI_ADAPTER);
        rcvSanPhamKhuyenMai.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rcvSanPhamKhuyenMai.setAdapter(khuyenMaiAdapter);

        ProductDao.getInstance().getSanPhamKhuyenMai(10, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                khuyenMaiProductList = (List<Product>) obj;
                khuyenMaiAdapter.setData(khuyenMaiProductList);
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }

    private void setUpRcvPhoBien() {
        popularProductList = new ArrayList<>();
        popularProductAdapter =
                new HorizontalProductAdapter(popularProductList, this, this, OverUtils.TYPE_PHO_BIEN_ADAPTER);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPopular.setAdapter(popularProductAdapter);

        ProductDao.getInstance().getSanPhamPhoBien(10, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                popularProductList = (List<Product>) obj;
                popularProductAdapter.setData(popularProductList);
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });

    }

    private void setUpTvTimKiem() {
        tvTimKiem.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivity(intent);
        });
    }

    private void setUpTvHoTen() {
        String hoTen = userLogin.getName();
        if (hoTen != null) {
            tvTenNguoiDung.setText("Hi " + hoTen);
        } else {
            String userName = userLogin.getUsername();
            tvTenNguoiDung.setText("Hi " + userName);
        }
    }

    private void initView(View view) {
        tvTenNguoiDung = view.findViewById(R.id.tvTenNguoiDung);
        tvTimKiem = view.findViewById(R.id.tvTimKiem);
        recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular);
        imgSanPhamKhuyenMais = view.findViewById(R.id.imgSanPhamKhuyenMais);
        rcvSanPhamKhuyenMai = view.findViewById(R.id.rcvSanPhamKhuyenMai);
        imgMoiNhat = view.findViewById(R.id.imgMoiNhat);
        rcvSanPhamMoiNhat = view.findViewById(R.id.rcvSanPhamMoiNhat);
    }

    private void recyclerViewCategory() {
        recyclerViewCategoryList = view.findViewById(R.id.recyclerViewCategoryList);
        recyclerViewCategoryList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        loaiSPList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), loaiSPList, this);
        recyclerViewCategoryList.setAdapter(categoryAdapter);

        ProductTypeDao.getInstance().getAllProductType(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                loaiSPList = (List<LoaiSP>) obj;
                categoryAdapter.setData(loaiSPList);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }


    @Override
    public void callback(String categoryId) {
        Intent intent = new Intent(getContext(), ProductActivity.class);
        intent.putExtra("categoryId", categoryId);
        startActivity(intent);
    }

    @Override
    public void onClickItem(Object obj) {
        String productId = (String) obj;
        Intent intent = new Intent(getContext(), ShowProductActivity.class);
        intent.putExtra("productId", productId);
        startActivity(intent);
    }

    @Override
    public void onDeleteItem(Object obj) {
    }

    @Override
    public void onAddToCard(Product product) {
        GioHang gioHang = new GioHang(product.getId(), 1);
        List<GioHang> gioHangList = userLogin.getGio_hang();
        if (gioHangList == null) {
            gioHangList = new ArrayList<>();
            gioHangList.add(gioHang);
            postGioHang(gioHangList);
        } else {
            boolean tonTaiGioHangCuaSP = false;
            for (GioHang dhct : gioHangList) {
                if (dhct.getMa_sp().equals(gioHang.getMa_sp())) {
                    tonTaiGioHangCuaSP = true;
                }
            }

            if (tonTaiGioHangCuaSP) {
                for (GioHang dhct : gioHangList) {
                    if (dhct.getMa_sp().equals(gioHang.getMa_sp())) {
                        int soLuong = dhct.getSo_luong() + gioHang.getSo_luong();
                        if (soLuong > 50) {
                            OverUtils.makeToast(getContext(), "Số lượng hàng của 1 sản phẩm phẩm không quá 50 sp");
                        } else {
                            dhct.setSo_luong(soLuong);
                        }

                    }
                }
                postGioHang(gioHangList);
            } else {
                gioHangList.add(gioHang);
                postGioHang(gioHangList);
            }
        }

    }

    private void postGioHang(List<GioHang> gioHangList) {
        userLogin.setGio_hang(gioHangList);
        GioHangDao.getInstance().insertGioHang(userLogin,
                userLogin.getGio_hang(),
                new IAfterInsertObject() {
                    @Override
                    public void onSuccess(Object obj) {
                        LocalUserDatabase.getInstance(getContext()).getUserDao().update(userLogin);
                        OverUtils.makeToast(getContext(), "Thêm thành công");
                    }

                    @Override
                    public void onError(DatabaseError exception) {
                        OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                    }
                });
    }


}