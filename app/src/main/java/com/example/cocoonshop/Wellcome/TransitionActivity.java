package com.example.cocoonshop.Wellcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.cocoonshop.MainActivities.Authentication.SignInActivity;
import com.example.cocoonshop.MainActivities.Authentication.SignUpActivity;
import com.example.cocoonshop.R;
import com.example.cocoonshop.databinding.ActivityTransitionBinding;

public class TransitionActivity extends AppCompatActivity {


    private ActivityTransitionBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding =ActivityTransitionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       Glide.with(TransitionActivity.this).load(R.drawable.sliderone).circleCrop().into(binding.transitionImage);


        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

     }




}