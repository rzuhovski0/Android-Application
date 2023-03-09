package com.btproject.barberise.partner;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    List<String> userList;
    String userName;
    Context mContext;

    FirebaseDatabase database;
    DatabaseReference reference;

    public UsersAdapter(List<String> userList) {
        this.userList = userList;

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public UsersAdapter(List<String> userList,Context mContext) {
        this.userList = userList;
        this.mContext = mContext;

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public UsersAdapter(List<String> userList,String userName,Context mContext) {
        this.userList = userList;
        this.userName = userName;
        this.mContext = mContext;

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_display,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        reference.child("Users").child(userList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final String otherName = snapshot.child("userName").getValue().toString();
                String imageURL = snapshot.child("image").getValue().toString();

                holder.textViewShopName.setText(otherName);

                if (imageURL.equals("null"))
                {
                    holder.imageView.setImageResource(R.drawable.user);
                }
                else
                {
                    Picasso.get().load(imageURL).into(holder.imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textViewShopName,textViewRating;
        private ShapeableImageView imageView;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textViewShopName = itemView.findViewById(R.id.nameTextView);
            textViewRating = itemView.findViewById(R.id.RatingTextView);
            imageView = itemView.findViewById(R.id.ProfileImageView);
        }
    }
}