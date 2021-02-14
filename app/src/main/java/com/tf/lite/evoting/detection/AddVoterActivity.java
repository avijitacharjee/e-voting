package com.tf.lite.evoting.detection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tf.lite.evoting.detection.databinding.ActivityAddVoterBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddVoterActivity extends AppCompatActivity {
    private static final String TAG = "AdminActivity";
    ActivityAddVoterBinding binding;
    List<String> centers = Arrays.asList("--Select Voting Center--","1","2","3");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddVoterBinding.inflate(getLayoutInflater(),null,false);
        setContentView(binding.getRoot());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, centers);
        binding.votingCenterSpinner.setAdapter(adapter);
        binding.submit.setOnClickListener(v->{
            if(binding.votingCenterSpinner.getSelectedItemPosition()>0){
                v();
            }else {
                Toast.makeText(this, "Please select voting center", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showDialog(){
        binding.progressDialog.setVisibility(View.VISIBLE);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                binding.progressImage.animate().rotationBy(360).withEndAction(this).setDuration(1500).setInterpolator(new LinearInterpolator()).start();
            }
        };

        binding.progressImage.animate().rotationBy(360).withEndAction(runnable).setDuration(1500).setInterpolator(new LinearInterpolator()).start();
    }
    public void hideDialog(){
        binding.progressDialog.setVisibility(View.GONE);
        binding.progressImage.clearAnimation();
    }
    public void v() {
        showDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://onlinevotingdipta.000webhostapp.com/api.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            hideDialog();
            if(response.contains("success")){
                Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            }
        },
                error -> {
            hideDialog();
                    Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", binding.nameEditText.getText().toString());
                params.put("father", binding.fathersNameEditText.getText().toString());
                params.put("mother", binding.mothersNameEditText.getText().toString());
                params.put("present_address", binding.presentAddressEditText.getText().toString());
                params.put("permanent_address", binding.permanentAddressEditText.getText().toString());
                params.put("nid", binding.nidEditText.getText().toString());
                params.put("driving_licence", binding.drivingLicenceEditText.getText().toString());
                params.put("face_info", getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,MODE_PRIVATE).getString("face",""));
                params.put("center", binding.votingCenterSpinner.getSelectedItemPosition()+"");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,AdminDashboard.class));
    }
}