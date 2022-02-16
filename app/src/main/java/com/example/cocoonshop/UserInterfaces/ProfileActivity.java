package com.example.cocoonshop.UserInterfaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.cocoonshop.SuperClasses.Constraints;
import com.example.cocoonshop.SuperClasses.FirestoreBaseActivity;
import com.example.cocoonshop.R;
import com.example.cocoonshop.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProfileActivity extends FirestoreBaseActivity {

    private ActivityProfileBinding binding;
    String userFullName, userNickName, userPhoneNumber, userBirthdate, userGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showProgress();

        getUserData();

        setSupportActionBar(binding.appBar.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.appBar.toolbar.findViewById(R.id.user_icon).setVisibility(View.GONE);


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
                Intent temp = Constraints.navigationItemSelectedMethod(item, 2, ProfileActivity.this);
                if (temp != null) {
                    startActivity(temp);
                    finish();
                }
                return true;
            }

        });


        //-----------------------------------------------------------------------------------------------

        binding.passwordEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePassDialog();
            }
        });

    }


    private void getUserData() {

        DocumentReference docRef = firebaseFirestore.collection("users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        userFullName = document.getString("userFullName");
                        userNickName = document.getString("userNickName");
                        userPhoneNumber = document.getString("userPhoneNumber");
                        userBirthdate = document.getString("userBirthdate");
                        userGender = document.getString("userGender");

                        binding.userFullName.setText(userFullName);
                        binding.userName.setText("@" + userNickName);
                        binding.profilePhone.setText(userPhoneNumber);
                        binding.profileBirthdate.setText(userBirthdate);
                        binding.profileGender.setText(userGender);
                        // from authentication
                        binding.profileEmail.setText(currentUser.getEmail());
                        getUserImage(binding.profileUserImage);

                        getFavouritesCount();
                        getCartCount();

                        progressDialog.dismiss();

                    } else {
                        Log.e("TAG", "No such document");
                    }
                } else {
                    Log.e("TAG", "get failed with ", task.getException());
                }
            }
        });

    }



    private void showChangePassDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(ProfileActivity.this).inflate(
                R.layout.layout_change_password_dailog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );

        builder.setView(view);
        String newPassword = ((EditText) view.findViewById(R.id.et_new_password)).getText().toString();
        String oldPassword = ((EditText) view.findViewById(R.id.et_old_password)).getText().toString();
        ((TextView) view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.change_user_password));
        ((Button) view.findViewById(R.id.button_cancel)).setText(getResources().getString(R.string.cancel));
        ((Button) view.findViewById(R.id.button_ok)).setText(getResources().getString(R.string.okay));
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.error);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (newPassword.trim().equals("") || oldPassword.trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Please Check All Fields", Toast.LENGTH_SHORT).show();
                } else {
                    updatePassword(oldPassword, newPassword);

                }
            }
        });

        view.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
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

    private void updatePassword(String oldPassword , String newPassword) {
        AuthCredential authCredential = EmailAuthProvider.getCredential(currentUser.getEmail(), oldPassword);
        currentUser.reauthenticate(authCredential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            currentUser.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                currentUser.reload();
                                                Toast.makeText(getApplicationContext(), "password Update for" + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                task.getException().printStackTrace();
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "password Update error" + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                            task.getException().printStackTrace();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void getFavouritesCount() {

        FirebaseFirestore.getInstance().collection("favourites").document(currentUser.getUid())
                .collection("userFavProductIds").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        int count = 0;
                        for (DocumentSnapshot document : task.getResult()) {
                            count++;

                        }
                        binding.favsCount.setText(count + "");
                    } else {
                        Log.d("TAG", "Error getting documents count: ", task.getException());
                    }
                }
            });

    }

    private void getCartCount() {
            FirebaseFirestore.getInstance().collection("shoppingCart").document(currentUser.getUid())
                    .collection("userCartProducts").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int count = 0;
                                for (DocumentSnapshot document : task.getResult()) {
                                    count++;
                                }
                                binding.cartCount.setText(count + "");
                            } else {
                                Log.d("TAG", "Error getting documents count: ", task.getException());
                            }
                        }
                    });

    }

}