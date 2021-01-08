package com.raamaangroup.stepMeter;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button retryButton = findViewById(R.id.retryButton);

        retryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!GPSAndInternetChecker.check(MainActivity.this)) {
                    retryButton.setVisibility(View.VISIBLE);
                } else {

                    retryButton.setVisibility(View.INVISIBLE);
                    startApp();
                }
            }
        });

        if(!GPSAndInternetChecker.check(MainActivity.this)) {
            retryButton.setVisibility(View.VISIBLE);

        }

        if(retryButton.getVisibility()==View.INVISIBLE) {
            startApp();
        }

    }

    private void startApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}