package com.example.cocoonshop.MainActivities;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.example.cocoonshop.SuperClasses.Constraints;
import com.example.cocoonshop.SuperClasses.FirestoreBaseActivity;
import com.example.cocoonshop.R;
import com.example.cocoonshop.databinding.ActivityContactUsBinding;
import com.google.android.material.navigation.NavigationView;


public class ContactUsActicity extends FirestoreBaseActivity {

    private ActivityContactUsBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent ReversedData = getIntent();


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
                Intent temp = Constraints.navigationItemSelectedMethod(item, 5, ContactUsActicity.this);
                if (temp != null) {
                    startActivity(temp);
                    finish();
                }
                return true;
            }
        });


        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:0592215966"));
                startActivity(call);
            }
        });

        binding.btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=100015074885381")));

            }
        });

        binding.btnWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse("http://newucas.ucas.edu.ps/")));
            }
        });


        binding.btnGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent email = new Intent(Intent.ACTION_SENDTO , Uri.parse("mailto:" + " rshurbaji@smail.ucas.edu.ps "));
                email.putExtra(Intent.EXTRA_SUBJECT, "subject");
                email.putExtra(Intent.EXTRA_TEXT, "text");
                startActivity(Intent.createChooser(email, "Choose an Email  :"));

            }
        });
    }
}