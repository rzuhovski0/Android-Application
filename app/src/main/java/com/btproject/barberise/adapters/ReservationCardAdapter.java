package com.btproject.barberise.adapters;

import static com.btproject.barberise.utils.DatabaseUtils.removeReservation;
import static com.btproject.barberise.utils.DatabaseUtils.removeUserFromFavorites;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.R;
import com.btproject.barberise.reservation.Reservation;
import com.btproject.barberise.reservation.ReservationTestingActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.ParseException;
import java.util.ArrayList;

public class ReservationCardAdapter extends RecyclerView.Adapter<ReservationCardAdapter.ViewHolder>{

    private ArrayList<Reservation> reservations;
    private Context mContext;

    public ReservationCardAdapter(ArrayList<Reservation> reservations,Context context)
    {
        this.reservations = reservations;
        mContext = context;
    }

    @NonNull
    @Override
    public ReservationCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservations_card, parent, false);
        return new ReservationCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /**Load username of service provider*/
        holder.inCalNameTextView.setText(reservations.get(position).getServiceProviderName());

        //TODO add getPrice to Reservation
        holder.inCalPriceTextView.setText(reservations.get(position).getPriceString());

        /**Load Date & Time*/
        //TODO implement Date+Time formation
        try {
            holder.inCalDateTimeTextView.setText(reservations.get(position).formatDateAndTime());
        } catch (ParseException e) {
            holder.inCalDateTimeTextView.setText("Unable to display date and time");
        }

        /**Load profile picture*/
        //TODO implement getProfilePicture on Reservation
        Glide.with(holder.inCalProfileImageView.getContext())
                .load(reservations.get(position).getProfilePicture())
                .into(holder.inCalProfileImageView);

        /**If reservation already passed, change its button design*/
        // We need to use try/catch block because hasPassed method return value of hasTimeAlreadyHappened return value which parses time
        try {
            if(!reservations.get(position).hasPassed())
            {
                ColorStateList colorStateList = ContextCompat.getColorStateList(mContext, R.color.red);
                holder.inCalAgainFragmentView.setBackgroundTintList(colorStateList);
                holder.imageViewSrc.setImageResource(R.drawable.x);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //TODO differ listener action based on if the reservation already happened
        /**Set reserve again button listener*/
        holder.inCalAgainFragmentView.setOnClickListener(v -> {
            try {
                if(reservations.get(position).hasPassed()) {
                    Intent intent = new Intent(mContext, ReservationTestingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id", reservations.get(position).getServiceProviderId());
                    mContext.startActivity(intent);
                }else{
                    removeReservation(reservations.get(position).getId());
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView inCalNameTextView,inCalPriceTextView,inCalDateTimeTextView;
        public ShapeableImageView inCalProfileImageView;
        public ImageView imageViewSrc;
        public FrameLayout inCalAgainFragmentView;
        public CardView inCalCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            inCalCardView = itemView.findViewById(R.id.inCalCardView);

            inCalNameTextView = itemView.findViewById(R.id.inCalNameTextView);
            inCalPriceTextView = itemView.findViewById(R.id.inCalPriceTextView);
            inCalDateTimeTextView = itemView.findViewById(R.id.inCalDateTimeTextView);

            inCalAgainFragmentView = itemView.findViewById(R.id.inCalAgainFragmentView);

            inCalProfileImageView = itemView.findViewById(R.id.inCalProfileImageView);
            imageViewSrc = itemView.findViewById(R.id.srcDrawable);
        }
    }

}
