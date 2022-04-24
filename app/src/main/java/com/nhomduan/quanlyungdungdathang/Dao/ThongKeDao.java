package com.nhomduan.quanlyungdungdathang.Dao;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterQuery;
import com.nhomduan.quanlyungdungdathang.Model.DonHang;


import java.util.ArrayList;
import java.util.List;

public class ThongKeDao {
    private static ThongKeDao instance;

    private ThongKeDao() {
    }

    public static ThongKeDao getInstance() {
        if (instance == null) {
            instance = new ThongKeDao();
        }
        return instance;
    }

    public void getDonHangByTime(double thoiGianBD, double thoiGianKT, IAfterQuery iAfterQuery) {
        Query query = FirebaseDatabase.getInstance().getReference().child("don_hang")
                .orderByChild("thoiGianGiaoHang").startAt(thoiGianBD).endAt(thoiGianKT);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DonHang> donHangList = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()) {
                    DonHang donHang = data.getValue(DonHang.class);
                    donHangList.add(donHang);
                }
                iAfterQuery.onResult(donHangList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iAfterQuery.onError(error);
            }
        });
    }
}
