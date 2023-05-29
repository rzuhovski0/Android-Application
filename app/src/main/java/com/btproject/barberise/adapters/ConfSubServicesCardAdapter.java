package com.btproject.barberise.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.R;
import com.btproject.barberise.reservation.Subcategory;

import java.util.ArrayList;

public class ConfSubServicesCardAdapter extends RecyclerView.Adapter<ConfSubServicesCardAdapter.ViewHolder>{

    private ArrayList<Subcategory> subcategories;
    private Context context;


    public ConfSubServicesCardAdapter(ArrayList<Subcategory> subcategories,Context context)
    {
        this.subcategories = subcategories;
        this.context = context;
    }

    @NonNull
    @Override
    public ConfSubServicesCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conf_subservice_card, parent, false);
        return new ConfSubServicesCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfSubServicesCardAdapter.ViewHolder holder, int position) {
        String hint = (String) holder.subcategoryName.getHint();
        String no = String.valueOf(position + 1);
        holder.subcategoryName.setHint(hint + " číslo " + no);

        holder.subcategoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Get the name of the subcategory
                String subcategoryNameInput = holder.subcategoryName.getText().toString();

                // Set the name to subcategory
                subcategories.get(holder.getAdapterPosition()).setName(subcategoryNameInput);
            }
        });

        holder.subcategoryDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Get the name of the subcategory
                String subcategoryDescriptionString = holder.subcategoryDescription.getText().toString();

                // Set the name to subcategory
                subcategories.get(holder.getAdapterPosition()).setDescription(subcategoryDescriptionString);
            }
        });

        holder.subcategoryPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Get the name of the subcategory
                String subcategoryPriceString = holder.subcategoryPrice.getText().toString();

                Double price = Double.valueOf(subcategoryPriceString);

                // Set the name to subcategory
                subcategories.get(holder.getAdapterPosition()).setPrice(price);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subcategories.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected EditText subcategoryName,subcategoryDescription,subcategoryPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            subcategoryName = itemView.findViewById(R.id.subcategoryNameTextViewInSubservices);
            subcategoryDescription = itemView.findViewById(R.id.DescriptionTextViewInSubServices);
            subcategoryPrice = itemView.findViewById(R.id.priceTextViewInSubServices);
        }
    }
}
