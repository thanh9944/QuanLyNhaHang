package com.nhomduan.quanlyungdungdathang.Fragment;

import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.nhomduan.quanlyungdungdathang.Activity.LoginActivity;
import com.nhomduan.quanlyungdungdathang.Activity.NhapOTPActivity;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterInsertObject;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.LoginViewModel;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class SignupTabFragment extends Fragment {

    private EditText edtSoDienThoai;
    private EditText edtTenDangNhap;
    private EditText edtMatKhau;
    private EditText edtNhapLaiMatKhau;
    private ToggleButton btnCheckPass2;
    private ToggleButton btnCheckPass;
    private Button btnHuyDangKi;
    private Button btnDangKy;

    private LoginActivity loginActivity;

    // biến môi trường kết nối của Auth của Firebase
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        loginActivity = (LoginActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpEdtSoDienThoai();
        setUpBtnCheckPass(btnCheckPass, edtMatKhau);
        setUpBtnCheckPass(btnCheckPass2, edtNhapLaiMatKhau);
        setUpBtnLogin();
        setUpBtnCancel();
    }

    private void initView(View view) {
        edtSoDienThoai = view.findViewById(R.id.edtSoDienThoai);
        edtTenDangNhap = view.findViewById(R.id.edtTenDangNhap);
        edtMatKhau = view.findViewById(R.id.edtMatKhau);
        edtNhapLaiMatKhau = view.findViewById(R.id.edtNhapLaiMatKhau);
        btnCheckPass2 = view.findViewById(R.id.btnCheckPass2);
        btnHuyDangKi = view.findViewById(R.id.btnHuyDangKi);
        btnDangKy = view.findViewById(R.id.btnDangKy);
        btnCheckPass = view.findViewById(R.id.btnCheckPass);
    }

    private void setUpEdtSoDienThoai() {
        edtSoDienThoai.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                edtSoDienThoai.setHint("");
            } else {
                edtSoDienThoai.setHint("Nhập số điện thoại");
            }
        });
    }

    private void setUpBtnCheckPass(ToggleButton btnCheckPass, EditText edtMatKhau) {
        btnCheckPass.setOnClickListener(v -> {
            if (btnCheckPass.isChecked()) {
                edtMatKhau.setTransformationMethod(null);
            } else {
                edtMatKhau.setTransformationMethod(new PasswordTransformationMethod());
            }

        });
    }

    private void setUpBtnLogin() {
        btnDangKy.setOnClickListener(v -> {
            String phone_number = "+84" + edtSoDienThoai.getText().toString().trim();
            String username = edtTenDangNhap.getText().toString().trim();
            String password = edtMatKhau.getText().toString().trim();
            String rePassword = edtNhapLaiMatKhau.getText().toString().trim();
            if (validate(phone_number, username, password, rePassword)) {
                UserDao.getInstance().isDuplicateUserName(username, new IAfterGetAllObject() {
                    @Override
                    public void iAfterGetAllObject(Object obj) {
                        if ((Boolean) obj) {
                            OverUtils.makeToast(getContext(), "Tên đăng nhập này đễ tồn tại");
                        } else {
                            UserDao.getInstance().isDuplicatePhoneNumber(phone_number, new IAfterGetAllObject() {
                                @Override
                                public void iAfterGetAllObject(Object obj) {
                                    if ((Boolean) obj) {
                                        OverUtils.makeToast(getContext(), "Số điện thoại này đã tồn tại");
                                        return;
                                    }
                                    User newUser = new User(username, password, phone_number, true);
                                    UserDao.getInstance().insertUser(newUser, new IAfterInsertObject() {
                                        @Override
                                        public void onSuccess(Object obj) {
                                            LoginViewModel loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
                                            loginViewModel.setData((User) obj);
                                            OverUtils.makeToast(getContext(), "Đăng kí thành công");
                                            goToLoginFragment();
                                        }

                                        @Override
                                        public void onError(DatabaseError exception) {
                                            OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                                        }
                                    });

                                }

                                @Override
                                public void onError(DatabaseError error) {
                                    OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                                }
                            });
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
        btnHuyDangKi.setOnClickListener(v -> {
            edtTenDangNhap.setText("");
            edtMatKhau.setText("");
            edtNhapLaiMatKhau.setText("");
            edtSoDienThoai.setText("");
        });
    }

    private boolean validate(String phone_number, String username, String password, String rePassword) {
        if (password.isEmpty() || rePassword.isEmpty() || username.isEmpty() || phone_number.isEmpty()) {
            OverUtils.makeToast(getContext(), "Quý khánh vui lòng nhập đầy đủ thông tin");
            return false;
        }

        if (username.length() <= 5) {
            OverUtils.makeToast(getContext(), "Quý khánh vui lòng đặt tên đăng nhập từ 6 kí tự trở lên");
            return false;
        }

        if (!phone_number.matches("^\\+84\\d{9,10}$")) {
            OverUtils.makeToast(getContext(), "Sai định dạng số điện thoại");
            return false;
        }

        if (!password.equals(rePassword)) {
            OverUtils.makeToast(getContext(), "Mật khẩu không trùng khớp");
            return false;
        }

        if (password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
        } else {
            OverUtils.makeToast(getContext(),
                    "Mật khẩu cần 8 kí tự trở lên, trong đó có chứa kí tự đặc biệt, chữ cái viết hoa và số");
            return false;
        }
        return true;
    }

    private void goToLoginFragment() {
        edtSoDienThoai.setText("");
        edtMatKhau.setText("");
        edtNhapLaiMatKhau.setText("");
        edtTenDangNhap.setText("");
        loginActivity.getTabLayout().selectTab(loginActivity.getTabLayout().getTabAt(0));
    }


    private void onClickVerifyPhone(User user) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(user.getPhone_number())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(loginActivity)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                                Log.d("TAG", "onVerificationCompleted:" + credential);
                                signInWithPhoneAuthCredential(credential, user);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Log.w("TAG", "onVerificationFailed", e);
                                OverUtils.makeToast(getContext(), "Lỗi thực hiện");
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                Log.d("TAG", "onCodeSent:" + verificationId);
                                goToEnterOTPActivity(user, verificationId);
                            }


                        }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, User user) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
//                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            UserDao.getInstance().insertUser(user, new IAfterInsertObject() {
                                @Override
                                public void onSuccess(Object obj) {
                                    goToLoginFragment();
                                }

                                @Override
                                public void onError(DatabaseError exception) {
                                    OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                                }
                            });

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                OverUtils.makeToast(getContext(), "Quý khánh vui lòng tra lại OTP");
                            }
                        }
                    }
                });
    }

    private void goToEnterOTPActivity(User user, String verificationId) {
        Intent intent = new Intent(getContext(), NhapOTPActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("verificationId", verificationId);
        startActivity(intent);
    }
}