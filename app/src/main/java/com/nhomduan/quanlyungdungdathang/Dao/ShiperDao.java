package com.nhomduan.quanlyungdungdathang.Dao;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterDeleteObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterInsertObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterUpdateObject;
import com.nhomduan.quanlyungdungdathang.Model.Shipper;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShiperDao {
    private static ShiperDao instance;

    private ShiperDao() {
    }

    public static ShiperDao getInstance() {
        if (instance == null) {
            instance = new ShiperDao();
        }
        return instance;
    }

    public void getAllShipper(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("shipper")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Shipper> result = new ArrayList<>();
                        result.add(0, new Shipper("", "Chọn shipper ", ""));
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Shipper shipper = data.getValue(Shipper.class);
                            if (shipper != null) {
                                result.add(shipper);
                            }
                        }
                        iAfterGetAllObject.iAfterGetAllObject(result);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }

    public void getShipperById(String id, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("shipper").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Shipper shipper = snapshot.getValue(Shipper.class);
                        iAfterGetAllObject.iAfterGetAllObject(shipper);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }

    public void insertShipper(Shipper shipper, IAfterInsertObject inAfterInsertObject) {
        FirebaseDatabase.getInstance().getReference().child("shipper").child(shipper.getId())
                .setValue(shipper, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            inAfterInsertObject.onSuccess(shipper);
                        } else {
                            inAfterInsertObject.onError(error);
                        }

                    }
                });
    }

    public void updateShipper(Shipper shipper, Map<String, Object> map, IAfterUpdateObject inAfterUpdateObject) {
        FirebaseDatabase.getInstance().getReference().child("shipper").child(shipper.getId())
                .updateChildren(map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            inAfterUpdateObject.onSuccess(shipper);
                        } else {
                            inAfterUpdateObject.onError(error);
                        }
                    }
                });
    }

    public void deleteShiper(Context context, Shipper shipper, IAfterDeleteObject iAfterDeleteObject) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc chắn muốn xóa?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa", (dialog, i) -> {
                    FirebaseDatabase.getInstance().getReference().child("shipper").child(shipper.getId())
                            .removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error == null) {
                                        iAfterDeleteObject.onSuccess(shipper);
                                    } else {
                                        iAfterDeleteObject.onError(error);
                                    }
                                }
                            });
                })
                .show();
    }

}
