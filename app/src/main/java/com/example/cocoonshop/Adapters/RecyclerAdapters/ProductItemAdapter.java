package com.example.cocoonshop.Adapters.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.example.cocoonshop.SuperClasses.Constraints;
import com.example.cocoonshop.MainActivities.Product_Details;
import com.example.cocoonshop.ModelClasses.RecyclerModels.ProductItem;
import com.example.cocoonshop.databinding.ProductItemBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductItemViewHolder> {

    private Context context;
    private ArrayList<ProductItem> productItemList;


    public ProductItemAdapter(Context context, ArrayList<ProductItem> productItemList) {
        this.context = context;
        this.productItemList = productItemList;
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ProductItemBinding binding = ProductItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {


            ProductItem p = productItemList.get(position);
            holder.binding.productName.setText(p.getProductName());
            holder.binding.productNewPrice.setText(String.valueOf(p.getProductNewPrice()));

            if(p.getProductCategory() == 1){
                holder.binding.productCategory.setText("Woman");
            }else if(p.getProductCategory() == 2){
                holder.binding.productCategory.setText("Men");
            }else if(p.getProductCategory() == 3){
                holder.binding.productCategory.setText("Kids");
            }

            if (p.getProductOldPrice() != 0) {
                holder.binding.productOldPrice.setVisibility(View.VISIBLE);
                holder.binding.productOldPrice.setPaintFlags(holder.binding.productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); // set middle line
                holder.binding.productOldPrice.setText(String.valueOf(p.getProductOldPrice()));
            } else {
                holder.binding.productOldPrice.setVisibility(View.INVISIBLE);
            }

            Glide.with(context).load(p.getProductImagePath()).into(holder.binding.productImage);

            setUpActions((holder), position);
    }

    private void setUpActions(ProductItemViewHolder holder, int position) {
        holder.binding.productItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductItem p = productItemList.get(position);
                Intent intent = new Intent(context, Product_Details.class);
                intent.putExtra(Constraints.PRODUCT_KEY, p.getKey());
                intent.putExtra(Constraints.PRODUCT_IMG, p.getProductImagePath());
                intent.putExtra(Constraints.PRODUCT_NAME, p.getProductName());
                intent.putExtra(Constraints.PRODUCT_DESC, p.getProductDescription());
                intent.putExtra(Constraints.PRODUCT_CAT, p.getProductCategory());
                intent.putExtra(Constraints.PRODUCT_NEW_PRICE, p.getProductNewPrice());
                intent.putExtra(Constraints.PRODUCT_OLD_PRICE, p.getProductOldPrice());
                Log.e("Keyyyyyyyyyyyyyyyyyyyy" , p.getKey() +"");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productItemList != null ? productItemList.size() : 0;
    }

    public static final class ProductItemViewHolder extends RecyclerView.ViewHolder {

        ProductItemBinding binding;

        public ProductItemViewHolder(ProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
