package com.tf.lite.evoting.detection;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tf.lite.evoting.detection.databinding.ActivityAdminBinding;

import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    private static final String TAG = "AdminActivity";
    ActivityAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater(),null,false);
        setContentView(binding.getRoot());
        binding.submit.setOnClickListener(v->{
            v();
        });
    }
    public void v() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://onlinevotingdipta.000webhostapp.com/api.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if(response.contains("success")){
                Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            }
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
                params.put("name", binding.nameEditText.getText().toString());
                params.put("father", binding.fathersNameEditText.getText().toString());
                params.put("mother", binding.mothersNameEditText.getText().toString());
                params.put("present_address", binding.presentAddressEditText.getText().toString());
                params.put("permanent_address", binding.permanentAddressEditText.getText().toString());
                params.put("nid", binding.nidEditText.getText().toString());
                params.put("driving_licence", binding.drivingLicenceEditText.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}