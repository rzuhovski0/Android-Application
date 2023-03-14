package com.btproject.barberise.adapters;

import static com.btproject.barberise.utils.DatabaseUtils.removeReservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.R;
import com.btproject.barberise.reservation.Reservation;

import java.text.ParseException;
import java.util.ArrayList;

public class UserResCardAdapter extends RecyclerView.Adapter<UserResCardAdapter.ViewHolder>{

    private ArrayList<Reservation> reservations;
    private Context mContext;

    private String userId;

    public UserResCardAdapter(ArrayList<Reservation> reservations,Context context,String id)
    {
        this.reservations = reservations;
        this.mContext = context;
        this.userId = id;
    }



    @NonNull
    @Override
    public UserResCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_card_user, parent, false);
        return new UserResCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /**Load username of service provider*/

        String clientName = reservations.get(position).getUserName();
        if(clientName != null)
            holder.inResCardUserClientNameTextView.setText(clientName);

        String subcategoryName = reservations.get(position).getSubcategoryName();
        if(subcategoryName != null)
            holder.inResCardUserSubCatNameTextView.setText(subcategoryName);

        String description = reservations.get(position).getSubcategoryDescription();
        if(description != null)
            holder.inResCardUserResDescTextView.setText(description);


        /**Load Date & Time*/
        //TODO implement Date+Time formation
        try {

            String time = reservations.get(position).formatDateAndTime();
            if(time != null)
             holder.inResCardUserTimeTextView.setText(time);
        } catch (ParseException e) {
            holder.inResCardUserTimeTextView.setText("Unable to display date and time");
        }

        //TODO differ listener action based on if the reservation already happened
        /**Set reserve again button listener*/
        String reservationId = reservations.get(position).getId();
        if(reservationId != null) {
            holder.inResCardUserFragmentView.setOnClickListener(v -> {
                removeReservation(reservationId, userId);
            });
        }
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView inResCardUserCardView;
        public TextView inResCardUserClientNameTextView,inResCardUserSubCatNameTextView,
                inResCardUserResDescTextView, inResCardUserTimeTextView;
        public FrameLayout inResCardUserFragmentView;

        public ViewHolder(View itemView) {
            super(itemView);
            inResCardUserCardView = itemView.findViewById(R.id.inResCardUserCardView);

            inResCardUserClientNameTextView = itemView.findViewById(R.id.inResCardUserClientNameTextView);
            inResCardUserSubCatNameTextView = itemView.findViewById(R.id.inResCardUserSubCatNameTextView);
            inResCardUserResDescTextView = itemView.findViewById(R.id.inResCardUserResDescTextView);
            inResCardUserTimeTextView = itemView.findViewById(R.id.inResCardUserTimeTextView);

            inResCardUserFragmentView = itemView.findViewById(R.id.inResCardUserFragmentView);

        }
    }

}
