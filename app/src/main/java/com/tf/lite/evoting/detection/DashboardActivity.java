package com.tf.lite.evoting.detection;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.tf.lite.evoting.detection.databinding.ActivityDashboardBinding;

import org.json.JSONArray;

public class DashboardActivity extends AppCompatActivity {
    ActivityDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater(),null,false);
        setContentView(binding.getRoot());
        binding.candidateInfoTextView.setOnClickListener(v->{
            startActivity(new Intent(this, CandidateInfoActivity.class));
        });
        try {
            String s = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,MODE_PRIVATE).getString("user","");
            JSONArray jsonArray = new JSONArray(s);
            binding.nameTextView.setText(jsonArray.getJSONObject(0).getString("name"));
            binding.fathersTextView.setText(jsonArray.getJSONObject(0).getString("father"));
            binding.mothersNameTextView.setText(jsonArray.getJSONObject(0).getString("mother"));
            binding.presentAddressTextView.setText(jsonArray.getJSONObject(0).getString("present_address"));
            binding.permaAddressTextView.setText(jsonArray.getJSONObject(0).getString("permanent_address"));
            binding.nidTextView.setText(jsonArray.getJSONObject(0).getString("nid"));
            binding.licenceTextView.setText(jsonArray.getJSONObject(0).getString("driving_licence"));
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }
}