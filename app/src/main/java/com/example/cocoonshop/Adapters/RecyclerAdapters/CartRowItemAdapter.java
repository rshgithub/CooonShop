package com.example.cocoonshop.Adapters.RecyclerAdapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cocoonshop.Interfaces.RecyclerInterfaces.ClickListener;
import com.example.cocoonshop.ModelClasses.UserCartProducts;
import com.example.cocoonshop.ModelClasses.RecyclerModels.ProductItem;
import com.example.cocoonshop.databinding.ProductRowItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartRowItemAdapter extends RecyclerView.Adapter<CartRowItemAdapter.CartRowItemViewHolder> {

    private Context context;
    ProductItem product;
    private UserCartProducts c;
    private List<UserCartProducts> cartItemList;
    private ClickListener cartItemClickListener;


    public CartRowItemAdapter(Context context, List<UserCartProducts> cartItemList, ClickListener cartItemClickListener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.cartItemClickListener = cartItemClickListener;
    }
    public CartRowItemAdapter(Context context, List<UserCartProducts> cartItemList ) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartRowItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductRowItemBinding binding = ProductRowItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartRowItemViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartRowItemAdapter.CartRowItemViewHolder holder, int position) {

        c = cartItemList.get(position);
        holder.binding.productName.setText(c.getProductName());
        holder.binding.productNewPrice.setText(String.valueOf(c.getProductNewPrice()));

        // ===========
        holder.binding.productTotalPrice.setText(String.valueOf(c.getProductTotalPrice()));
        holder.binding.productTotalQuantity.setText(String.valueOf(c.getProductTotalQuantity()));

        if (c.getProductCategory() == 1) {
            holder.binding.productCategory.setText("Woman");
        } else if (c.getProductCategory() == 2) {
            holder.binding.productCategory.setText("Men");
        } else if (c.getProductCategory() == 3) {
            holder.binding.productCategory.setText("Kids");
        }

        Glide.with(context).load(c.getProductImagePath()).into(holder.binding.productImage);

        Log.e("Here", c.getProductName() + c.getProductCategory() + c.getProductNewPrice() + c.getProductTotalPrice() + c.getProductTotalQuantity() + "");
        setUpActions(holder, position);

    }

    private void setUpActions(CartRowItemAdapter.CartRowItemViewHolder holder, int position) {

        holder.binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cartItemClickListener.deleteItemClick(position);
             }
        });
    }

    public void clear() {
        int size = cartItemList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                cartItemList.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public int getItemCount() {
        return cartItemList != null ? cartItemList.size() : 0;
    }

    public static final class CartRowItemViewHolder extends RecyclerView.ViewHolder {

        ProductRowItemBinding binding;

        public CartRowItemViewHolder(ProductRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }



    }


