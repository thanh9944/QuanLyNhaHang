package com.nhomduan.quanlyungdungdathang.Activity;

import static com.nhomduan.quanlyungdungdathang.Activity.FlashActivity.userLogin;
import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlyungdungdathang.Adapter.DonHangAdapter;
import com.nhomduan.quanlyungdungdathang.Adapter.OrderAdapter;
import com.nhomduan.quanlyungdungdathang.Dao.OrderDao;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Model.DonHang;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private List<DonHang> donHangList;
    private DonHangAdapter donHangAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        getListDonHang();
    }

    private void initView() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView = findViewById(R.id.rv_donhang);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getListDonHang() {
        donHangList = new ArrayList<>();
        donHangAdapter = new DonHangAdapter(OrderActivity.this, donHangList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(OrderActivity.this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(donHangAdapter);
        UserDao.getInstance().getDonHangByUser(userLogin, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                donHangList = (List<DonHang>) obj;
                Collections.reverse(donHangList);
                donHangAdapter.setData(donHangList);
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(OrderActivity.this, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}