package com.example.samy.cairometro;

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
    int posStart;
    int posArrival;

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


        posStart = pref.getInt("start pos", -1);
        posArrival = pref.getInt("arrival pos", -1);

        if (posStart != -1) {
            binding.startStation.setSelection(posStart);
            binding.arrivalStation.setSelection(posArrival);
        }

    }

    public void getResult(View view) {

        String startStation = binding.startStation.getSelectedItem().toString().toLowerCase();
        String arrivalStaion = binding.arrivalStation.getSelectedItem().toString().toLowerCase();

        validation(startStation, arrivalStaion);

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
}