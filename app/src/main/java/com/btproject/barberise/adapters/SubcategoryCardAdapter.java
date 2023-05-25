package com.btproject.barberise.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.R;
import com.btproject.barberise.reservation.Category;
import com.btproject.barberise.reservation.Subcategory;

import java.util.ArrayList;

public class SubcategoryCardAdapter extends RecyclerView.Adapter<SubcategoryCardAdapter.ViewHolder>{

    private Context context;
    private Category category;

    public SubcategoryCardAdapter(Context context, CategoryCardAdapter categoryCardAdapter, Category category)
    {
        this.context = context;
        this.category = category;
    }


    @NonNull
    @Override
    public SubcategoryCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_card, parent, false);
        return new SubcategoryCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubcategoryCardAdapter.ViewHolder holder, int position) {
        /**Get the current subcategory*/

        ArrayList<Subcategory> subcategories = category.getSubcategories();

        // Get current subcategory
        Subcategory subcategory = subcategories.get(position);

        holder.subcategoryNameEditText.setText(subcategory.getName());
        holder.DescriptionEditText.setText(subcategory.getDescription());
        holder.priceEditText.setText(Double.toString(subcategory.getPrice()));

        /**Add listener to delete subcategory clicking frameLayout*/
        holder.inSubCardUserFragmentView.setOnClickListener(view -> {
            int removedPosition = holder.getAdapterPosition();
            subcategories.remove(removedPosition);
        });

        /**Listener for subcategory name*/
        holder.subcategoryNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Get the name of the subcategory
                String subcategoryNameInput = holder.subcategoryNameEditText.getText().toString();

                // Set the name to subcategory
                subcategory.setName(subcategoryNameInput);
            }
        });

        /**Listener for subcategory description*/
        holder.DescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Get the name of the subcategory
                String descriptionInput = holder.DescriptionEditText.getText().toString();

                // Set the name to subcategory
                subcategory.setDescription(descriptionInput);
            }
        });

        /**Listener for subcategory price*/
//        holder.DescriptionEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                // Get the name of the subcategory
//                String priceInput = holder.DescriptionEditText.getText().toString();
//
//                // Set the name to subcategory
//                subcategory.setPrice(priceInput);
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return category.getSubcategories().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected EditText subcategoryNameEditText,DescriptionEditText,priceEditText;
        protected FrameLayout inSubCardUserFragmentView;

        public ViewHolder(View itemView) {
            super(itemView);
            subcategoryNameEditText = itemView.findViewById(R.id.subcategoryNameEditText);
            DescriptionEditText = itemView.findViewById(R.id.DescriptionEditText);
            priceEditText = itemView.findViewById(R.id.priceEditText);

            inSubCardUserFragmentView = itemView.findViewById(R.id.addSubcategoryButton);
        }
    }
}
