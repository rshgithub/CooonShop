package com.example.cocoonshop.MainActivities;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.cocoonshop.Adapters.RecyclerAdapters.ProductItemAdapter;
import com.example.cocoonshop.SuperClasses.Constraints;
import com.example.cocoonshop.SuperClasses.FirestoreBaseActivity;
import com.example.cocoonshop.ModelClasses.RecyclerModels.ProductItem;
import com.example.cocoonshop.R;
import com.example.cocoonshop.databinding.ActivityAllCategoryProductsBinding;
import com.example.cocoonshop.databinding.NavHeaderBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllCategoryProducts extends FirestoreBaseActivity {

    private ActivityAllCategoryProductsBinding binding;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllCategoryProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showProgress();
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
                Intent temp = Constraints.navigationItemSelectedMethod(item, 0, AllCategoryProducts.this);
                if (temp != null) {
                    startActivity(temp); finish();
                }
                return true;
            }
        });

        //---------------------------------------------------------------------------------------------------------------------

        intent = getIntent();

        if (getIntent() != null) {

            if (intent.getIntExtra(Constraints.ALL_CATS, 0) == Constraints.ALL_CATS_VAL) {
                allFirestoreProducts();
                binding.categoryName.setText("All Products");

            } else if (intent.getIntExtra(Constraints.DSICOUNT_PRODS, 0) == Constraints.DISCOUNT_PRODS_VAL) {
                discountedProducts();
                binding.categoryName.setText("All Discounted Products");

            } else if (intent.getIntExtra(Constraints.BEST_SELL_PRODS, 0) == Constraints.BEST_SELL_PRODS_VAL) {
                bestSellProducts();
                binding.categoryName.setText("All Best Sell Products");

            } else if (intent.getIntExtra(Constraints.CAT_WOMAN, 0) == Constraints.CAT_WOMAN_VAL) {
                womanProducts();
                binding.categoryName.setText("All Woman Products");

            } else if (intent.getIntExtra(Constraints.CAT_MEN, 0) == Constraints.CAT_MEN_VAL) {
                menProducts();
                binding.categoryName.setText("All Men Products");

            } else if (intent.getIntExtra(Constraints.CAT_KIDS, 0) == Constraints.CAT_KIDS_VAL) {
                kidsProducts();
                binding.categoryName.setText("All Kids Products");
            }


        } else {
            Toast.makeText(getBaseContext(), "No retrieved Data", Toast.LENGTH_SHORT).show();
        }

    }


    private void allFirestoreProducts() {

        CollectionReference questionRef = firebaseFirestore.collection("products");
        questionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<ProductItem> productsList = new ArrayList<ProductItem>();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        ProductItem product = snapshot.toObject(ProductItem.class);
                        product.setKey(snapshot.getId());
                        productsList.add(product);
                    }
                    productsRecycler(productsList);
                    progressDialog.dismiss();
                }

            }
        });
    }

    private void bestSellProducts() {

        CollectionReference questionRef = firebaseFirestore.collection("products");
        Query query = questionRef.whereEqualTo("isBestSell", true);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<ProductItem> productsList = new ArrayList<>();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        ProductItem product = snapshot.toObject(ProductItem.class);
                        product.setKey(snapshot.getId());
                        productsList.add(product);
                    }
                    productsRecycler(productsList);
                    progressDialog.dismiss();
                }

            }
        });
    }

    private void discountedProducts() {

        CollectionReference questionRef = firebaseFirestore.collection("products");
        Query query = questionRef.whereNotEqualTo("productOldPrice", 0);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<ProductItem> productsList = new ArrayList<ProductItem>();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        ProductItem product = snapshot.toObject(ProductItem.class);
                        product.setKey(snapshot.getId());
                        productsList.add(product);
                    }
                    productsRecycler(productsList);
                    progressDialog.dismiss();
                }

            }
        });
    }

    private void womanProducts() {

        CollectionReference questionRef = firebaseFirestore.collection("products");
        Query query = questionRef.whereEqualTo("productCategory", 1);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<ProductItem> productsList = new ArrayList<ProductItem>();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        ProductItem product = snapshot.toObject(ProductItem.class);
                        product.setKey(snapshot.getId());
                        productsList.add(product);
                    }
                    productsRecycler(productsList);
                    progressDialog.dismiss();
                }

            }
        });
    }

    private void menProducts() {

        CollectionReference questionRef = firebaseFirestore.collection("products");
        Query query = questionRef.whereEqualTo("productCategory", 2);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<ProductItem> productsList = new ArrayList<ProductItem>();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        ProductItem product = snapshot.toObject(ProductItem.class);
                        product.setKey(snapshot.getId());
                        productsList.add(product);
                    }
                    productsRecycler(productsList);
                    progressDialog.dismiss();
                }

            }
        });
    }

    private void kidsProducts() {

        CollectionReference questionRef = firebaseFirestore.collection("products");
        Query query = questionRef.whereEqualTo("productCategory", 3);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<ProductItem> productsList = new ArrayList<ProductItem>();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        ProductItem product = snapshot.toObject(ProductItem.class);
                        product.setKey(snapshot.getId());
                        productsList.add(product);
                    }
                    productsRecycler(productsList);
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void productsRecycler(ArrayList<ProductItem> productsList) {

        ProductItemAdapter productsAdapter = new ProductItemAdapter(this, productsList);

        LinearLayoutManager FirstRecyclerlayoutManager = new GridLayoutManager(this, 2);
        binding.categoryProductsRecycler.setLayoutManager(FirstRecyclerlayoutManager);
        binding.categoryProductsRecycler.setHasFixedSize(true);
        binding.categoryProductsRecycler.setAdapter(productsAdapter);
        productsAdapter.notifyDataSetChanged();
    }

}