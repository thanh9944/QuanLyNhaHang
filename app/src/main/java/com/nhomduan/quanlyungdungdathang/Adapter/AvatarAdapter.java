package com.nhomduan.quanlyungdungdathang.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlyungdungdathang.Interface.ClickAvatar;
import com.nhomduan.quanlyungdungdathang.Model.Avatar;
import com.nhomduan.quanlyungdungdathang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {
    private Context context;
    private List<Avatar> list;
    private ClickAvatar clickAvatar;

    public AvatarAdapter(Context context, List<Avatar> list) {
        this.context = context;
        this.list = list;
    }

    public void setClickAvatar(ClickAvatar clickAvatar) {
        this.clickAvatar = clickAvatar;
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_avatar, parent, false);
        return new AvatarViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder holder, int position) {
        Avatar avatar = list.get(position);
        if (avatar.getImage().equals("null")) {
            holder.imageAvatar.setImageResource(R.drawable.ic_image);
            holder.nameAvatar.setText(avatar.getTitle());
            holder.itemView.setOnClickListener(v -> {
                clickAvatar.onClickAvatar(avatar);
            });
        } else {
            Picasso.get()
                    .load(avatar.getImage())
                    .placeholder(R.drawable.ic_image)
                    .into(holder.imageAvatar);
            holder.nameAvatar.setText(avatar.getTitle());
            holder.itemView.setOnClickListener(v -> clickAvatar.onClickAvatar(avatar));
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AvatarViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageAvatar;
        TextView nameAvatar;
        LinearLayout layout;

        public AvatarViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAvatar = itemView.findViewById(R.id.img_avatar_selected);
            nameAvatar = itemView.findViewById(R.id.tv_nameAvatar);
            layout = itemView.findViewById(R.id.click_avatar);
        }
    }

}
