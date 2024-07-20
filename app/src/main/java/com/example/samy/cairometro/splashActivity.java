package com.example.samy.cairometro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

@SuppressLint("CustomSplashScreen")
public class splashActivity extends AppCompatActivity {

    MediaPlayer sound;
    SharedPreferencesHelper pref;
    byte posStart,posArrival;
    String startStation,arrivalStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (pref == null) {
            pref = SharedPreferencesHelper.getInstance(this);
        }


        splash();
        callSound();
    }

    private void validation() {
        posStart = (byte) pref.getStartPos("pos start", (byte) 0);
        posArrival = (byte) pref.getArrivalPos("pos arrival", (byte) 0);

        startStation = pref.getStartStation("start","");
        arrivalStation = pref.getStartStation("arrival","");

        if (posStart != 0) {
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("startStation", startStation);
            intent.putExtra("arrivalStation", arrivalStation);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        }

    private void splash(){
        int SPLASH_DISPLAY_TIMER = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               validation();
            }
        }, SPLASH_DISPLAY_TIMER);

    }

    private void callSound(){
        sound = MediaPlayer.create(this,R.raw.train);
        sound.setVolume(.05f,.05f);
        sound.start();
    }
}