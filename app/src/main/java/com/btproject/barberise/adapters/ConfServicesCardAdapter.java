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
import com.btproject.barberise.reservation.Category;
import com.btproject.barberise.reservation.Subcategory;

import java.util.ArrayList;

public class ConfServicesCardAdapter extends RecyclerView.Adapter<ConfServicesCardAdapter.ViewHolder>{

    ArrayList<Category> categories;
    Context context;

    public ConfServicesCardAdapter(ArrayList<Category> categories,Context context)
    {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public ConfServicesCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conf_services_card, parent, false);
        return new ConfServicesCardAdapter.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfServicesCardAdapter.ViewHolder holder, int position) {
        String hint = (String) holder.categoryName.getHint();
        String no = String.valueOf(position + 1);
        holder.categoryName.setHint(hint + " číslo " + no);
        holder.categoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Get the name of the subcategory
                String subcategoryNameInput = holder.categoryName.getText().toString();

                // Set the name to subcategory
                categories.get(holder.getAdapterPosition()).setName(subcategoryNameInput);
            }
        });

        // Create and set the SubservicesCardAdapter for the inner RecyclerView
        ArrayList<Subcategory> subcategories = categories.get(position).getSubcategories();
        ConfSubServicesCardAdapter confSubServicesCardAdapter = new ConfSubServicesCardAdapter(subcategories, context);
        holder.recyclerView.setAdapter(confSubServicesCardAdapter);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected EditText categoryName;
        protected RecyclerView recyclerView;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryConfEditText);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
        }
    }
}
