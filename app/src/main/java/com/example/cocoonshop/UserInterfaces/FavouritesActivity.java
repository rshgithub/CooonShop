package com.example.cocoonshop.UserInterfaces;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.cocoonshop.Adapters.RecyclerAdapters.FavoriteRowItemAdapter;
import com.example.cocoonshop.Interfaces.RecyclerInterfaces.ClickListener;
import com.example.cocoonshop.MainActivities.Product_Details;
import com.example.cocoonshop.ModelClasses.UserCartProducts;
import com.example.cocoonshop.SuperClasses.Constraints;
import com.example.cocoonshop.SuperClasses.FirestoreBaseActivity;
import com.example.cocoonshop.ModelClasses.RecyclerModels.ProductItem;
import com.example.cocoonshop.R;
import com.example.cocoonshop.databinding.ActivityFavouritesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends FirestoreBaseActivity {

    private ActivityFavouritesBinding binding;
    private List<ProductItem> favProductsList;
    private  FavoriteRowItemAdapter favoritesAdapter;
    private ProductItem productItem ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavouritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        favProductsList = new ArrayList<>();

        showProgress();
        getAllFavs();

        // Nav Drawer + Toolbar -----------------------------------------------------

        setSupportActionBar(binding.appBar.toolbar);
        getUserImage(binding.appBar.toolbar.findViewById(R.id.user_icon));

        getUserFullName(binding.navigationView.getHeaderView(0).findViewById(R.id.user_full_name));
        getUserNickName(binding.navigationView.getHeaderView(0).findViewById(R.id.user_name));
        getUserImage(binding.navigationView.getHeaderView(0).findViewById(R.id.user_image));

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.appBar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                binding.drawerLayout.closeDrawer(GravityCompat.START); // ?
                Intent temp = Constraints.navigationItemSelectedMethod(item, 3, FavouritesActivity.this);
                if (temp != null) {
                    startActivity(temp);
                    finish();
                }
                return true;
            }
        });

        binding.deleteAllFavsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                deleteAllUserFavs();
            }
        });
    }


    private void getAllFavs() {
        firebaseFirestore.collection("favourites").document(currentUser.getUid()).collection("userFavProductIds")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {

                    favProductsList = queryDocumentSnapshots.toObjects(ProductItem.class);
                    favProductsRecycler(favProductsList);

                    progressDialog.dismiss();
                    Toasty.success(FavouritesActivity.this, "Success !", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toasty.normal(FavouritesActivity.this, "No Favourites :( !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteAllUserFavs() {
        firebaseFirestore.collection("favourites").document(currentUser.getUid()).collection("userFavProductIds")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {

                    for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                        favProductsList = (List<ProductItem>) queryDocumentSnapshots.toObjects(ProductItem.class);

                        firebaseFirestore.collection("favourites").document(currentUser.getUid()).collection("userFavProductIds")
                                .document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.e("DELETED" , document.getId() + "");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.e("NOT DELETED" , document.getId() + "");
                            }
                        });
                    }

                    favoritesAdapter.clear();
                    progressDialog.dismiss();
                    Toasty.success(FavouritesActivity.this, "Success !", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toasty.normal(FavouritesActivity.this, "No products in your cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void favProductsRecycler(List<ProductItem> favProductsList) {

        favoritesAdapter = new FavoriteRowItemAdapter(FavouritesActivity.this, favProductsList, new ClickListener() {
            @Override
            public void deleteItemClick(int position) {
                productItem = favProductsList.get(position);
                deleteFavProduct();
            }
        });

        LinearLayoutManager FirstRecyclerlayoutManager = new LinearLayoutManager(FavouritesActivity.this, RecyclerView.VERTICAL, false);
        binding.favouritesProductsRecycler.setLayoutManager(FirstRecyclerlayoutManager);
        binding.favouritesProductsRecycler.setHasFixedSize(true);
        binding.favouritesProductsRecycler.setAdapter(favoritesAdapter);
        favoritesAdapter.notifyDataSetChanged();
    }

    private void deleteFavProduct() {

        firebaseFirestore.collection("favourites").document(currentUser.getUid()).collection("userFavProductIds")
                .document(productItem.getKey()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toasty.success(FavouritesActivity.this, productItem.getProductName() + " has been deleted to your Favourites !", Toast.LENGTH_SHORT).show();
                    favProductsList.remove(productItem);
                    favoritesAdapter.notifyDataSetChanged();
                }
            });
    }

}
