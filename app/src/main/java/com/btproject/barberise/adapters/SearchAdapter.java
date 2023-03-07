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

import com.btproject.barberise.R;
import com.btproject.barberise.navigation.profile.User;
import com.btproject.barberise.reservation.ReservationTestingActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private ArrayList<User> mUsers;
    private Context mContext;

    public SearchAdapter(ArrayList<User> mUsers,Context mContext)
    {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    public void setFilteredList(ArrayList<User> filteredList)
    {
        this.mUsers = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_card_search, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.inSearchNameTextView.setText(user.getUsername());
        if (mUsers.get(position).getProfile_picture() != null) {
            Glide.with(holder.inSearchProfileImageView.getContext())
                    .load(user.getProfile_picture())
                    .into(holder.inSearchProfileImageView);
        } else {
            Glide.with(holder.inSearchProfileImageView.getContext())
                    .load(R.drawable.user)
                    .into(holder.inSearchProfileImageView);
        }
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ReservationTestingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id", user.getId());
            intent.putExtra("username", user.getUsername());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView inSearchNameTextView;

        public ImageView inSearchProfileImageView;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            inSearchNameTextView = itemView.findViewById(R.id.inSearchNameTextView);
            inSearchProfileImageView = itemView.findViewById(R.id.ProfileImageView);
            cardView = itemView.findViewById(R.id.inSeachCardView);
        }
    }

}
