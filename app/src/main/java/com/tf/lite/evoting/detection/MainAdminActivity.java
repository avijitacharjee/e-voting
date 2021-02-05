package com.tf.lite.evoting.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

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

public class MainAdminActivity extends AppCompatActivity {
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
            DialogFragment newFragment = new FromTimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "timePicker");
        });
        binding.toTimeButton.setOnClickListener(v -> {
            DialogFragment newFragment = new ToTimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "timePicker");
        });
        database.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(MainAdminActivity.this, snapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static class FromTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
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
            database.getReference("hf").setValue(hourOfDay);
            database.getReference("mf").setValue(minute);
        }
    }
    public static class ToTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

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
            database.getReference("ht").setValue(hourOfDay);
            database.getReference("mt").setValue(minute);
        }
    }




}