package com.example.samy.cairometro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.samy.cairometro.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    SharedPreferences.Editor editor;
    int posStart, posArrival;
    String startStation, arrivalStation;
    SharedPreferencesHelper pref;

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

        if (pref == null) {
            pref = SharedPreferencesHelper.getInstance(this);
        }

        posStart = pref.getStartPos("pos start", (byte) 0);
        posArrival = pref.getArrivalPos("pos arrival", (byte) 0);

        if (posStart != 0) {

            startStation = binding.startStation.getItemAtPosition(posStart).toString();
            arrivalStation = binding.arrivalStation.getItemAtPosition(posArrival).toString();

            binding.startStation.setSelection(posStart);
            binding.arrivalStation.setSelection(posArrival);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        posStart = pref.getStartPos("pos start", (byte) 0);
        posArrival = pref.getArrivalPos("pos arrival", (byte) 0);
    }

    public void getResult(View view) {

        startStation = binding.startStation.getSelectedItem().toString().toLowerCase();
        arrivalStation = binding.arrivalStation.getSelectedItem().toString().toLowerCase();

        validation(startStation, arrivalStation);

        pref.setStartPos("pos start", (byte) binding.startStation.getSelectedItemPosition());
        pref.setArrivalPos("pos arrival", (byte) binding.arrivalStation.getSelectedItemPosition());
        pref.setStartStation("start",startStation);
        pref.setArrivalStation("arrival",arrivalStation);
    }

    private void validation(String startStation, String arrivalStation) {

        if (startStation.equals("please select") || arrivalStation.equals("please select")) {

            YoYo.with(Techniques.Shake).duration(500).playOn(binding.resultBtn);

        } else if (arrivalStation.equals(startStation)) {

            YoYo.with(Techniques.Shake).duration(500).playOn(binding.resultBtn);

        } else {

            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("startStation", startStation);
            intent.putExtra("arrivalStation", arrivalStation);
            startActivity(intent);
        }
    }

    public void clearData(View view) {

        if (posStart ==0 || posArrival ==0){
            YoYo.with(Techniques.Shake).duration(500).playOn(binding.clearBtn);
        }else {
            posStart = 0;
            posArrival = 0;

            binding.startStation.setSelection(0);
            binding.arrivalStation.setSelection(0);

            pref.clear();
        }
    }

    public void change(View view) {

        if (binding.startStation.getSelectedItemPosition() == 0 || binding.arrivalStation.getSelectedItemPosition() == 0) {
            YoYo.with(Techniques.Shake).duration(500).playOn(binding.changeBtn);
        } else {
            byte temp = (byte) binding.startStation.getSelectedItemPosition();
            binding.startStation.setSelection(binding.arrivalStation.getSelectedItemPosition());
            binding.arrivalStation.setSelection(temp);
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}