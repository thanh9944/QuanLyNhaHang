package com.nhomduan.quanlyungdungdathang.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nhomduan.quanlyungdungdathang.Fragment.DanhSachDonHangByTTFragment;
import com.nhomduan.quanlyungdungdathang.Model.TrangThai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DanhSachDonHangPagerAdapter extends FragmentStateAdapter {
    List<TrangThai> trangThaiList;

    public DanhSachDonHangPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<TrangThai> trangThaiList) {
        super(fragmentActivity);
        this.trangThaiList = trangThaiList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        DanhSachDonHangByTTFragment fragment = new DanhSachDonHangByTTFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("trang_thai", trangThaiList.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        if(trangThaiList != null) {
            return trangThaiList.size();
        }
        return 0;
    }
}