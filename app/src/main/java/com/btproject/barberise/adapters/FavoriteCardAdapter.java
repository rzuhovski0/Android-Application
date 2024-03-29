package com.btproject.barberise.adapters;

import static com.btproject.barberise.utils.DatabaseUtils.removeUserFromFavorites;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.R;
import com.btproject.barberise.reservation.ReservationTestingActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoriteCardAdapter extends RecyclerView.Adapter<FavoriteCardAdapter.ViewHolder>{

    @NonNull
    @Override
    public FavoriteCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String,String> favMap = favUsers.get(position);

        /**Load username*/
        holder.inFavNameOneTextView.setText(favMap.get("userName"));

        /**Load profile picture*/
        Glide.with(holder.inFavProfileImageView.getContext())
                .load(favMap.get("imageUrl"))
                .into(holder.inFavProfileImageView);

        //TODO implement remove from fav

        /**Set reserve again button listener*/
        holder.inFavAgainButton.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ReservationTestingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id", favMap.get("barberShopId"));
            mContext.startActivity(intent);
        });

        holder.inFavFavButton.setOnClickListener(v ->
        {
            removeUserFromFavorites(favMap.get("barberShopId"));
        });
    }

    @Override
    public int getItemCount() {
        return favUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView inFavNameOneTextView,inFavNameTwoTextView;
        public ShapeableImageView inFavProfileImageView;
        public FrameLayout inFavAgainButton,inFavFavButton;
        public CardView inFavCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            inFavCardView = itemView.findViewById(R.id.inSeachCardView);

            inFavNameOneTextView = itemView.findViewById(R.id.inSearchNameTextView);
            inFavNameTwoTextView = itemView.findViewById(R.id.inCalPriceTextView);

            inFavAgainButton = itemView.findViewById(R.id.addSubcategoryButton);
            inFavFavButton = itemView.findViewById(R.id.inFavFavFrameLayoutButton);

            inFavProfileImageView = itemView.findViewById(R.id.ProfileImageView);
        }
    }

    private ArrayList<HashMap<String,String>> favUsers;
    private Context mContext;

    public FavoriteCardAdapter(ArrayList<HashMap<String,String>> favUsers,Context context)
    {
        this.favUsers = favUsers;
        mContext = context;
    }

}
