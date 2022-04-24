package com.nhomduan.quanlyungdungdathang.LocalDatabase;

import androidx.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.nhomduan.quanlyungdungdathang.Model.GioHang;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GioHangTypeConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public String gioHangToJson(List<GioHang> gioHangList) {
        return gson.toJson(gioHangList);
    }

    @TypeConverter
    public List<GioHang> jsonToGioHang(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<ArrayList<GioHang>>() {}.getType();
        return gson.fromJson(data, listType);
    }
}
