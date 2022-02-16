package com.example.cocoonshop.UserInterfaces;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.cocoonshop.Adapters.RecyclerAdapters.CartRowItemAdapter;
import com.example.cocoonshop.Interfaces.RecyclerInterfaces.ClickListener;
import com.example.cocoonshop.SuperClasses.Constraints;
import com.example.cocoonshop.SuperClasses.FirestoreBaseActivity;
import com.example.cocoonshop.ModelClasses.UserCartProducts;
import com.example.cocoonshop.R;
import com.example.cocoonshop.databinding.ActivityCartBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Cart extends FirestoreBaseActivity {

    private ActivityCartBinding binding;
    private UserCartProducts tmep;
    UserCartProducts userCartProducts;
    List<UserCartProducts> productsList;
    CartRowItemAdapter cartRowItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showProgress();
        getUserCart();


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

                binding.drawerLayout.closeDrawer(GravityCompat.START);
                Intent temp = Constraints.navigationItemSelectedMethod(item, 4, Cart.this);
                if (temp != null) {
                    startActivity(temp);
                    finish();
                }
                return true;
            }
        });

        binding.deleteAllCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                deleteUserCart();
            }
        });

        binding.buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                deleteUserCart();
            }
        });
    }

    private void getUserCart() {
        firebaseFirestore.collection("shoppingCart").document(currentUser.getUid()).collection("userCartProducts")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    productsList= new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        UserCartProducts userCartProducts = doc.toObject(UserCartProducts.class);
                        userCartProducts.setKey(doc.getId());
                        productsList.add(userCartProducts);
                    }
                    cartRecycler(productsList);
                    progressDialog.dismiss();
                    Toasty.success(Cart.this, "Success !", Toast.LENGTH_SHORT).show();
                    updateTotalCost();
                } else {
                    progressDialog.dismiss();
                    Toasty.normal(Cart.this, "No products in your cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteUserCart() {

        firebaseFirestore.collection("shoppingCart").document(currentUser.getUid()).collection("userCartProducts")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        productsList = (List<UserCartProducts>) queryDocumentSnapshots.toObjects(UserCartProducts.class);

                        firebaseFirestore.collection("shoppingCart").document(currentUser.getUid()).collection("userCartProducts")
                                .document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.e("DELETED", document.getId() + "");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.e("NOT DELETED", document.getId() + "");
                            }
                        });
                    }

                    Log.e(" onSuccess ", "Removed");
                    cartRowItemAdapter.clear();
                    progressDialog.dismiss();
                    updateTotalCost();
                    Toasty.success(Cart.this, "Success !", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toasty.normal(Cart.this, "No products in your cart", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void updateTotalCost() {
        if (cartRowItemAdapter.getItemCount() != 0) {
            int totalCost = 0;

            for (UserCartProducts products : productsList) {
                totalCost += products.getProductTotalPrice();
                Log.e("jhjbhybjkjlkjjhblyhvbyg", totalCost + "");
            }
            binding.totalCost.setText(totalCost + "");

        } else {
            binding.totalCost.setText("0");
        }
    }

    private void deleteCartProduct() {

        firebaseFirestore.collection("shoppingCart").document(currentUser.getUid()).collection("userCartProducts")
                .document(userCartProducts.getKey()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toasty.success(Cart.this, userCartProducts.getProductName() + " has been deleted to your Favourites !", Toast.LENGTH_SHORT).show();
                productsList.remove(userCartProducts);
                cartRowItemAdapter.notifyDataSetChanged();
                updateTotalCost();
            }
        });
     }


    public void cartRecycler(List<UserCartProducts> shoppingCartList) {

        cartRowItemAdapter = new CartRowItemAdapter(Cart.this, shoppingCartList, new ClickListener() {
            @Override
            public void deleteItemClick(int position) {
                userCartProducts=productsList.get(position);
                deleteCartProduct();
            }
        });

        LinearLayoutManager FirstRecyclerlayoutManager = new LinearLayoutManager(Cart.this, RecyclerView.VERTICAL, false);
        binding.cartRecycler.setLayoutManager(FirstRecyclerlayoutManager);
        binding.cartRecycler.setHasFixedSize(true);
        binding.cartRecycler.setAdapter(cartRowItemAdapter);
        cartRowItemAdapter.notifyDataSetChanged();
    }


}