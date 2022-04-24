package com.nhomduan.quanlyungdungdathang.Activity;

import static com.nhomduan.quanlyungdungdathang.Activity.FlashActivity.userLogin;
import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nhomduan.quanlyungdungdathang.Adapter.AvatarAdapter;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.ClickAvatar;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterUpdateObject;
import com.nhomduan.quanlyungdungdathang.LocalDatabase.LocalUserDatabase;
import com.nhomduan.quanlyungdungdathang.Model.Avatar;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;

import java.util.ArrayList;
import java.util.List;

public class AvatarActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AvatarAdapter adapter;
    private ProgressDialog progressDialog;
    private List<Avatar> list = new ArrayList<>();
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    progressDialog = new ProgressDialog(AvatarActivity.this);
                    progressDialog.setMessage("Cập nhật ảnh ...");
                    progressDialog.show();
                    StorageReference fileRef =
                            FirebaseStorage.getInstance().getReference("imageAvater/").
                                    child(System.currentTimeMillis() + "." + OverUtils.getExtensionFile(AvatarActivity.this, uri));
                    fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                userLogin.setHinhanh(String.valueOf(uri));
                                UserDao.getInstance().updateUser(userLogin, userLogin.toMapAvatar(), new IAfterUpdateObject() {
                                    @Override
                                    public void onSuccess(Object obj) {
                                        progressDialog.cancel();
                                        OverUtils.makeToast(getApplicationContext(), "cập nhật ảnh thành công");
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        intent.setAction(OverUtils.GO_TO_ORDER_FROFILE_FRAGMENT);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onError(DatabaseError error) {
                                        OverUtils.makeToast(AvatarActivity.this, ERROR_MESSAGE);
                                    }
                                });
                            }
                        });
                    });
                }

            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);
        initView();
        setUpAvatarList();
        setUpSelectImg();

    }

    private void setUpSelectImg() {
        adapter.setClickAvatar(avatar -> {
            if (avatar.getImage().equals("null")) {
                mGetContent.launch("image/*");
            } else {
                userLogin.setHinhanh(avatar.getImage());
                UserDao.getInstance().updateUser(userLogin, userLogin.toMapAvatar(), new IAfterUpdateObject() {
                    @Override
                    public void onSuccess(Object obj) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.setAction(OverUtils.GO_TO_ORDER_FROFILE_FRAGMENT);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        OverUtils.makeToast(AvatarActivity.this, ERROR_MESSAGE);
                    }
                });
            }
        });
    }

    private void setUpAvatarList() {
        adapter = new AvatarAdapter(this, list);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("avatar");
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null) {
                    list.clear();
                    list.add(new Avatar("Custom Avatar", "null"));
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Avatar avatar = dataSnapshot.getValue(Avatar.class);
                    list.add(avatar);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void initView() {
        recyclerView = findViewById(R.id.rv_avatar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}