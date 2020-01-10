package com.qy.ccm.aty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lijiankun24.shadowlayout.ShadowLayout;
import com.qy.ccm.R;
import com.qy.ccm.aty.account.LoginAndRegisterAty;
import com.qy.ccm.aty.wallet.CreateImportAty;
import com.qy.ccm.aty.wallet.ImputWalletAndCreateWalletAty;
import com.qy.ccm.intro.SlideFragOne;
import com.qy.ccm.utils.Utils;
import com.qy.ccm.view.CustomAppIntro;
import com.qy.ccm.view.SampleSlide;

import java.util.Objects;

public class IntroActivity extends CustomAppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showStatusBar(false);
        // Note here that we DO NOT use setContentView();
        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
//        MainFrag mainFrag = new MainFrag();
        SlideFragOne sliderPage0 = new SlideFragOne();

        addSlide(sliderPage0);
        addSlide(SampleSlide.newInstance(R.layout.slide_page_two));
        addSlide(SampleSlide.newInstance(R.layout.slide_page_three));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(getResources().getColor(R.color.color_1186ce));
        setSeparatorColor(getResources().getColor(R.color.color_1186ce));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(false);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
//        setVibrate(true);
//        setVibrateIntensity(30);

//        set buttom click

        Button start_tiyan_id = (Button) findViewById(R.id.start_tiyan_id);
        start_tiyan_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent3 = new Intent(IntroActivity.this, CreateImportAty.class);
                startActivity(intent3);
                finish();
            }
        });

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);

    }

    @Override
    protected void onPageSelected(int position) {

        LinearLayout bottomBar = (LinearLayout) findViewById(R.id.bottom);
        Button start_tiyan_id = (Button) findViewById(R.id.start_tiyan_id);
        if (bottomBar == null || start_tiyan_id == null) {
            System.out.println("");
        } else {
//
            if (position == 2) {
                bottomBar.setVisibility(View.GONE);
                start_tiyan_id.setVisibility(View.VISIBLE);

            } else {
                bottomBar.setVisibility(View.VISIBLE);
                start_tiyan_id.setVisibility(View.GONE);
            }

        }
        super.onPageSelected(position);
    }
}