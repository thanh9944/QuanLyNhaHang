package com.nhomduan.quanlyungdungdathang.Interface;

import com.google.firebase.database.DatabaseError;

public interface IAfterDeleteObject {
    void onSuccess(Object obj);
    void onError(DatabaseError error);
}
