package com.tf.lite.evoting.detection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;
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
import com.tf.lite.evoting.detection.databinding.ActivityCandidateInfoBinding;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandidateInfoActivity extends AppCompatActivity {
    ActivityCandidateInfoBinding binding;
    List<String> candidates = new ArrayList<>();
    DatabaseReference obama, trump, biden;
    String id = "0", voted = "0";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCandidateInfoBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        candidates.add("--Select your vote--");
        candidates.add("Barak Obama");
        candidates.add("Donult Trump");
        candidates.add("Joe Biden");
        //binding.spinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, candidates);
        binding.spinner.setAdapter(adapter);
        obama = database.getReference("obama");
        trump = database.getReference("trump");
        biden = database.getReference("biden");
        try {
            String s = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).getString("user", "");
            JSONArray jsonArray = new JSONArray(s);
            voted = jsonArray.getJSONObject(0).getString("voted");
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        binding.submit.setOnClickListener(v -> {
            if (binding.spinner.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Please select your vote", Toast.LENGTH_LONG).show();
            } else {
                biometricAuth();
            }


            /*obama.setValue("0");
            trump.setValue("0");
            biden.setValue("0");*/
        });
        if (voted.equals("1")) {
            binding.submit.setOnClickListener(null);
            binding.submit.setText("Thanks for your vote.");
            binding.submit.setGravity(Gravity.CENTER_HORIZONTAL);
        }

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
    }

    private void biometricAuth() {
        BiometricManager.BiometricBuilder biometricBuilder = new BiometricManager.BiometricBuilder(this)
                .setTitle("e-voting app")
                .setSubtitle("Fingerprint Authentication")
                .setDescription("Illegal activity may be reported")
                .setNegativeButtonText("Exit app");
        BiometricCallback biometricCallback = new BiometricCallback() {
            @Override
            public void onSdkVersionNotSupported() {
                /*
                 *  Will be called if the device sdk version does not support Biometric authentication
                 */
                Toast.makeText(CandidateInfoActivity.this, "Biometric authentication is not supported on this device..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBiometricAuthenticationNotSupported() {
                /*
                 *  Will be called if the device does not contain any fingerprint sensors
                 */
                Toast.makeText(CandidateInfoActivity.this, "Biometric authentication is not supported on this device..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBiometricAuthenticationNotAvailable() {
                /*
                 *  The device does not have any biometrics registered in the device.
                 */
                Toast.makeText(CandidateInfoActivity.this, new Object() {
                }.getClass().getEnclosingMethod().getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBiometricAuthenticationPermissionNotGranted() {
                /*
                 *  android.permission.USE_BIOMETRIC permission is not granted to the app
                 */
                Toast.makeText(CandidateInfoActivity.this, "Biometric permission not granted..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBiometricAuthenticationInternalError(String error) {
                /*
                 *  This method is called if one of the fields such as the title, subtitle,
                 * description or the negative button text is empty
                 */
                Toast.makeText(CandidateInfoActivity.this, "There was an internal error.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                /*
                 * When the fingerprint doesn’t match with any of the fingerprints registered on the device,
                 * then this callback will be triggered.
                 */
                Toast.makeText(CandidateInfoActivity.this, "Failed to authenticate", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationCancelled() {
                /*
                 * The authentication is cancelled by the user.
                 */
                finishAffinity();
            }

            @Override
            public void onAuthenticationSuccessful() {
                /*
                 * When the fingerprint is has been successfully matched with one of the fingerprints
                 * registered on the device, then this callback will be triggered.
                 */
                switch (binding.spinner.getSelectedItemPosition()) {
                    case 1:
                        obama.setValue((Integer.parseInt(binding.obamaCount.getText().toString()) + 1) + "");
                        break;
                    case 2:
                        trump.setValue((Integer.parseInt(binding.trumpCount.getText().toString()) + 1) + "");
                        break;
                    case 3:
                        biden.setValue((Integer.parseInt(binding.bidenCount.getText().toString()) + 1) + "");
                        break;
                }
                //Toast.makeText(CandidateInfoActivity.this, "Your voting was successful.\nThanks for your precious vote..", Toast.LENGTH_LONG).show();
                v();
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                /*
                 * This method is called when a non-fatal error has occurred during the authentication
                 * process. The callback will be provided with an help code to identify the cause of the
                 * error, along with a help message.
                 */
                //Toast.makeText(CandidateInfoActivity.this, new Object(){}.getClass().getEnclosingMethod().getName(), Toast.LENGTH_SHORT).show();
                Toast.makeText(CandidateInfoActivity.this, "There's a problem with you sensor..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                /*
                 * When an unrecoverable error has been encountered and the authentication process has
                 * completed without success, then this callback will be triggered. The callback is provided
                 * with an error code to identify the cause of the error, along with the error message.
                 */
                biometricBuilder.build().authenticate(this);
                Toast.makeText(CandidateInfoActivity.this, "Must authenticate to use this app... ", Toast.LENGTH_SHORT).show();
            }
        };

        biometricBuilder.build().authenticate(biometricCallback);

    }

    public void v() {
        showDialog();
        try {
            String s = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).getString("user", "");
            JSONArray jsonArray = new JSONArray(s);
            id = jsonArray.getJSONObject(0).getString("id");
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(CandidateInfoActivity.this);
        String url = "https://onlinevotingdipta.000webhostapp.com/api.php";
        String finalId = id;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            hideDialog();
            voted = "1";
            binding.submit.setOnClickListener(null);
            binding.submit.setText("Thanks for your vote.");
            binding.submit.setGravity(Gravity.CENTER_HORIZONTAL);
            Toast.makeText(CandidateInfoActivity.this, "Your voting was successful.\nThanks for your precious vote..", Toast.LENGTH_LONG).show();
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
                params.put("update_voted", finalId);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}