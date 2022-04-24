package com.nhomduan.quanlyungdungdathang.Fragment;

import static com.nhomduan.quanlyungdungdathang.Activity.FlashActivity.userLogin;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlyungdungdathang.Activity.ChiTietDonHangActivity;
import com.nhomduan.quanlyungdungdathang.Activity.HomeActivity;
import com.nhomduan.quanlyungdungdathang.Adapter.DonHangAdapter;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Interface.OnClickItem;
import com.nhomduan.quanlyungdungdathang.Model.DonHang;
import com.nhomduan.quanlyungdungdathang.Model.TrangThai;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DanhSachDonHangByTTFragment extends Fragment {
    private TrangThai trangThai;
    private RecyclerView rcvDanhSachDonHang;
    private List<DonHang> donHangList;
    private DonHangAdapter donHangAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_danh_sach_don_hang_by_t_t, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDuLieu();
        initView(view);
        setUpListDonHang();
    }

    private void getDuLieu() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            trangThai = (TrangThai) bundle.getSerializable("trang_thai");
        }

    }

    private void initView(View view) {
        rcvDanhSachDonHang = view.findViewById(R.id.rcvDonHang);
    }

    private void setUpListDonHang() {
        donHangList = new ArrayList<>();
        donHangAdapter = new DonHangAdapter(getContext(), donHangList);
        rcvDanhSachDonHang.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvDanhSachDonHang.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rcvDanhSachDonHang.setAdapter(donHangAdapter);
        UserDao.getInstance().getDonHangByUser(userLogin, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                donHangList = loc(trangThai, (List<DonHang>) obj);
                Collections.reverse(donHangList);
                donHangAdapter.setData(donHangList);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });

    }


    public List<DonHang> loc(TrangThai trangThai, List<DonHang> donHangList) {
        List<DonHang> result = new ArrayList<>();
        for (DonHang donHang : donHangList) {
            if (donHang.getTrang_thai().equals(trangThai.getTrangThai())) {
                result.add(donHang);
            }
        }
        return result;
    }

}