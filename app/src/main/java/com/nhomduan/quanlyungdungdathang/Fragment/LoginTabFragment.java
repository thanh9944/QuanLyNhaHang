package com.nhomduan.quanlyungdungdathang.Fragment;

import static com.nhomduan.quanlyungdungdathang.Activity.FlashActivity.userLogin;
import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlyungdungdathang.Activity.HomeActivity;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.LocalDatabase.LocalUserDatabase;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.LoginViewModel;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;


public class LoginTabFragment extends Fragment {

    private EditText edtTenDangNhap, edtMatKhau;
    private TextView tvQuenMatKhau;
    private Button btnDangNhap, btnHuyDangNhap;
    private ToggleButton btnCheckPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpSignupData();

        // cài đặt các animation cho các view
        setUpViewAnimation();

        setUpBtnCheckPass();
        setUpBtnLogin();
        setUpBtnCancel();
    }

    private void initView(View view) {
        edtTenDangNhap = view.findViewById(R.id.edtTenDangNhap);
        edtMatKhau = view.findViewById(R.id.edtMatKhau);
        tvQuenMatKhau = view.findViewById(R.id.tvQuenMatKhau);
        btnDangNhap = view.findViewById(R.id.btnDangNhap);
        btnCheckPass = view.findViewById(R.id.btnCheckPass);
        btnHuyDangNhap = view.findViewById(R.id.btnHuyDangNhap);
    }

    private void setUpSignupData() {
        LoginViewModel loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        loginViewModel.getData().observe(getViewLifecycleOwner(), user -> {
            if(user != null) {
                edtTenDangNhap.setText(user.getUsername());
                edtMatKhau.setText(user.getPassword());
            }
        });
    }

    private void setUpViewAnimation() {
        // cài đặt vị trí đứng của các view
        edtTenDangNhap.setTranslationX(800);
        edtMatKhau.setTranslationX(800);
        tvQuenMatKhau.setTranslationX(800);
        btnCheckPass.setTranslationX(800);
        btnDangNhap.setTranslationX(800);
        btnHuyDangNhap.setTranslationX(800);

        // độ mờ của view
        float alpha = 0;

        // cài đặt độ mời của view theo biến alpha
        edtTenDangNhap.setAlpha(alpha);
        edtMatKhau.setAlpha(alpha);
        tvQuenMatKhau.setAlpha(alpha);
        btnCheckPass.setAlpha(alpha);
        btnDangNhap.setAlpha(alpha);
        btnHuyDangNhap.setAlpha(alpha);

        // cài đặt animate cho các view
        edtTenDangNhap.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        edtMatKhau.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        tvQuenMatKhau.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        btnCheckPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        btnDangNhap.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        btnHuyDangNhap.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
    }

    private void setUpBtnCheckPass() {
        btnCheckPass.setOnClickListener(v -> {
            if (btnCheckPass.isChecked()) {
                edtMatKhau.setTransformationMethod(null);
            } else {
                edtMatKhau.setTransformationMethod(new PasswordTransformationMethod());
            }
        });
    }

    private void setUpBtnLogin() {
        btnDangNhap.setOnClickListener(v -> {
            String userName = edtTenDangNhap.getText().toString().trim();
            String password = edtMatKhau.getText().toString().trim();
            if (validateInput(userName, password)) {
                UserDao.getInstance().getUserByUserName(userName, new IAfterGetAllObject() {
                    @Override
                    public void iAfterGetAllObject(Object obj) {
                        if (obj == null) {
                            OverUtils.makeToast(getContext(), "Tài khoản không tồn tại");
                            return;
                        }

                        User user = (User) obj;
                        if (user.getPassword().equals(password)) {
                            storageAccount(user);
                            userLogin = user;
                            SharedPreferences.Editor editor = OverUtils.getSPInstance(getContext(), OverUtils.PASS_FILE).edit();
                            editor.putString("pass", OverUtils.PASS_LOGIN_ACTIVITY);
                            editor.apply();
                            goToHomeActivity();
                        } else {
                            OverUtils.makeToast(getContext(), "Vui lòng kiểm tra lại mật khẩu");
                        }

                    }

                    @Override
                    public void onError(DatabaseError error) {
                        OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                    }
                });
            }
        });
    }

    private void setUpBtnCancel() {
        btnHuyDangNhap.setOnClickListener(v -> {
            edtTenDangNhap.setText("");
            edtMatKhau.setText("");
        });
    }

    private boolean validateInput(String userName, String password) {
        if (userName.isEmpty() || password.isEmpty()) {
            OverUtils.makeToast(getContext(), "Quý khánh vui lòng nhập đầy đủ thông tin");
            return false;
        }
        return true;
    }

    private void storageAccount(User user) {
        LocalUserDatabase.getInstance(getContext()).getUserDao().insert(user);
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}
