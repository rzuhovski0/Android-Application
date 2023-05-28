package com.btproject.barberise.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.navigation.profile.SetUpSubcategoriesActivity;
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
import com.btproject.barberise.reservation.ReservationTestingActivity;
import com.btproject.barberise.reservation.Subcategory;




public class CategoryCardAdapter extends RecyclerView.Adapter<CategoryCardAdapter.ViewHolder>{

    private static CategoryCardAdapter INSTANCE;


    public interface CategoryRemoveListener {
        void onCategoryRemoved(Category category);
    }


    private Context mContext;
    private ArrayList<Category> categories;

    private CategoryRemoveListener categoryRemoveListener;

    private CategoryCardAdapter()
    {
    }

    public void setContextAndCategories(Context mContext,ArrayList<Category> categories)
    {
        this.mContext = mContext;
        this.categories = categories;
    }

    public static CategoryCardAdapter getCategoryCardAdapter()
    {
        if(INSTANCE == null)
            return new CategoryCardAdapter();
        else
            return INSTANCE;
    }

    public void setCategoryRemoveListener(CategoryRemoveListener listener) {
        this.categoryRemoveListener = listener;
    }

    public void addCategory(Category category) {
        categories.add(category);
        notifyItemInserted(categories.size() - 1);
    }

    @NonNull
    @Override
    public CategoryCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card, parent, false);
        return new CategoryCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryCardAdapter.ViewHolder holder, int position) {

        holder.goIntoCategoryFrameLayout.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, SetUpSubcategoriesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });

        holder.removeCategoryFrameLayout.setOnClickListener(view -> {
            if (position >= 0 && position < categories.size()) {
                Category category = categories.get(position);
                categories.remove(category);
                if (categories.isEmpty()) {
                    notifyDataSetChanged();
                    if (categoryRemoveListener != null) {
                        categoryRemoveListener.onCategoryRemoved(category);
                    }
                } else {
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, categories.size());
                    if (categoryRemoveListener != null) {
                        categoryRemoveListener.onCategoryRemoved(category);
                    }
                }
            }
        });

        /**Listener for subcategory name*/
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
                categories.get(position).setName(subcategoryNameInput);
            }
        });

        /** Display name*/
        holder.categoryName.setText(categories.get(position).getName());


    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected EditText categoryName;
        protected FrameLayout goIntoCategoryFrameLayout,removeCategoryFrameLayout;

        protected TextView firstSubcategoryDesTextView, SubcategoriesTotalTextView, firstSubcategoryTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryEditText);
            goIntoCategoryFrameLayout = itemView.findViewById(R.id.addSubcategoryButton);
            removeCategoryFrameLayout = itemView.findViewById(R.id.removeCategoryButton);

            firstSubcategoryDesTextView = itemView.findViewById(R.id.firstSubcategoryDesTextView);
            SubcategoriesTotalTextView = itemView.findViewById(R.id.SubcategoriesTotalTextView);
            firstSubcategoryTextView = itemView.findViewById(R.id.firstSubcategoryTextView);
        }


    }


}