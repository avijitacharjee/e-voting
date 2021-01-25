package com.tf.lite.evoting.detection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tf.lite.evoting.detection.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        binding.submitButton.setOnClickListener(v -> {
            v();
        });
        binding.adminLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminLoginActivity.class));
        });
    }

    public void v() {
        showDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://onlinevotingdipta.000webhostapp.com/api.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            hideDialog();
            Log.d(TAG, "onCreate: " + response);
            getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit().putString("user", response).apply();
            getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit().putBoolean("add_face", false).apply();
            startActivity(new Intent(getApplicationContext(), DetectorActivity.class));
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
                params.put("get_user_by_nid", binding.nidEditText.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
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

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}