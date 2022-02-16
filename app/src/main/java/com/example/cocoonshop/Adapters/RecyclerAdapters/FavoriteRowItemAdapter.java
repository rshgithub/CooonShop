package com.example.cocoonshop.Adapters.RecyclerAdapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cocoonshop.Interfaces.RecyclerInterfaces.ClickListener;
import com.example.cocoonshop.SuperClasses.Constraints;
import com.example.cocoonshop.MainActivities.Product_Details;
import com.example.cocoonshop.ModelClasses.RecyclerModels.ProductItem;
import com.example.cocoonshop.databinding.ProductRowItemBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import static com.example.cocoonshop.SuperClasses.FirestoreBaseActivity.currentUser;
import static com.example.cocoonshop.SuperClasses.FirestoreBaseActivity.firebaseFirestore;

public class FavoriteRowItemAdapter extends RecyclerView.Adapter<FavoriteRowItemAdapter.FavouritRowItemViewHolder>{

    private Context context;
    private List<ProductItem> favouritItemList;
    private ProductItem p;
    private ClickListener clickListener;


    public FavoriteRowItemAdapter(Context context, List<ProductItem> favouritItemList , ClickListener clickListener) {
        this.context = context;
        this.favouritItemList = favouritItemList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FavouritRowItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductRowItemBinding binding = ProductRowItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FavouritRowItemViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FavoriteRowItemAdapter.FavouritRowItemViewHolder holder, int position) {

        p = favouritItemList.get(position);
        holder.binding.productName.setText(p.getProductName());
        holder.binding.productNewPrice.setText(String.valueOf(p.getProductNewPrice()));

        if(p.getProductCategory() == 1){
            holder.binding.productCategory.setText("Woman");
        }else if(p.getProductCategory() == 2){
            holder.binding.productCategory.setText("Men");
        }else if(p.getProductCategory() == 3){
            holder.binding.productCategory.setText("Kids");
        }

        holder.binding.linearLayoutForTotal.setVisibility(View.INVISIBLE);
        Glide.with(context).load(p.getProductImagePath()).into(holder.binding.productImage);

        setUpActions((holder), position);
    }


    private void setUpActions(FavoriteRowItemAdapter.FavouritRowItemViewHolder holder, int position) {

        holder.binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.deleteItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouritItemList != null ? favouritItemList.size(): 0;
    }

    public static final class FavouritRowItemViewHolder extends RecyclerView.ViewHolder{

        ProductRowItemBinding binding;

        public FavouritRowItemViewHolder(ProductRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void clear() {
        int size = favouritItemList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                favouritItemList.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

}
