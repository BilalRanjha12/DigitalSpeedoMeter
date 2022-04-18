package com.leeddev.digitalspeedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

public class SplashActivity extends AppCompatActivity {
    private RelativeLayout getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getStarted = findViewById(R.id.btn_get_started);


        getStarted.animate().alpha(0).setDuration(2000).setInterpolator(new DecelerateInterpolator()).withStartAction(new Runnable() {
            @Override
            public void run() {
                getStarted.animate().alpha(1).setDuration(2000).setInterpolator(new AccelerateInterpolator()).start();
            }
        }).start();
        getStarted.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(intent);
        });
    }
}
