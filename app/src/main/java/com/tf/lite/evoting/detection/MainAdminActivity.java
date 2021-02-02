package com.tf.lite.evoting.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tf.lite.evoting.detection.databinding.ActivityMainAdminBinding;

public class MainAdminActivity extends AppCompatActivity {
    ActivityMainAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainAdminBinding.inflate(getLayoutInflater(),null,false);
        setContentView(binding.getRoot());
    }
}