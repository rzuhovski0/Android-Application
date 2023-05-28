package com.btproject.barberise.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.R;
import com.btproject.barberise.reservation.Category;
import com.btproject.barberise.reservation.ContactInfoActivity;
import com.btproject.barberise.reservation.Subcategory;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ServicesCardAdapter extends RecyclerView.Adapter<ServicesCardAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Category> categories;

    public ServicesCardAdapter(Context context, ArrayList<Category> categories)
    {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ServicesCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_card, parent, false);
        return new ServicesCardAdapter.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesCardAdapter.ViewHolder holder, int position) {
        // Set name for each category
        String name = position + 1 + ". " + categories.get(position).getName() + ":";
        holder.categoryName.setText(name);

        // Create and set the SubservicesCardAdapter for the inner RecyclerView
        ArrayList<Subcategory> subcategories = categories.get(position).getSubcategories();
        SubservicesCardAdapter subservicesAdapter = new SubservicesCardAdapter(subcategories, context);
        holder.recyclerView.setAdapter(subservicesAdapter);

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView categoryName;
        protected RecyclerView recyclerView;

        public ViewHolder(View itemView,Context context) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryTextView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
        }
    }
}
