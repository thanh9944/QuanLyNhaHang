package com.nhomduan.quanlyungdungdathang.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class OrdersStatusAdapter extends FragmentStatePagerAdapter {

    public OrdersStatusAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        switch (position) {
            case 0:
                title = "Chờ xác nhận";
                break;
            case 1:
                title = "Chế biến";
                break;
            case 2:
                title = "Giao hàng";
                break;
            case 3:
                title = "Hoàn tất";
                break;
        }
        return title;
    }
    @Override
    public int getCount () {
        return 4;
    }
}
