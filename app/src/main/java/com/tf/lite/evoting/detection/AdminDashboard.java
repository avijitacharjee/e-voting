package com.tf.lite.evoting.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.tf.lite.evoting.detection.databinding.ActivityAdminDashboardBinding;

public class AdminDashboard extends AppCompatActivity {
    ActivityAdminDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater(),null,false);
        setContentView(binding.getRoot());
        binding.textView.setOnClickListener(v->{
            Intent intent = new Intent(this,DetectorActivity.class);
            intent.putExtra("add_face",true);
            startActivity(intent);
        });
    }
}