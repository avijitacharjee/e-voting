package com.tf.lite.evoting.detection;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tf.lite.evoting.detection.databinding.ActivityAdminLoginBinding;


public class AdminLoginActivity extends AppCompatActivity {
    ActivityAdminLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminLoginBinding.inflate(getLayoutInflater(),null,false);
        setContentView(binding.getRoot());
        binding.submitButton.setOnClickListener(v->{
            if(binding.nidEditText.getText().toString().equals("admin")){
                startActivity(new Intent(this,AdminActivity.class));
            }
            else{
                Toast.makeText(this,"Wrong password",Toast.LENGTH_LONG).show();
            }
        });
    }
}