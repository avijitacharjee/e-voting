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
            getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,MODE_PRIVATE).edit().putBoolean("add_face",true).apply();
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,AdminLoginActivity.class));
    }
}