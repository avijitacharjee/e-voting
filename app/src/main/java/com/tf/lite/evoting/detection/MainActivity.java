package com.tf.lite.evoting.detection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        binding.camera.setOnClickListener(v -> {
            startActivity(new Intent(this, DetectorActivity.class));
        });
    }

    public void v() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://onlinevotingdipta.000webhostapp.com/api.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Log.d(TAG, "onCreate: " + response);
            getSharedPreferences("app", MODE_PRIVATE).edit().putString("user", response).apply();
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        },
                error -> {
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
}