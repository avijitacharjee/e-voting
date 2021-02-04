package com.tf.lite.evoting.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tf.lite.evoting.detection.R;
import com.tf.lite.evoting.detection.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater(),null,false);
        setContentView(binding.getRoot());
        handler.postDelayed(() -> startActivity(new Intent(SplashScreenActivity.this,MainActivity.class)),500);
    }
}