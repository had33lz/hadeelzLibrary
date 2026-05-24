package com.example.hadeelzlibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hadeelzlibrary.R;

public class SplashActivity extends AppCompatActivity {

    // How long splash stays (3 seconds)
    private static final int SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // After delay → go to LoginActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // close splash so user can't go back to it
            }
        }, SPLASH_DELAY);
    }
}