package com.example.cocoonshop.MainActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cocoonshop.Adapters.RecyclerAdapters.CategoryItemAdapter;
import com.example.cocoonshop.Adapters.RecyclerAdapters.ProductItemAdapter;
import com.example.cocoonshop.Adapters.SliderAdapter;
import com.example.cocoonshop.SuperClasses.Constraints;
import com.example.cocoonshop.SuperClasses.FirestoreBaseActivity;
import com.example.cocoonshop.Interfaces.RecyclerInterfaces.CetogoryClickListener;
import com.example.cocoonshop.ModelClasses.RecyclerModels.CategoryItem;
import com.example.cocoonshop.ModelClasses.RecyclerModels.ProductItem;
import com.example.cocoonshop.R;

import com.example.cocoonshop.databinding.ActivityHomePageBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;
import java.util.Locale;

public class HomePageActivity extends FirestoreBaseActivity {

    private ActivityHomePageBinding binding;
    ArrayList<CategoryItem> categoriesList;
    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();

        showProgress();

        // slider -----------------------------------------------------
        imageSliderRun();

        // Categories -----------------------------------------------------
        categoriesEventChangeListener();

        // Best Sell Products -----------------------------------------------------
        bestSellProducts();

        // Internet Connection Check -----------------------------------------------------

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Toasty.success(HomePageActivity.this, "Internet Connected", Toast.LENGTH_SHORT, true).show();
            connected = true;
        } else {
            connected = false;
            showErrorDialog();
        }

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

                Intent temp = Constraints.navigationItemSelectedMethod(item, 1, HomePageActivity.this);
                if (temp != null) {
                    startActivity(temp);
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //-----------------------------------------------------

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchText) {
                Intent intent = new Intent(HomePageActivity.this, SearchActivity.class);
                intent.putExtra(Constraints.SEARCH_TEXT, searchText);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
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
                    startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(HomePageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        //-----------------------------------------------------

        binding.seeAllProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, AllCategoryProducts.class);
                intent.putExtra(Constraints.ALL_CATS, Constraints.ALL_CATS_VAL);
                startActivity(intent);
            }
        });

        binding.seeAllBestSellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, AllCategoryProducts.class);
                intent.putExtra(Constraints.BEST_SELL_PRODS, Constraints.BEST_SELL_PRODS_VAL);
                startActivity(intent);
            }
        });

        binding.shopNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, AllCategoryProducts.class);
                intent.putExtra(Constraints.DSICOUNT_PRODS, Constraints.DISCOUNT_PRODS_VAL);
                startActivity(intent);
            }
        });
        binding.navigationView.getMenu().getItem(0).setChecked(true);
    }


    //---------------------------------------------------------------------------------------------------------------------

    private void imageSliderRun() {
        int[] images = {R.drawable.sliderone, R.drawable.slidertwo, R.drawable.sliderthree};
        SliderAdapter sliderAdapter = new SliderAdapter(images);
        binding.imageSlider.setSliderAdapter(sliderAdapter);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        binding.imageSlider.startAutoCycle();
    }

    private void categoriesEventChangeListener() {

        CollectionReference questionRef = firebaseFirestore.collection("categories");

        questionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                categoriesList = new ArrayList<>();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        categoriesList.add(snapshot.toObject(CategoryItem.class));
                    }
                    categoriesRecycler(categoriesList);
                    progressDialog.dismiss();
//                    Log.d("TAG", categoriesList.size() + "");
//                    Log.d("TAG", categoriesList.get(0).getCategoryName());

                }

            }
        });
    }

    private void bestSellProducts() {

        CollectionReference questionRef = (CollectionReference) firebaseFirestore.collection("products");
        Query query = questionRef.whereEqualTo("isBestSell", true).limit(4);
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

                }
            }
        });
    }

    public void categoriesRecycler(ArrayList<CategoryItem> categoriesList) {

        CategoryItemAdapter categoriesAdapter = new CategoryItemAdapter(HomePageActivity.this, categoriesList, new CetogoryClickListener() {
            @Override
            public void getCategoryPosition(int position) {
                categoriesList.get(position);
                Log.e("SENDING_POSITION", position + "");

                if (position == 0) {
                    Intent intent = new Intent(HomePageActivity.this, AllCategoryProducts.class);
                    intent.putExtra(Constraints.CAT_WOMAN, Constraints.CAT_WOMAN_VAL);
                    startActivity(intent);

                } else if (position == 1) {
                    Intent intent = new Intent(HomePageActivity.this, AllCategoryProducts.class);
                    intent.putExtra(Constraints.CAT_MEN, Constraints.CAT_MEN_VAL);
                    startActivity(intent);

                } else if (position == 2) {
                    Intent intent = new Intent(HomePageActivity.this, AllCategoryProducts.class);
                    intent.putExtra(Constraints.CAT_KIDS, Constraints.CAT_KIDS_VAL);
                    startActivity(intent);

                }
            }
        });

        LinearLayoutManager FirstRecyclerlayoutManager = new LinearLayoutManager(HomePageActivity.this, RecyclerView.HORIZONTAL, false);
        binding.CategoriesRecycler.setLayoutManager(FirstRecyclerlayoutManager);
        binding.CategoriesRecycler.setHasFixedSize(true);
        binding.CategoriesRecycler.setAdapter(categoriesAdapter);
        categoriesAdapter.notifyDataSetChanged();
    }


    public void productsRecycler(ArrayList<ProductItem> productsList) {

        ProductItemAdapter productsAdapter = new ProductItemAdapter(this, productsList);

        LinearLayoutManager FirstRecyclerlayoutManager = new GridLayoutManager(this, 2);
        binding.BestSellRecycler.setLayoutManager(FirstRecyclerlayoutManager);
        binding.BestSellRecycler.setHasFixedSize(true);
        binding.BestSellRecycler.setAdapter(productsAdapter);
        productsAdapter.notifyDataSetChanged();
    }


    private void showErrorDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(HomePageActivity.this).inflate(
                R.layout.layout_error_dailog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.connection_error));
        ((TextView) view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.connection_error_text));
        ((Button) view.findViewById(R.id.button_ok)).setText(getResources().getString(R.string.okay));
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.error);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && data != null) {
                    //put array of speech in textView
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    binding.searchView.setQuery(result.get(0), false);

                    Intent intent = new Intent(HomePageActivity.this, SearchActivity.class);
                    intent.putExtra(Constraints.SEARCH_TEXT, result.get(0));
                    startActivity(intent);
                }

                break;
        }
    }


}

