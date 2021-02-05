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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainAdminActivity extends AppCompatActivity {
    private static final String TAG = "MainAdminActivity";
    ActivityMainAdminBinding binding;
    DatabaseReference obama, trump, biden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainAdminBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        obama = database.getReference("obama");
        trump = database.getReference("trump");
        biden = database.getReference("biden");

        obama.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.obamaCount.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        trump.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.trumpCount.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        biden.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.bidenCount.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.fromTimeButton.setOnClickListener(v -> {
            DialogFragment newFragment = new FromTimePickerFragment(binding);
            newFragment.show(getSupportFragmentManager(), "timePicker");
        });
        binding.toTimeButton.setOnClickListener(v -> {
            DialogFragment newFragment = new ToTimePickerFragment(binding);
            newFragment.show(getSupportFragmentManager(), "timePicker");
        });

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
                binding.fromTimeButton.setText("From: \n"+hourOfDay+":"+minute);
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
        public ToTimePickerFragment(ActivityMainAdminBinding binding){
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
                binding.toTimeButton.setText("To: \n"+hourOfDay+":"+minute);
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

public void setTo(String ht,String mt) {
        showDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(MainAdminActivity.this);
        String url = "https://onlinevotingdipta.000webhostapp.com/api.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            hideDialog();
            Toast.makeText(this, "Successfully updated", Toast.LENGTH_SHORT).show();
            binding.fromTimeButton.setText("To: \n"+ht+":"+mt);
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

}