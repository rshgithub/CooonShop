package com.example.cocoonshop.Wellcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cocoonshop.Adapters.WalkthroughtAdapter;
import com.example.cocoonshop.R;
import com.example.cocoonshop.databinding.ActivityWalkthroughtBinding;

public class WalkthroughtActivity extends AppCompatActivity {


    ViewPager mSLideViewPager;
    LinearLayout mDotLayout;

    TextView[] dots;
    WalkthroughtAdapter walkthroughtAdapter;

    private ActivityWalkthroughtBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWalkthroughtBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getitem(0) > 0){

                    binding.slideViewPager.setCurrentItem(getitem(-1),true);

                }

            }
        });

        binding.nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getitem(0) < 3)
                    mSLideViewPager.setCurrentItem(getitem(1),true);
                else {

                    startActivity(new Intent(WalkthroughtActivity.this, TransitionActivity.class));
                    finish();

                }

            }
        });

        binding.skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(WalkthroughtActivity.this,TransitionActivity.class));
                finish();

            }
        });

        mSLideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.indicator_layout);

        walkthroughtAdapter = new WalkthroughtAdapter(this);
        mSLideViewPager.setAdapter(walkthroughtAdapter);
        setUpindicator(0);
        mSLideViewPager.addOnPageChangeListener(viewListener);

    }

    public void setUpindicator(int position){

        dots = new TextView[4];
        mDotLayout.removeAllViews();

        for (int i = 0 ; i < dots.length ; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive));
            mDotLayout.addView(dots[i]);
        }
        dots[position].setTextColor(getResources().getColor(R.color.active));

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setUpindicator(position);
            if (position > 0){
                binding.backbtn.setVisibility(View.VISIBLE);

            }else {
                binding.backbtn.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getitem(int i){

        return mSLideViewPager.getCurrentItem() + i;
    }

}