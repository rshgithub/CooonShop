package com.example.cocoonshop.MainActivities;

import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import es.dmoral.toasty.Toasty;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cocoonshop.BuildConfig;
import com.example.cocoonshop.ModelClasses.RecyclerModels.ProductItem;
import com.example.cocoonshop.SuperClasses.Constraints;
import com.example.cocoonshop.SuperClasses.FirestoreBaseActivity;
import com.example.cocoonshop.ModelClasses.UserCartProducts;
import com.example.cocoonshop.R;
import com.example.cocoonshop.databinding.ActivityProductDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Product_Details extends FirestoreBaseActivity {

    ActivityProductDetailsBinding binding;
    String productImageUrl, productName, productDescription;
    int productNewPrice, productOldPrice, productCategory;
    int price, quantity, total;
    private String productKey ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // if item added to favs and cart


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
                Intent temp = Constraints.navigationItemSelectedMethod(item, 0, Product_Details.this);
                if (temp != null) {
                    startActivity(temp);
                    finish();
                }
                return true;
            }
        });

        //-------------------------------------------------------------------------

        if (getIntent() != null) {

            productKey = getIntent().getStringExtra(Constraints.PRODUCT_KEY);

//            setLikeButton();

            productImageUrl = getIntent().getStringExtra(Constraints.PRODUCT_IMG);
            productName = getIntent().getStringExtra(Constraints.PRODUCT_NAME);
            productDescription = getIntent().getStringExtra(Constraints.PRODUCT_DESC);
            productNewPrice = getIntent().getIntExtra(Constraints.PRODUCT_NEW_PRICE, 0);
            productOldPrice = getIntent().getIntExtra(Constraints.PRODUCT_OLD_PRICE, 0);

            productCategory = getIntent().getIntExtra(Constraints.PRODUCT_CAT, 0);
            if (productCategory == 1) {
                binding.productCategory.setText("Woman");
            } else if (productCategory == 2) {
                binding.productCategory.setText("Men");
            } else if (productCategory == 3) {
                binding.productCategory.setText("Kids");
            }

            if (productOldPrice == 0) {
                binding.productOldPrice.setVisibility(View.GONE);

            } else {
                binding.productOldPrice.setVisibility(View.VISIBLE);
                binding.productOldPrice.setPaintFlags(binding.productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); // set middle line
                binding.productOldPrice.setText(String.valueOf(binding.productOldPrice.getText()));
            }

            Glide.with(this).load(productImageUrl).into(binding.productImage);
            binding.productName.setText(productName);
            binding.productDescription.setText(productDescription);
            binding.productNewPrice.setText(productNewPrice + "");
            binding.productTotalPrice.setText(productNewPrice + "");
            binding.productOldPrice.setText(productOldPrice + "");

        } else {
            Toast.makeText(getBaseContext(), "No retrieved Data", Toast.LENGTH_SHORT).show();}


        binding.productDetailsAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                price = Integer.parseInt(binding.productNewPrice.getText().toString());
                quantity = Integer.parseInt(binding.productTotalQuantity.getText().toString());
                quantity += 1;
                total = price * quantity;
                binding.productTotalQuantity.setText(quantity + "");
                binding.productTotalPrice.setText(total + "");

            }
        });

        binding.productDetailsMinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                price = Integer.parseInt(binding.productNewPrice.getText().toString());
                quantity = Integer.parseInt(binding.productTotalQuantity.getText().toString());
                // cart product quantity must be at least = 1
                quantity -= 1;
                if (quantity > 0) {
                    total = price * quantity;
                    binding.productTotalQuantity.setText(quantity + "");
                    binding.productTotalPrice.setText(total + "");

                }
            }
        });

        binding.btnLike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addProductToFavs();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                deleteFromFavs();
                Toasty.normal(Product_Details.this,"you can edit from your favs ").show();
            }
        });


        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                addProductToCart();
            }
        });

        //-------------------------------------------------------------------------
        binding.shareItemDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.productImage.setDrawingCacheEnabled(true); // get imageview as bitmap
                Bitmap productImageBitmap = binding.productImage.getDrawingCache(); // enable ImageView to draw image cache.
                shareProductDetails(productImageBitmap);

            }
        });
        setLikeButton();
    }


    private void addProductToCart() {

        int finalTotal = Integer.parseInt(binding.productTotalPrice.getText().toString());
        int finalQuantity = Integer.parseInt(binding.productTotalQuantity.getText().toString());

        UserCartProducts userCartProducts = new UserCartProducts(productName, productImageUrl, productKey,
                productCategory, productNewPrice, finalTotal , finalQuantity);

        firebaseFirestore.collection("shoppingCart").document(currentUser.getUid()).collection("userCartProducts")
                .document(productKey).set(userCartProducts).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("productKey ========== ",productKey + "");
                progressDialog.dismiss();
                productAddedBtn();
                showSuccessDialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toasty.normal(Product_Details.this, productName + " Failed to add product item !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProductToFavs() {

        ProductItem productItem = new ProductItem(productKey, productName, productImageUrl, productCategory, productNewPrice);

        firebaseFirestore.collection("favourites").document(currentUser.getUid()).collection("userFavProductIds")
                .document(productKey).set(productItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toasty.success(Product_Details.this, productName + " added product to favs !", Toast.LENGTH_SHORT).show();
             }
        });
    }

//------------------------------------------------------------------------------------------------------------------------------
    private void deleteFromFavs() {

        firebaseFirestore.collection("favourites").document(currentUser.getUid()).collection("userFavProductIds")
                .document(productKey).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toasty.success(Product_Details.this, productName + " has been deleted to your Favourites !", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toasty.normal(Product_Details.this, productName + " Failed to delete from your Favourites !", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setLikeButton() {

        firebaseFirestore.collection("favourites").document(currentUser.getUid()).collection("userFavProductIds")
                .whereEqualTo("key",productKey)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        Log.d("like",task.getResult().toString());
                        if(task.getResult()!=null&&!task.getResult().isEmpty()){
                            binding.btnLike.setLiked(true);
                        }else{
                            binding.btnLike.setLiked(false);
                        }
                    }
                });
    }

    private void productAddedBtn() {

        binding.btnBuy.setText(R.string.item_added);
        binding.btnBuy.setTextColor(getResources().getColor(R.color.PopPink));
        binding.btnBuy.setBackgroundDrawable(getResources().getDrawable(R.drawable.pink_transparent));
        binding.btnBuy.setEnabled(true);

    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Product_Details.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(Product_Details.this).inflate(
                R.layout.layout_success_dailog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.cart_add_success));
        ((TextView) view.findViewById(R.id.textMessage)).setText(productName + " has been added to your cart!");
        ((Button) view.findViewById(R.id.buttonAction)).setText(getResources().getString(R.string.okay));
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.done);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
//                Toast.makeText(Product_Details.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

    }


    private void shareProductDetails(Bitmap bitmapImage) {
        Intent shareIntent;
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Share.png";
        OutputStream out = null;
        File file = new File(path);
        try {
            out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//         BuildConfig.APPLICATION_ID Show package name.
//         getLocalClassName() It will show the current activity class name.
//        .provider subclass of ContentProvider to access data

        Uri bmpUri = FileProvider.getUriForFile(Product_Details.this, BuildConfig.APPLICATION_ID + "."
                + getLocalClassName() + ".provider", file); //file = image file

        shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey check this " + productName + " it's only $" + productNewPrice + " At "
                + "https://play.google.com/store/apps/details?id=" + getPackageName() + "App");
        shareIntent.setType("image/png");
        startActivity(Intent.createChooser(shareIntent, "Share with"));

    }


}