package com.tf.lite.evoting.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tf.lite.evoting.detection.databinding.ActivityMainAdminBinding;
import com.tf.lite.evoting.detection.pickers.TimePickerFragment;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainAdminActivity extends AppCompatActivity {
    private static final String TAG = "MainAdminActivity";
    ActivityMainAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainAdminBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());

        binding.fromTimeButton.setOnClickListener(v -> {
            DialogFragment newFragment = new FromTimePickerFragment(binding);
            newFragment.show(getSupportFragmentManager(), "timePicker");
        });
        binding.toTimeButton.setOnClickListener(v -> {
            DialogFragment newFragment = new ToTimePickerFragment(binding);
            newFragment.show(getSupportFragmentManager(), "timePicker");
        });
        getVotingInfo();
    }

    private void getVotingInfo() {
        showDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://onlinevotingdipta.000webhostapp.com/api.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            hideDialog();
            try {
                JSONArray jsonArray = new JSONArray(response);
                VoteCount voteCount = new VoteCount(jsonArray);
                binding.r1.setText(""+voteCount.getR1());
                binding.r2.setText(""+voteCount.getR2());
                binding.r3.setText(""+voteCount.getR3());

                binding.k1.setText(""+voteCount.getK1());
                binding.k2.setText(""+voteCount.getK2());
                binding.k3.setText(""+voteCount.getK3());

                binding.j1.setText(""+voteCount.getJ1());
                binding.j2.setText(""+voteCount.getJ2());
                binding.j3.setText(""+voteCount.getJ3());

                binding.rahimTotal.setText(""+voteCount.rahimTotal());
                binding.karimTotal.setText(""+voteCount.karimTotal());
                binding.jasimTotal.setText(""+voteCount.jasimTotal());
            } catch (Exception e) {

            }

        },
                error -> {
                    hideDialog();
                    Toast.makeText(MainAdminActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("get_all_users", "abcd");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public static class FromTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        ActivityMainAdminBinding binding;

        public FromTimePickerFragment(ActivityMainAdminBinding binding) {
            this.binding = binding;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            Date currentTime = Calendar.getInstance().getTime();
            binding.progressDialog.setVisibility(View.VISIBLE);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    binding.progressImage.animate().rotationBy(360).withEndAction(this).setDuration(1500).setInterpolator(new LinearInterpolator()).start();
                }
            };

            binding.progressImage.animate().rotationBy(360).withEndAction(runnable).setDuration(1500).setInterpolator(new LinearInterpolator()).start();
            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            String url = "https://onlinevotingdipta.000webhostapp.com/api.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                binding.progressDialog.setVisibility(View.GONE);
                binding.progressImage.clearAnimation();
                Toast.makeText(view.getContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                binding.fromTimeButton.setText("From: \n" + hourOfDay + ":" + minute);
            },
                    error -> {
                        binding.progressDialog.setVisibility(View.GONE);
                        binding.progressImage.clearAnimation();
                        Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return super.getHeaders();
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("update_from", "abcd");
                    params.put("hf", String.valueOf(hourOfDay));
                    params.put("mf", String.valueOf(minute));
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    public static class ToTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        ActivityMainAdminBinding binding;

        public ToTimePickerFragment(ActivityMainAdminBinding binding) {
            this.binding = binding;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            Date currentTime = Calendar.getInstance().getTime();
            binding.progressDialog.setVisibility(View.VISIBLE);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    binding.progressImage.animate().rotationBy(360).withEndAction(this).setDuration(1500).setInterpolator(new LinearInterpolator()).start();
                }
            };

            binding.progressImage.animate().rotationBy(360).withEndAction(runnable).setDuration(1500).setInterpolator(new LinearInterpolator()).start();
            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            String url = "https://onlinevotingdipta.000webhostapp.com/api.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                binding.progressDialog.setVisibility(View.GONE);
                binding.progressImage.clearAnimation();
                Toast.makeText(view.getContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                binding.toTimeButton.setText("To: \n" + hourOfDay + ":" + minute);
            },
                    error -> {
                        binding.progressDialog.setVisibility(View.GONE);
                        binding.progressImage.clearAnimation();
                        Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return super.getHeaders();
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("update_to", "abcd");
                    params.put("ht", String.valueOf(hourOfDay));
                    params.put("mt", String.valueOf(minute));
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    public void showDialog() {
        binding.progressDialog.setVisibility(View.VISIBLE);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                binding.progressImage.animate().rotationBy(360).withEndAction(this).setDuration(1500).setInterpolator(new LinearInterpolator()).start();
            }
        };

        binding.progressImage.animate().rotationBy(360).withEndAction(runnable).setDuration(1500).setInterpolator(new LinearInterpolator()).start();
    }

    public void hideDialog() {
        binding.progressDialog.setVisibility(View.GONE);
        binding.progressImage.clearAnimation();
    }

    public void setTo(String ht, String mt) {
        showDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(MainAdminActivity.this);
        String url = "https://onlinevotingdipta.000webhostapp.com/api.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            hideDialog();
            Toast.makeText(this, "Successfully updated", Toast.LENGTH_SHORT).show();
            binding.fromTimeButton.setText("To: \n" + ht + ":" + mt);
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
                params.put("update_to", "abcd");
                params.put("ht", ht);
                params.put("mt", mt);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    class VoteCount {
        private JSONArray users;
        private int data[][] = new int[4][3];
        VoteCount(JSONArray users) throws JSONException {
            this.users = users;
            calculate();
        }
        private void calculate() throws JSONException {
            for (int i=0;i<users.length();i++){
                if(users.getJSONObject(i).getString("voted").equals("1")){
                    if(users.getJSONObject(i).getString("center").equals("1")){
                        data[0][0]++;
                    }
                    if(users.getJSONObject(i).getString("center").equals("2")){
                        data[1][0]++;
                    }
                    if(users.getJSONObject(i).getString("center").equals("3")){
                        data[2][0]++;
                    }
                    data[3][0]++;
                }
                if(users.getJSONObject(i).getString("voted").equals("2")){
                    if(users.getJSONObject(i).getString("center").equals("1")){
                        data[0][1]++;
                    }
                    if(users.getJSONObject(i).getString("center").equals("2")){
                        data[1][1]++;
                    }
                    if(users.getJSONObject(i).getString("center").equals("3")){
                        data[2][1]++;
                    }
                    data[3][1]++;
                }
                if(users.getJSONObject(i).getString("voted").equals("3")){
                    if(users.getJSONObject(i).getString("center").equals("1")){
                        data[0][2]++;
                    }
                    if(users.getJSONObject(i).getString("center").equals("2")){
                        data[1][2]++;
                    }
                    if(users.getJSONObject(i).getString("center").equals("3")){
                        data[2][2]++;
                    }
                    data[3][2]++;
                }

            }
        }
        public int getR1(){
            return data[0][0];
        }
        public int getR2(){
            return data[1][0];
        }
        public int getR3(){
            return data[2][0];
        }
        public int getK1(){
            return data[0][1];
        }
        public int getK2(){
            return data[1][1];
        }
        public int getK3(){
            return data[2][1];
        }
        public int getJ1(){
            return data[0][2];
        }
        public int getJ2(){
            return data[1][2];
        }
        public int getJ3(){
            return data[2][2];
        }
        public int rahimTotal(){
            return data[3][0];
        }
        public int karimTotal(){
            return data[3][1];
        }
        public int jasimTotal(){
            return data[3][2];
        }

    }
}