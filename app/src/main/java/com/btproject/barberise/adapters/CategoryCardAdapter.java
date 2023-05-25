package com.btproject.barberise.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.reservation.Category;

import java.util.ArrayList;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.btproject.barberise.R;
import com.btproject.barberise.reservation.Subcategory;


public class CategoryCardAdapter extends RecyclerView.Adapter<CategoryCardAdapter.ViewHolder>{

    private static Context mContext;
    private ArrayList<Category> categories;
    private ArrayList<Subcategory> subcategories;

    public CategoryCardAdapter(Context mContext,ArrayList<Category> categories)
    {
        this.mContext = mContext;
        this.categories = categories;
        subcategories = new ArrayList<>();
    }


    @NonNull
    @Override
    public CategoryCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card, parent, false);
        return new CategoryCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryCardAdapter.ViewHolder holder, int position) {

        Category category = categories.get(position);

        // Set category name
        holder.categoryName.setText(category.getName());

        /** Add a watcher for categoryEditText to set category name*/
        holder.categoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Get the name of the category
                String categoryInput = holder.categoryName.getText().toString();

                // Set the name to category
                categories.get(holder.getAdapterPosition()).setName(categoryInput);
            }
        });

        holder.addSubcategoryFrameLayout.setOnClickListener(view ->
        {
            // Create new subcategory and add it to the category
            Subcategory subcategory = new Subcategory();
            category.addSubcategory(subcategory);

            int index = category.getSubcategories().indexOf(subcategory);

            if(holder.subcategoryCardAdapter == null)
            {
                holder.subcategoryCardAdapter = new SubcategoryCardAdapter(mContext,this,category);
                holder.inCategoryCardRecyclerView.setAdapter(holder.subcategoryCardAdapter);
            }

            holder.subcategoryCardAdapter.notifyItemInserted(index);
        });

        holder.removeSubcategoryFrameLayout.setOnClickListener(view -> {
            int removedPosition = holder.getAdapterPosition();
            categories.remove(removedPosition);
            notifyItemRemoved(removedPosition);
        });


    }


    private void initAdapter()
    {

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected EditText categoryName;
        protected FrameLayout addSubcategoryFrameLayout,removeSubcategoryFrameLayout;
        protected RecyclerView inCategoryCardRecyclerView;
        protected SubcategoryCardAdapter subcategoryCardAdapter;
        protected boolean isCustomized = false;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryEditText);
            addSubcategoryFrameLayout = itemView.findViewById(R.id.addSubcategoryButton);
            removeSubcategoryFrameLayout = itemView.findViewById(R.id.removeCategoryButton);
            inCategoryCardRecyclerView = itemView.findViewById(R.id.inCategoryCardRecyclerView);
            initRecyclerView();
        }

        protected void initRecyclerView()
        {
            inCategoryCardRecyclerView.setHasFixedSize(true);
            inCategoryCardRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false));
        }

    }


}