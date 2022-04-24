package com.nhomduan.quanlyungdungdathang.Fragment;

import static com.nhomduan.quanlyungdungdathang.Activity.FlashActivity.userLogin;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlyungdungdathang.Activity.AvatarActivity;
import com.nhomduan.quanlyungdungdathang.Activity.HomeActivity;
import com.nhomduan.quanlyungdungdathang.Activity.LoginActivity;
import com.nhomduan.quanlyungdungdathang.Activity.OrderActivity;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterRequestPermission;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterUpdateObject;
import com.nhomduan.quanlyungdungdathang.LocalDatabase.LocalUserDatabase;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private ImageView imgEdit, imgAvatar;
    private TextView tvUsername;
    private Button btnCancel, btnChange, btnLogout, btnChangeAdress, btnComfirm;
    private EditText edUsername, edPass, edPassRepeat, edAddress;
    private CardView cvAddress, cvOrder, cvSupport;

    private FragmentActivity fragmentActivity;
    private Context mContext;
    private User user;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    callPhone();
                } else {
                    OverUtils.makeToast(getContext(), "Permission denied!");
                }
            });

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = getActivity();
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getUserLogin();
        setUpBtnLogout();
        setUpChangeAvatarAction();
        setUpToOrderAction();
        setUpToSupportAction();
    }

    private void initView(View view) {
        cvAddress = view.findViewById(R.id.cv_diachi);
        cvOrder = view.findViewById(R.id.cv_donhang);
        cvSupport = view.findViewById(R.id.cv_trungtamhotro);
        imgAvatar = view.findViewById(R.id.img_avatar_profile);
        imgEdit = view.findViewById(R.id.img_edit_profile);
        tvUsername = view.findViewById(R.id.tv_accountname_profile);
        btnLogout = view.findViewById(R.id.btn_logout_account);
    }

    private void getUserLogin() {
        UserDao.getInstance().getUserByUserNameListener(userLogin.getUsername(),
                new IAfterGetAllObject() {
                    @Override
                    public void iAfterGetAllObject(Object obj) {
                        if(obj != null) {
                            user = (User) obj;
                            if (user.getUsername() != null) {
                                buildComponentUser(user);
                            }
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
    }

    private void buildComponentUser(User user) {
        if (user.getHinhanh() != null) {
            Picasso.get()
                    .load(user.getHinhanh())
                    .placeholder(R.drawable.ic_image)
                    .into(imgAvatar);
        }
        if (user.getName() != null) {
            tvUsername.setText(user.getName());
        } else {
            tvUsername.setText(user.getUsername());
        }
        imgEdit.setOnClickListener(v -> openDialogToChangePassAndName(user));
        cvAddress.setOnClickListener(v -> openDiaLogToChangeAddress(user));
    }

    private void setUpBtnLogout() {
        btnLogout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Bạn có chắc muốn đăng xuất?")
                    .setPositiveButton("CÓ", (dialog, which) -> {
                        SharedPreferences.Editor editor = OverUtils.getSPInstance(getContext(), OverUtils.PASS_FILE).edit();
                        editor.putString("pass", OverUtils.PASS_FLASH_ACTIVITY);
                        editor.apply();
                        LocalUserDatabase.getInstance(mContext).getUserDao().delete(OverUtils.getUserLogin(mContext));
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        fragmentActivity.finish();
                    })
                    .setNegativeButton("HỦY", (dialog, which) -> dialog.dismiss());
            Dialog dialog = builder.create();
            dialog.show();
        });
    }

    private void setUpChangeAvatarAction() {
        imgAvatar.setOnClickListener(v -> {
            startActivity(new Intent(mContext, AvatarActivity.class));
        });
    }

    private void setUpToOrderAction() {
        cvOrder.setOnClickListener(v -> {
            startActivity(new Intent(mContext, OrderActivity.class));
        });
    }

    private void setUpToSupportAction() {
        cvSupport.setOnClickListener(v -> {
            requestPermissions(Manifest.permission.CALL_PHONE, request -> {
                if (request) {
                    callPhone();
                }
            });
        });
    }

    public void requestPermissions(String permission, IAfterRequestPermission onAfterRequestPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requireActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                onAfterRequestPermission.onAfterRequestPermission(true);
            } else {
                requestPermissionLauncher.launch(permission);
            }
        } else {
            onAfterRequestPermission.onAfterRequestPermission(true);
        }
    }

    private void callPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:1900 1000"));
        startActivity(intent);
    }

    private void openDiaLogToChangeAddress(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_address_user, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        edAddress = view.findViewById(R.id.edtAddressUser_profile);
        btnChangeAdress = view.findViewById(R.id.btnChangeAddress_profile);
        btnComfirm = view.findViewById(R.id.btnComfirmAddress_profile);

        edAddress.setText(user.getAddress());
        btnChangeAdress.setOnClickListener(v -> {
            String address = edAddress.getText().toString();
            if (address.isEmpty()) {
                OverUtils.makeToast(getContext(), "Không để trống thông tin");
            } else {
                user.setAddress(address);
                UserDao.getInstance().updateUser(user, user.editAdress(), new IAfterUpdateObject() {
                    @Override
                    public void onSuccess(Object obj) {
                        OverUtils.makeToast(getContext(), "Thay đổi địa chỉ thành công");
                        LocalUserDatabase.getInstance(mContext).getUserDao().update(user);
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        OverUtils.makeToast(getContext(), "Thay đổi địa chỉ thất bại");
                        dialog.dismiss();
                    }
                });
            }
        });
        btnComfirm.setOnClickListener(v -> dialog.dismiss());
    }

    private void openDialogToChangePassAndName(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_user, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        edUsername = view.findViewById(R.id.edtTenNguoiDung_profile);
        edPass = view.findViewById(R.id.edtMatKhau_profile);
        edPassRepeat = view.findViewById(R.id.edtNhapLaiMatKhau_profile);
        btnChange = view.findViewById(R.id.btnChangeThongTin_profile);
        btnCancel = view.findViewById(R.id.btnHuyChangeThongTin_profile);

        edPass.setText(user.getPassword());
        edPassRepeat.setText(user.getPassword());
        edUsername.setText(user.getName());


        btnChange.setOnClickListener(v -> {
            String name = edUsername.getText().toString().trim();
            String pass = edPass.getText().toString().trim();
            String passRepeat = edPassRepeat.getText().toString().trim();
            if (validate(name, pass, passRepeat)) {
                user.setPassword(pass);
                user.setName(name);
                UserDao.getInstance().updateUser(user, user.editUser(), new IAfterUpdateObject() {
                    @Override
                    public void onSuccess(Object obj) {
                        OverUtils.makeToast(getContext(), "Thay đổi thông tin thành công");
                        LocalUserDatabase.getInstance(mContext).getUserDao().update(user);
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        OverUtils.makeToast(getContext(), "Thay đổi thông tin thất bại");
                        dialog.dismiss();
                    }
                });

            }
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }

    private boolean validate(String username, String password, String rePassword) {
        if (password.isEmpty() || rePassword.isEmpty() || username.isEmpty()) {
            OverUtils.makeToast(getContext(), "Quý khánh vui lòng nhập đầy đủ thông tin");
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

}