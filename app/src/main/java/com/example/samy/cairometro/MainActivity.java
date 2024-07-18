package com.example.samy.cairometro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.samy.cairometro.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    SharedPreferences pref;
    int posStart, posArrival;
    String startStation, arrivalStation;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pref = getSharedPreferences("stations", MODE_PRIVATE);

        posStart = pref.getInt("start pos", 0);
        posArrival = pref.getInt("arrival pos", 0);

        if (posStart != 0) {
//            binding.startStation.setSelection(posStart);
//            binding.arrivalStation.setSelection(posArrival);
            startStation = binding.startStation.getItemAtPosition(posStart).toString();
            arrivalStation = binding.arrivalStation.getItemAtPosition(posArrival).toString();
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("startStation", startStation);
            intent.putExtra("arrivalStation", arrivalStation);
            startActivity(intent);
        } else {
            pref.edit().clear();
        }
    }

    public void getResult(View view) {

        startStation = binding.startStation.getSelectedItem().toString().toLowerCase();
        arrivalStation = binding.arrivalStation.getSelectedItem().toString().toLowerCase();

        validation(startStation, arrivalStation);

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("start pos", binding.startStation.getSelectedItemPosition());
        editor.putInt("arrival pos", binding.arrivalStation.getSelectedItemPosition());
        editor.apply();
    }

    private void validation(String startStation, String arrivalStation) {

        if (startStation.equals("please select") || arrivalStation.equals("please select")) {

            Toast.makeText(this, "please select station", Toast.LENGTH_SHORT).show();

        } else if (arrivalStation.equals(startStation)) {

            Toast.makeText(this, "please select another station", Toast.LENGTH_SHORT).show();

        } else {

            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("startStation", startStation);
            intent.putExtra("arrivalStation", arrivalStation);
            startActivity(intent);
        }
    }

    public void clearData(View view) {

        posStart = 0;
        posArrival = 0;

        startStation = "";
        arrivalStation = "";
        binding.startStation.setSelection(0);
        binding.arrivalStation.setSelection(0);
    }

    public void change(View view) {

        byte temp = (byte) binding.startStation.getSelectedItemPosition();
        binding.startStation.setSelection(binding.arrivalStation.getSelectedItemPosition());
        binding.arrivalStation.setSelection(temp);
    }
}