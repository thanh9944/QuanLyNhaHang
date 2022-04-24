package com.nhomduan.quanlyungdungdathang.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nhomduan.quanlyungdungdathang.Adapter.LoginAdapter;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.LoginViewModel;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView fb, google, twitter;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    callPhone();
                } else {
                    OverUtils.makeToast(LoginActivity.this, "Không có quyền gọi điện");
                }
            });

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setUpTabLauout();
        setUpViewAnimation();
        triggerCallPhone();
    }

    private void initView() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        fb = findViewById(R.id.fab_fb);
        google = findViewById(R.id.fab_google);
        twitter = findViewById(R.id.fab_twitter);
    }

    private void setUpTabLauout() {
        tabLayout.addTab(tabLayout.newTab().setText("Đăng nhập"));
        tabLayout.addTab(tabLayout.newTab().setText("Đăng ký"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewAnimation() {
        fb.setTranslationY(300);
        google.setTranslationY(300);
        twitter.setTranslationY(300);
        tabLayout.setTranslationY(300);

        float v = 0;

        fb.setAlpha(v);
        google.setAlpha(v);
        twitter.setAlpha(v);
        tabLayout.setAlpha(v);


        fb.animate().translationY(0).alpha(1).setDuration(2000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(2000).setStartDelay(600).start();
        twitter.animate().translationY(0).alpha(1).setDuration(2000).setStartDelay(800).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(2000).setStartDelay(100).start();
    }

    private void triggerCallPhone() {
        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("CALL_PHONE")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        callPhone();
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
                    }
                } else {
                    callPhone();
                }
            }
        }
    }

    private void callPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:1900 1000"));
        startActivity(intent);
    }


}