package com.nhomduan.quanlyungdungdathang.Dao;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterDeleteObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterInsertObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterUpdateObject;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ProductDao {
    private static ProductDao instance;

    private ProductDao() {
    }

    public static ProductDao getInstance() {
        if (instance == null) {
            instance = new ProductDao();
        }
        return instance;
    }

    public void getAllProduct(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("san_pham")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Product> result = new ArrayList<>();
                        for (DataSnapshot obj : snapshot.getChildren()) {
                            Product product = obj.getValue(Product.class);
                            if (product != null) {
                                result.add(product);
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

    public void getProductById(String id, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("san_pham").child(id)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    Product product = snapshot.getValue(Product.class);
                    iAfterGetAllObject.iAfterGetAllObject(product);
                } else {
                    iAfterGetAllObject.iAfterGetAllObject(null);
                }
            } else {
                iAfterGetAllObject.iAfterGetAllObject(null);
            }
        });
    }


    public void getProductByIdListener(String id, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("san_pham").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Product product = snapshot.getValue(Product.class);
                        iAfterGetAllObject.iAfterGetAllObject(product);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }

    public void insertProduct(Product product, IAfterInsertObject iAfterInsertObject) {
        FirebaseDatabase.getInstance().getReference().child("san_pham").child(product.getId())
                .setValue(product, (error, ref) -> {
                    if (error == null) {
                        iAfterInsertObject.onSuccess(product);
                    } else {
                        iAfterInsertObject.onError(error);
                    }

                });
    }

    public void updateProduct(Product product, Map<String, Object> map, IAfterUpdateObject iAfterUpdateObject) {
        FirebaseDatabase.getInstance().getReference().child("san_pham").child(product.getId())
                .updateChildren(map, (error, ref) -> {
                    if (error == null) {
                        iAfterUpdateObject.onSuccess(product);
                    } else {
                        iAfterUpdateObject.onError(error);
                    }
                });
    }

    public void updateProduct(Product product, Map<String, Object> map) {
        FirebaseDatabase.getInstance().getReference().child("san_pham").child(product.getId())
                .updateChildren(map);
    }

    public void deleteProduct(Context context, Product product, IAfterDeleteObject iAfterDeleteObject) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc chắn muốn xóa?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa", (dialog, i) -> {
                    FirebaseDatabase.getInstance().getReference().child("san_pham").child(product.getId()).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                iAfterDeleteObject.onSuccess(product);
                            } else {
                                iAfterDeleteObject.onError(error);
                            }
                        }
                    });
                })
                .show();
    }

    public void getSanPhamMoi(int soLuong, IAfterGetAllObject iAfterGetAllObject) {
        Query query = FirebaseDatabase.getInstance().getReference().child("san_pham")
                .limitToLast(soLuong);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> result = new ArrayList<>();
                for (DataSnapshot obj : snapshot.getChildren()) {
                    Product product = obj.getValue(Product.class);
                    if (product != null) {
                        result.add(product);
                    }
                }
                List<Product> resultList = OverUtils.filterProduct2(result);
                Collections.reverse(resultList);
                iAfterGetAllObject.iAfterGetAllObject(resultList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iAfterGetAllObject.onError(error);
            }
        });
    }

    public void getSanPhamKhuyenMai(int soLuong, IAfterGetAllObject iAfterGetAllObject) {
        Query query = FirebaseDatabase.getInstance().getReference().child("san_pham")
                .orderByChild("khuyen_mai").startAfter(0);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> result = new ArrayList<>();
                for (DataSnapshot obj : snapshot.getChildren()) {
                    Product product = obj.getValue(Product.class);
                    if (product != null) {
                        result.add(product);
                    }
                }
                List<Product> resultList = OverUtils.filterProduct(result);
                Collections.sort(resultList, (o1, o2) -> {
                    if (o1.getKhuyen_mai() > o2.getKhuyen_mai()) {
                        return -1;
                    } else if (o1.getKhuyen_mai() < o2.getKhuyen_mai()) {
                        return 1;
                    } else {
                        return 0;
                    }
                });

                if (soLuong >= resultList.size()) {
                    iAfterGetAllObject.iAfterGetAllObject(resultList);
                } else {
                    iAfterGetAllObject.iAfterGetAllObject(resultList.subList(0, soLuong - 1));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iAfterGetAllObject.onError(error);
            }
        });
    }

    public void getSanPhamPhoBien(int soLuong, IAfterGetAllObject iAfterGetAllObject) {
        Query query = FirebaseDatabase.getInstance().getReference().child("san_pham");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> result = new ArrayList<>();
                for (DataSnapshot obj : snapshot.getChildren()) {
                    Product product = obj.getValue(Product.class);
                    if (product != null) {
                        result.add(product);
                    }
                }
                List<Product> resultList = OverUtils.filterProduct(result);
                List<Product> resultList2 = OverUtils.filterProduct3(resultList);
                Collections.sort(resultList2,
                        (o1, o2) -> Integer.compare(o2.getSo_luong_da_ban(), o1.getSo_luong_da_ban()));
                if (soLuong >= resultList2.size()) {
                    iAfterGetAllObject.iAfterGetAllObject(resultList2);
                } else {
                    iAfterGetAllObject.iAfterGetAllObject(resultList2.subList(0, soLuong - 1));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iAfterGetAllObject.onError(error);
            }
        });
    }

    public void getProductByType(String idLoaiSP, IAfterGetAllObject iAfterGetAllObject) {
        Query query = FirebaseDatabase.getInstance().getReference().child("san_pham")
                .orderByChild("loai_sp").equalTo(idLoaiSP);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> result = new ArrayList<>();
                for (DataSnapshot obj : snapshot.getChildren()) {
                    Product product = obj.getValue(Product.class);
                    if (product != null) {
                        result.add(product);
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

}
