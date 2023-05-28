package com.btproject.barberise.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.R;
import com.btproject.barberise.reservation.Subcategory;

import java.util.ArrayList;

public class SubservicesCardAdapter extends RecyclerView.Adapter<SubservicesCardAdapter.ViewHolder>{

    private ArrayList<Subcategory> subcategories;
    private Context context;


    public SubservicesCardAdapter(ArrayList<Subcategory> subcategories,Context context)
    {
        this.subcategories = subcategories;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subservices_card, parent, false);
        return new SubservicesCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = extractStringBeforeNumber(subcategories.get(position).getName());

        holder.subcategoryNameTextViewInSubservices.setText(name);
        holder.DescriptionTextViewInSubServices.setText(subcategories.get(position).getDescription());
        holder.priceTextViewInSubServices.setText(String.valueOf(subcategories.get(position).getPrice()) + "€");
    }

    @Override
    public int getItemCount() {
        return subcategories.size();
    }

    private String extractStringBeforeNumber(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                break;
            }
            result.append(c);
        }
        return result.toString().trim();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView subcategoryNameTextViewInSubservices,DescriptionTextViewInSubServices,priceTextViewInSubServices;

        public ViewHolder(View itemView) {
            super(itemView);
            subcategoryNameTextViewInSubservices = itemView.findViewById(R.id.subcategoryNameTextViewInSubservices);
            DescriptionTextViewInSubServices = itemView.findViewById(R.id.DescriptionTextViewInSubServices);
            priceTextViewInSubServices = itemView.findViewById(R.id.priceTextViewInSubServices);
        }
    }

}
