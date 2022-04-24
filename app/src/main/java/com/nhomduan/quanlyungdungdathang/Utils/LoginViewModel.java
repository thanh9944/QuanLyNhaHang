package com.nhomduan.quanlyungdungdathang.Utils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nhomduan.quanlyungdungdathang.Model.User;

public class LoginViewModel extends ViewModel{
    private final MutableLiveData<User> selectedItem = new MutableLiveData<>();

    public MutableLiveData<User> getData() {
        return  selectedItem;
    }

    public void setData(User user) {
        selectedItem.setValue(user);
    }
    public User getSelectedItem() {
        return selectedItem.getValue();
    }
}
