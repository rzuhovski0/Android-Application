package com.btproject.barberise.adapters;

import static androidx.core.content.res.ResourcesCompat.getDrawable;
import static com.btproject.barberise.utils.DatabaseUtils.removeReservation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.R;
import com.btproject.barberise.reservation.Reservation;
import com.btproject.barberise.reservation.ReservationTestingActivity;
import com.btproject.barberise.utils.DatabaseUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.ParseException;
import java.util.ArrayList;

public class ReservationCardAdapter extends RecyclerView.Adapter<ReservationCardAdapter.ViewHolder>{

    private ArrayList<Reservation> reservations;
    private Context mContext;

    private String clientId;

    public ReservationCardAdapter(ArrayList<Reservation> reservations,Context context,String clientId)
    {
        this.reservations = reservations;
        this.mContext = context;
        this.clientId = clientId;
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

            /** If reservation has passed && wasn't yet rated, add ability to rate it*/
            }
            if(reservations.get(position).hasPassed() && !reservations.get(position).getAlreadyRated()){

                //Update visibility of rateTxtView
                holder.rateTextView.setVisibility(View.VISIBLE);
                holder.rateTextView.setOnClickListener(view -> {
                    // Update visibility
                    if(holder.ratingBar.getVisibility() == View.GONE){
                        holder.ratingBar.setVisibility(View.VISIBLE);
                        animAlphaComponents(holder);
                        rate(holder,reservations.get(position).getServiceProviderId(),reservations.get(position).getId());
                    }else{
                        holder.ratingBar.setVisibility(View.GONE);
                        animAlphaComponents(holder);
                    }

                });
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
                    Context parentContext = holder.itemView.getContext();
                    Dialog dialog = getCustomDialog(parentContext, position);
                    dialog.show();
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        });
    }

    private void rate(ViewHolder holder,String barberId, String reservationId)
    {
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int rating = (int)v;

                // Rate reservation
                DatabaseUtils.rateBarberShop(barberId,rating);

                // Update reservation alreadyPassed
                if(clientId != null)
                    DatabaseUtils.tagReservationAlreadyRated(clientId,reservationId);

                // Notify user
                Toast.makeText(mContext,R.string.thanks_for_rating,Toast.LENGTH_LONG).show();

                //Update visibility
                ratingBar.setVisibility(View.GONE);
                animAlphaComponents(holder);
            }
        });
    }

    private void animAlphaComponents(ViewHolder holder)
    {
        if(holder.ratingBar.getVisibility() == View.VISIBLE) {
            // Animate the alpha property of other views
            holder.imageViewSrc.animate().alpha(0.2f).setDuration(300).start();
            holder.inCalNameTextView.animate().alpha(0.2f).setDuration(300).start();
            holder.inCalDateTimeTextView.animate().alpha(0.2f).setDuration(300).start();
            holder.inCalAgainFragmentView.animate().alpha(0.2f).setDuration(300).start();
            holder.inCalProfileImageView.animate().alpha(0.2f).setDuration(300).start();
            holder.inCalPriceTextView.animate().alpha(0.2f).setDuration(300).start();
        }else{
            // Hide other components
            holder.imageViewSrc.animate().alpha(1f).setDuration(300).start();
            holder.inCalNameTextView.animate().alpha(1f).setDuration(300).start();
            holder.inCalDateTimeTextView.animate().alpha(1f).setDuration(300).start();
            holder.inCalAgainFragmentView.animate().alpha(1f).setDuration(300).start();
            holder.inCalProfileImageView.animate().alpha(1f).setDuration(300).start();
            holder.inCalPriceTextView.animate().alpha(1f).setDuration(300).start();
        }
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    private Dialog getCustomDialog(Context parentContext,int position)
    {
        //Create the Dialog here
        Dialog dialog = new Dialog(parentContext);
        dialog.setContentView(R.layout.reservation_confirm_cancel_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(parentContext.getResources(),R.drawable.dialog_background,parentContext.getTheme()));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        /**Init buttons*/
        Button Okay = dialog.findViewById(R.id.btn_okay);
        Button Cancel = dialog.findViewById(R.id.btn_cancel);

        Okay.setOnClickListener(v -> {
            String reservationId = reservations.get(position).getId();
            String barberShopId = reservations.get(position).getServiceProviderId();
            removeReservation(reservationId,barberShopId);
            dialog.dismiss();
        });

        Cancel.setOnClickListener(v -> dialog.dismiss());

        return dialog;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView inCalNameTextView,inCalPriceTextView,inCalDateTimeTextView,rateTextView;
        public RatingBar ratingBar;
        public ShapeableImageView inCalProfileImageView;
        public ImageView imageViewSrc;
        public FrameLayout inCalAgainFragmentView;
        public CardView inCalCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            inCalCardView = itemView.findViewById(R.id.inResCardUserCardView);

            inCalNameTextView = itemView.findViewById(R.id.inSearchNameTextView);
            inCalPriceTextView = itemView.findViewById(R.id.inCalPriceTextView);
            inCalDateTimeTextView = itemView.findViewById(R.id.inCalDateTimeTextView);
            rateTextView = itemView.findViewById(R.id.rateTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            ratingBar.setVisibility(View.GONE);
            rateTextView.setVisibility(View.GONE);

            inCalAgainFragmentView = itemView.findViewById(R.id.addSubcategoryButton);

            inCalProfileImageView = itemView.findViewById(R.id.ProfileImageView);
            imageViewSrc = itemView.findViewById(R.id.srcDrawable);
        }
    }

}
