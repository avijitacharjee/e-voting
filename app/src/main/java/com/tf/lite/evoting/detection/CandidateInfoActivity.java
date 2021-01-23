package com.tf.lite.evoting.detection;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tf.lite.evoting.detection.databinding.ActivityCandidateInfoBinding;

import java.util.ArrayList;
import java.util.List;

public class CandidateInfoActivity extends AppCompatActivity {
    ActivityCandidateInfoBinding binding;
    List<String> candidates = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCandidateInfoBinding.inflate(getLayoutInflater(),null,false);
        setContentView(binding.getRoot());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        candidates.add("--Select your vote--");
        candidates.add("Barak Obama");
        candidates.add("Donult Trump");
        candidates.add("Joe Biden");
        //binding.spinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,candidates);
        binding.spinner.setAdapter(adapter);
        DatabaseReference obama = database.getReference("obama");
        DatabaseReference trump = database.getReference("trump");
        DatabaseReference biden = database.getReference("biden");
        binding.submit.setOnClickListener(v->{
            switch (binding.spinner.getSelectedItemPosition())
            {
                case 0: Toast.makeText(this,"Please select your vote", Toast.LENGTH_LONG).show();break;
                case 1 : obama.setValue((Integer.parseInt(binding.obamaCount.getText().toString())+1)+"");break;
                case 2 : trump.setValue((Integer.parseInt(binding.trumpCount.getText().toString())+1)+"");break;
                case 3 : biden.setValue((Integer.parseInt(binding.bidenCount.getText().toString())+1)+"");break;
            }

            /*obama.setValue("0");
            trump.setValue("0");
            biden.setValue("0");*/
        });

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
}