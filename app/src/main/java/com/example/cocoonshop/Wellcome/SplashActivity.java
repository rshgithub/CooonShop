package com.example.cocoonshop.Wellcome;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.cocoonshop.SuperClasses.Constraints;
import com.example.cocoonshop.SuperClasses.FirestoreBaseActivity;
import com.example.cocoonshop.MainActivities.HomePageActivity;
import com.example.cocoonshop.R;
import com.example.cocoonshop.SharedPrefrences.SharedPreferencesFile;
import com.example.cocoonshop.databinding.ActivitySplashBinding;

import androidx.core.content.res.ResourcesCompat;

public class SplashActivity extends FirestoreBaseActivity {

    private ActivitySplashBinding binding ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding =ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Typeface typeface = ResourcesCompat.getFont(this, R.font.dancing_script_bold);

        binding.appname.setTypeface(typeface);

        YoYo.with(Techniques.FadeIn)
                .duration(2000) // Time it for logo takes to bounce up and down
                .playOn(binding.appname);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                SharedPreferencesFile sharedPreferencesFile = new SharedPreferencesFile();
                sharedPreferencesFile.SessionManager(SplashActivity.this);
                boolean isFirstTime = sharedPreferencesFile.getWalkthroughtFirstTime();  // return true

                if (isFirstTime) {
                    sharedPreferencesFile.setWalkthrought(false);
                    startActivity(new Intent(SplashActivity.this, WalkthroughtActivity.class));
                    finish(); // delete this activity from activities stack
                }else{
                    if(currentUser!=null){
                        startActivity(new Intent(SplashActivity.this, HomePageActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashActivity.this, TransitionActivity.class));
                        finish();
                    }

                }
            }
        }, Constraints.SPLASH_TIME_OUT);
    }

}