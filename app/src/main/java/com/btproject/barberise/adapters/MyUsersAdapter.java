package com.btproject.barberise.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.reservation.ReservationActivity;
import com.btproject.barberise.R;
import com.btproject.barberise.navigation.profile.User;
import com.bumptech.glide.Glide;

import java.util.List;

public class MyUsersAdapter extends RecyclerView.Adapter<MyUsersAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;
        public ImageView profilePictureImageView;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.nameTextView);
            profilePictureImageView = itemView.findViewById(R.id.profileImageView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    private List<User> mUsers;
    private Context mContext;

    public MyUsersAdapter(List<User> users,Context mContext) {
        this.mContext = mContext;
        this.mUsers = users;
    }


    @NonNull
    @Override
    public MyUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_display, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyUsersAdapter.ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.usernameTextView.setText(user.getUsername());
        if(mUsers.get(position).getProfile_picture() != null) {
            Glide.with(holder.profilePictureImageView.getContext())
                    .load(user.getProfile_picture())
                    .into(holder.profilePictureImageView);
        }else{
            Glide.with(holder.profilePictureImageView.getContext())
                    .load(R.drawable.user)
                    .into(holder.profilePictureImageView);
        }
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ReservationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id",user.getId());
            intent.putExtra("username",user.getUsername());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
