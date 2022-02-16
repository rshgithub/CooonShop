package com.example.cocoonshop.Adapters.RecyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cocoonshop.Interfaces.RecyclerInterfaces.CetogoryClickListener;
import com.example.cocoonshop.ModelClasses.RecyclerModels.CategoryItem;
import com.example.cocoonshop.databinding.CategoryItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.CategoryItemViewHolder> {

    private Context context;
    private ArrayList<CategoryItem> categoryItemList;
    private CetogoryClickListener cetogoryClickListener ;


    public CategoryItemAdapter(Context context, ArrayList<CategoryItem> categoryItemList, CetogoryClickListener cetogoryClickListener) {
        this.context = context;
        this.categoryItemList = categoryItemList;
        this.cetogoryClickListener = cetogoryClickListener;
    }


    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CategoryItemBinding binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryItemAdapter.CategoryItemViewHolder holder, int position) {

        CategoryItem c = categoryItemList.get(position);
        holder.binding.categoryName.setText(c.getCategoryName());
//            Log.e("Category Name", c.getCategoryName());
        Glide.with(context).load(c.getCategoryImagePath()).into(holder.binding.categoryImage);

        setUpActions(holder, position);
    }


    private void setUpActions(CategoryItemAdapter.CategoryItemViewHolder holder, int position) {
        holder.binding.productItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cetogoryClickListener != null)
                    cetogoryClickListener.getCategoryPosition(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return categoryItemList != null ? categoryItemList.size() : 0;
    }

    public static final class CategoryItemViewHolder extends RecyclerView.ViewHolder {

        CategoryItemBinding binding;

        public CategoryItemViewHolder(CategoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
