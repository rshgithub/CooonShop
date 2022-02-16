package com.example.cocoonshop.MainActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import es.dmoral.toasty.Toasty;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.cocoonshop.Adapters.RecyclerAdapters.ProductItemAdapter;
import com.example.cocoonshop.SuperClasses.Constraints;
import com.example.cocoonshop.SuperClasses.FirestoreBaseActivity;
import com.example.cocoonshop.ModelClasses.RecyclerModels.ProductItem;
import com.example.cocoonshop.R;
import com.example.cocoonshop.databinding.ActivitySearchBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends FirestoreBaseActivity {

    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showProgress();

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                Intent temp = Constraints.navigationItemSelectedMethod(item, 0, SearchActivity.this);
                if (temp != null) {
                    startActivity(temp);
                    finish();
                }
                return true;
            }
        });


        // Searched Products -----------------------------------------------------

        if (getIntent() != null) {
            String SearchedProduct = getIntent().getStringExtra(Constraints.SEARCH_TEXT);
            binding.searchView.setQuery(SearchedProduct,false);
            searchInProducts(SearchedProduct);
        }


        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchText) {
                searchInProducts(searchText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                searchInProducts(searchText);
                return false;
            }
        });

        binding.speachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open a new window with mic and Hi Speak Something

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Search product by speach..");

                try {
                    startActivityForResult(intent,1);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(SearchActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchInProducts(String searchName) {
//        .whereEqualTo("productName", searchName + '\uf8ff');

        CollectionReference questionRef = (CollectionReference) firebaseFirestore.collection("products");
        Query query = questionRef.orderBy("productName").startAt(searchName).endAt(searchName + "\uf8ff");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<ProductItem> productsList = new ArrayList<>();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        productsList.add(snapshot.toObject(ProductItem.class));
                    }
                    searchRecycler(productsList);
                    progressDialog.dismiss();
                }else{
//                    searchRecycler(null);
                    Toasty.error(SearchActivity.this, "No products found", Toast.LENGTH_SHORT, true).show();
                    progressDialog.dismiss();
                }

            }
        });
    }

    public void searchRecycler(ArrayList<ProductItem> productsList) {

        ProductItemAdapter productsAdapter = new ProductItemAdapter(this, productsList);

        LinearLayoutManager FirstRecyclerlayoutManager = new GridLayoutManager(this, 2);
        binding.searchRecycler.setLayoutManager(FirstRecyclerlayoutManager);
        binding.searchRecycler.setHasFixedSize(true);
        binding.searchRecycler.setAdapter(productsAdapter);
        productsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK && data!=null){
                    //put array of speech in textView
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    binding.searchView.setQuery(result.get(0), false);
                }
                break;
        }
    }
}