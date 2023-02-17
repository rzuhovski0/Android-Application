package com.btproject.barberise.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShopsViewAdapter extends RecyclerView.Adapter<ShopsViewAdapter.ShopsViewHolder> {

    private ArrayList<String>namesList;
    private ArrayList<String>ratingsList;
    private ArrayList<Integer>imagesList;
    private Context context;

    //added
    FirebaseDatabase database;
    DatabaseReference reference;

    public ShopsViewAdapter(ArrayList<String> namesList, ArrayList<String> ratingsList, ArrayList<Integer> imagesList, Context context) {
        this.namesList = namesList;
        this.ratingsList = ratingsList;
        this.imagesList = imagesList;
        this.context = context;

        //added
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    @NonNull
    @Override
    public ShopsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_display,parent,false);
        return new ShopsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopsViewHolder holder, int position) {
        holder.textViewShopName.setText(namesList.get(position));
        holder.textViewRating.setText(ratingsList.get(position));
        holder.imageView.setImageResource(imagesList.get(position));
    }

    @Override
    public int getItemCount() {
        return namesList.size();
    }

    public class ShopsViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewShopName,textViewRating;
        private ShapeableImageView imageView;
        private CardView cardView;

        public ShopsViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardViewShop);
            textViewShopName = itemView.findViewById(R.id.nameTextView);
            textViewRating = itemView.findViewById(R.id.RatingTextView);
            imageView = itemView.findViewById(R.id.profileImageView);
        }
    }

}
