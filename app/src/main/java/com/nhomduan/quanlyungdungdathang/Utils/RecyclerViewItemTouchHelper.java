package com.nhomduan.quanlyungdungdathang.Utils;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlyungdungdathang.Adapter.FavoriteProductAdapter;
import com.nhomduan.quanlyungdungdathang.Interface.ItemTouchHelpListener;

public class RecyclerViewItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ItemTouchHelpListener listener;

    public RecyclerViewItemTouchHelper(int dragDirs, int swipeDirs, ItemTouchHelpListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if(listener != null) {
            listener.onSwiped(viewHolder);
        }
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder != null) {
            View foreGroundView = ((FavoriteProductAdapter.ViewHolder)viewHolder).viewHolderProduct;
            getDefaultUIUtil().onSelected(foreGroundView);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreGroundView = ((FavoriteProductAdapter.ViewHolder)viewHolder).viewHolderProduct;
        getDefaultUIUtil().onDraw(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreGroundView = ((FavoriteProductAdapter.ViewHolder)viewHolder).viewHolderProduct;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        View foreGroundView = ((FavoriteProductAdapter.ViewHolder)viewHolder).viewHolderProduct;
        getDefaultUIUtil().clearView(foreGroundView);
    }
}
