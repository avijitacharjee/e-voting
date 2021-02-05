package com.tf.lite.evoting.detection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tf.lite.evoting.detection.databinding.ActivityAdminLoginBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AdminLoginActivity extends AppCompatActivity {
    private static final String TAG = "AdminLoginActivity";
    ActivityAdminLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminLoginBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        binding.submitButton.setOnClickListener(v -> {
                showDialog();
                RequestQueue requestQueue = Volley.newRequestQueue(AdminLoginActivity.this);
                String url = "https://onlinevotingdipta.000webhostapp.com/api.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                    hideDialog();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        int hf=Integer.parseInt(jsonObject.getString("hf"));
                        int mf=Integer.parseInt(jsonObject.getString("mf"));
                        int ht=Integer.parseInt(jsonObject.getString("ht"));
                        int mt=Integer.parseInt(jsonObject.getString("mt"));
                        final Calendar fromCalendar = Calendar.getInstance();
                        fromCalendar.set(Calendar.HOUR_OF_DAY,hf);
                        fromCalendar.set(Calendar.MINUTE,mf);

                        final Calendar toCalendar = Calendar.getInstance();
                        toCalendar.set(Calendar.HOUR_OF_DAY,ht);
                        toCalendar.set(Calendar.MINUTE,mt);
                        if(!(0==mf && 0==ht && 0 == mt && mt==0)){
                            if(Calendar.getInstance().getTimeInMillis()>fromCalendar.getTimeInMillis() && Calendar.getInstance().getTimeInMillis()<toCalendar.getTimeInMillis()){
                                Toast.makeText(this, "You can't login in voting time", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (binding.nidEditText.getText().toString().equals("admin1")) {
                        startActivity(new Intent(this, MainAdminActivity.class));
                    }
                    else if(binding.nidEditText.getText().toString().equals("admin2")){
                        startActivity(new Intent(this,AdminDashboard.class));
                    }
                    else {
                        Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show();
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
                        params.put("times", binding.nidEditText.getText().toString());
                        return params;
                    }
                };
                requestQueue.add(stringRequest);

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
}