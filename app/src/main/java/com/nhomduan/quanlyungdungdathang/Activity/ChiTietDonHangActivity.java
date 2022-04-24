package com.nhomduan.quanlyungdungdathang.Activity;

import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlyungdungdathang.Adapter.ChiTietDonHangAdapter;
import com.nhomduan.quanlyungdungdathang.Dao.OrderDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterUpdateObject;
import com.nhomduan.quanlyungdungdathang.Model.DonHang;
import com.nhomduan.quanlyungdungdathang.Model.Shipper;
import com.nhomduan.quanlyungdungdathang.Model.TrangThai;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;

import java.util.Date;

public class ChiTietDonHangActivity extends AppCompatActivity {

    private Toolbar toolbarChiTietDonHang;
    private TextView tvHoTen;
    private TextView tvDiaChi;
    private TextView tvSDT;
    private TextView tvThoiGianDatHang;
    private TextView tvShipper;
    private TextView tvTitleShipper;
    private TextView tvTongSoSanPham;
    private TextView tvTien;
    private TextView tvPhiVanChuyen;
    private TextView tvTongTien;
    private TextView tvMaGiamGia;
    private TextView tvGhiChu;
    private TextView tvHuyDon;
    private TextView tvThoiGianGiaoHang;
    private LinearLayout lyThoiGianGiaoHang;
    private TextView tvTTHuyDon;

    private RecyclerView rcvChiTietDonHang;
    private ChiTietDonHangAdapter chiTietDonHangAdapter;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);
        init();
        getDuLieu();
        getDonHangDaChon();
    }

    private void getDuLieu() {
        Intent intent = getIntent();
        id = intent.getStringExtra("donHangID");
    }


    private void getDonHangDaChon() {
        OrderDao.getInstance().getDonHangById(id, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                DonHang donHang = (DonHang) obj;

                tvHoTen.setText(donHang.getHo_ten());
                tvDiaChi.setText(donHang.getDia_chi());
                tvSDT.setText(donHang.getSdt());
                if(donHang.getThoiGianGiaoHangDuKien() != 0) {
                    tvThoiGianDatHang.setText(OverUtils.getSimpleDateFormat().format(new Date(donHang.getThoiGianGiaoHangDuKien())));
                } else {
                    tvThoiGianDatHang.setText(donHang.getThoiGianDatHang());
                }
                tvTien.setText(donHang.getTong_tien() + "đ");
                tvTongTien.setText(donHang.getTong_tien() + "đ");
                tvTongSoSanPham.setText("Tổng " + donHang.getDon_hang_chi_tiets().size() + " sản phẩm");

                chiTietDonHangAdapter = new ChiTietDonHangAdapter(ChiTietDonHangActivity.this, donHang.getDon_hang_chi_tiets());
                rcvChiTietDonHang.setAdapter(chiTietDonHangAdapter);

                if(!donHang.getTrang_thai().equals(TrangThai.CHUA_XAC_NHAN.getTrangThai())) {
                    tvHuyDon.setVisibility(View.GONE);
                }

                if(donHang.getThong_tin_huy_don() == null) {
                    tvTTHuyDon.setText("None");
                } else {
                    tvTTHuyDon.setText(donHang.getThong_tin_huy_don());
                }

                tvHuyDon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        donHang.setTrang_thai(TrangThai.HUY_DON.getTrangThai());
                        if(donHang.getThong_tin_huy_don() == null) {
                            donHang.setThong_tin_huy_don("Do khách hàng hủy khi đơn hàng chưa xác nhận(" +
                                    OverUtils.getSimpleDateFormat().format(new Date(System.currentTimeMillis())) + ")");
                        } else {
                            donHang.setThong_tin_huy_don(donHang.getThong_tin_huy_don() + " // " + "Do khách hàng hủy khi đơn hàng chưa xác nhận(" +
                                    OverUtils.getSimpleDateFormat().format(new Date(System.currentTimeMillis())) + ")");
                        }

                        OrderDao.getInstance().updateDonHang(donHang, donHang.toMapHuyDon(), new IAfterUpdateObject() {
                            @Override
                            public void onSuccess(Object obj) {
                                OverUtils.makeToast(ChiTietDonHangActivity.this, "Hủy thành công đơn hàng");
                                Intent intent = new Intent(ChiTietDonHangActivity.this, HomeActivity.class);
                                intent.setAction(OverUtils.GO_TO_ORDER_FRAGMENT);
                                startActivity(intent);
                            }

                            @Override
                            public void onError(DatabaseError error) {
                                OverUtils.makeToast(ChiTietDonHangActivity.this, ERROR_MESSAGE);
                            }
                        });
                    }
                });

                if (donHang.getGhi_chu() == null) {
                    tvGhiChu.setText("None");
                } else if (donHang.getGhi_chu() != null || donHang.getGhi_chu().isEmpty()) {
                    tvGhiChu.setText(donHang.getGhi_chu());
                }

                Shipper shipper = donHang.getShipper();
                if (shipper != null) {
                    tvShipper.setText(shipper.getName() + " - " + shipper.getPhone_number());
                    tvShipper.setVisibility(View.VISIBLE);
                    tvTitleShipper.setVisibility(View.VISIBLE);
                } else {
                    tvShipper.setVisibility(View.GONE);
                    tvTitleShipper.setVisibility(View.GONE);
                }

                if (donHang.getThoiGianGiaoHang() != 0) {
                    lyThoiGianGiaoHang.setVisibility(View.VISIBLE);
                    tvThoiGianGiaoHang.setText(OverUtils.getSimpleDateFormat().format(new Date(donHang.getThoiGianGiaoHang())));
                } else {
                    lyThoiGianGiaoHang.setVisibility(View.GONE);
                }


            }

            @Override
            public void onError(DatabaseError error) {

            }
        });

    }

    private void init() {
        toolbarChiTietDonHang = findViewById(R.id.toolbar_ChiTietDonHang);
        tvHoTen = findViewById(R.id.tvHoTen_ChiTietDonHang);
        tvDiaChi = findViewById(R.id.tvDiaChi_ChiTietDonHang);
        tvSDT = findViewById(R.id.tvSDT_ChiTietDonHang);
        tvThoiGianDatHang = findViewById(R.id.tvThoiGianDatHang_ChiTietGiaoHang);
        tvTongSoSanPham = findViewById(R.id.tvTongSoSanPham_ChiTietGiaoHang);
        tvTien = findViewById(R.id.tvTien_ChiTietDonHang);
        tvPhiVanChuyen = findViewById(R.id.tvPhiVanChuyen_ChiTietGiaoHang);
        tvTongTien = findViewById(R.id.tvTongTien_ChiTietDonHang);
        tvMaGiamGia = findViewById(R.id.tvMaGiamGia_ChiTietDonHang);
        tvGhiChu = findViewById(R.id.tv_GhiChu_ChiTietDonHang);
        tvShipper = findViewById(R.id.tvShipper_ChiTietDonHang);
        tvTitleShipper = findViewById(R.id.tvTitleShipper);
        tvThoiGianGiaoHang = findViewById(R.id.tvThoiGianGiaoHang_ChiTietGiaoHang);
        lyThoiGianGiaoHang = findViewById(R.id.layout_tggh);
        tvHuyDon = findViewById(R.id.tvHuyDon);
        tvTTHuyDon = findViewById(R.id.tv_TTHuyDon);


        rcvChiTietDonHang = findViewById(R.id.rcv_ChiTietDonHang);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChiTietDonHangActivity.this);
        rcvChiTietDonHang.setLayoutManager(linearLayoutManager);
        rcvChiTietDonHang.addItemDecoration(new DividerItemDecoration(ChiTietDonHangActivity.this, DividerItemDecoration.VERTICAL));


        toolbarChiTietDonHang.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}