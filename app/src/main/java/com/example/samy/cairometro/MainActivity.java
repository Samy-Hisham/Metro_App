package com.example.samy.cairometro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.samy.cairometro.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mumayank.com.airlocationlibrary.AirLocation;

public class MainActivity extends AppCompatActivity implements AirLocation.Callback {

    private ActivityMainBinding binding;

    int posStart, posArrival;
    String startStation, arrivalStation;
    SharedPreferencesHelper pref;
    AirLocation airLocation;
    double lat, lon;
    byte flag;
    String address;
    Geocoder geocoder;

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
        pref.setStartStation("start", startStation);
        pref.setArrivalStation("arrival", arrivalStation);
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

    //when call nearest station -> fix
    public void clearData(View view) {

        if (posStart == 0 || posArrival == 0) {
            YoYo.with(Techniques.Shake).duration(500).playOn(binding.clearBtn);
        } else {
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

    public void getRouteToSelectedStation(View view) {

        if (!binding.startStation.getSelectedItem().toString().equals("please select")) {
            airLocation = new AirLocation(this, this, true, 0, "");
            airLocation.start();
            flag = 1;
        } else {
            Toast.makeText(this, "select start station", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        airLocation.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onFailure(@NonNull AirLocation.LocationFailedEnum locationFailedEnum) {
        Toast.makeText(this, "something wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(@NonNull ArrayList<Location> arrayLocation) {
        lat = arrayLocation.get(0).getLatitude();
        lon = arrayLocation.get(0).getLongitude();

        if (flag == 1)
            openMap(lat, lon);
        else if (flag == 2)
            stationsCoordinates(lat, lon);
        else if (flag == 3) {
            getGeoCoder();
        }
        flag = 0;
    }

    public void nearestStation(View view) {

        airLocation = new AirLocation(this, this, true, 0, "");
        airLocation.start();
        flag = 2;
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void openMap(double lat, double lon) {

        startStation = binding.startStation.getSelectedItem().toString();

        Uri uri = Uri.parse("https://www.google.com/maps/dir/?api=1").buildUpon()
                .appendQueryParameter("origin", lat + "," + lon)
                .appendQueryParameter("destination", "Cairo Metro of " + startStation + ",Egypt")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    private void stationsCoordinates(double lat, double lon) {

//        if (pref.getList().isEmpty()) {
        ArrayList<InfoStation> infoList = new ArrayList<>();
        InfoStation helwan = new InfoStation("helwan", 29.8489, 31.3342);
        InfoStation ainhelwan = new InfoStation("ainhelwan", 29.8628, 31.3524);
        InfoStation helwanuniversity = new InfoStation("helwan university", 29.8689, 31.3477);
        InfoStation wadihof = new InfoStation("wadi hof", 29.8794, 31.3408);
        InfoStation hadayeqhelwan = new InfoStation("hadayeq helwan", 29.8971, 31.3314);
        InfoStation elmaasara = new InfoStation("el-maasara", 29.906, 31.3271);
        InfoStation toraelasmant = new InfoStation("tora el-asmant", 29.9258, 31.3151);
        InfoStation kozzika = new InfoStation("kozzika", 29.936, 31.3091);
        InfoStation toraelbalad = new InfoStation("tora el-balad", 29.9463, 31.301);
        InfoStation sakanatelmaadi = new InfoStation("sakanat el-maadi", 29.9527, 31.2909);
        InfoStation maadi = new InfoStation("maadi", 29.9598, 31.2854);
        InfoStation hadayeqelmaadi = new InfoStation("hadayeq el-maadi", 29.97, 31.278);
        InfoStation darelsalam = new InfoStation("dar el-salam", 29.9819, 31.2696);
        InfoStation elzahraa = new InfoStation("el-zahraa", 29.9952, 31.2589);
        InfoStation margirgis = new InfoStation("mar girgis", 30.0058, 31.2569);
        InfoStation elmalekelsaleh = new InfoStation("el-malek el-saleh", 30.0168, 31.2584);
        InfoStation alSayyedazeinab = new InfoStation("alSayyeda zeinab", 30.0291, 31.2627);
        InfoStation saadzaghloul = new InfoStation("saad zaghloul", 30.0366, 31.2655);
        InfoStation sadat = new InfoStation("sadat", 30.0443, 31.2631);
        InfoStation nasser = new InfoStation("nasser", 30.0535, 31.2661);
        InfoStation orabi = new InfoStation("orabi", 30.0574, 31.2699);
        InfoStation alshohadaa = new InfoStation("al-shohadaa", 30.0618, 31.2734);
        InfoStation ghamra = new InfoStation("ghamra", 30.0688, 31.2921);
        InfoStation eldemerdash = new InfoStation("el-demerdash", 30.0771, 31.3053);
        InfoStation manshietelsadr = new InfoStation("manshiet el-sadr", 30.0822, 31.3152);
        InfoStation kobrielqobba = new InfoStation("kobri el-qobba", 30.0869, 31.3212);
        InfoStation hammamatelqobba = new InfoStation("hammamat el-qobba", 30.0902, 31.3255);
        InfoStation sarayelqobba = new InfoStation("saray el-qobba", 30.0979, 31.3322);
        InfoStation hadayeqelzaitoun = new InfoStation("hadayeq el-zaitoun", 30.105, 31.3374);
        InfoStation helmeyetelzaitoun = new InfoStation("helmeyet el-zaitoun", 30.1142, 31.3413);
        InfoStation elmatareyya = new InfoStation("el-matareyya", 30.1212, 31.3413);
        InfoStation ainshams = new InfoStation("ain shams", 30.131, 31.3465);
        InfoStation ezbetelnakhl = new InfoStation("ezbet el-nakhl", 30.1392, 31.3518);
        InfoStation elmarg = new InfoStation("el-marg", 30.152, 31.363);
        InfoStation newelmarg = new InfoStation("new el-marg", 30.1634, 31.3657);
        //line2
        InfoStation elmounib = new InfoStation("el mounib", 29.9813, 31.2392);
        InfoStation sakiatmekki = new InfoStation("sakiat mekki", 29.9954, 31.2359);
        InfoStation ommelmisryeen = new InfoStation("omm el misryeen", 30.0052, 31.2354);
        InfoStation giza = new InfoStation("giza", 30.0105, 31.2344);
        InfoStation faisal = new InfoStation("faisal", 30.0172, 31.2315);
        InfoStation cairouniversity = new InfoStation("cairo university", 30.0259, 31.2286);
        InfoStation elbohooth = new InfoStation("el-bohooth", 30.0357, 31.2277);
        InfoStation dokki = new InfoStation("dokki", 30.0382, 31.2394);
        InfoStation opera = new InfoStation("opera", 30.0419, 31.2526);
        InfoStation naguib = new InfoStation("naguib", 30.0443, 31.2631);
        InfoStation attaba = new InfoStation("attaba", 30.0523, 31.2742);
        InfoStation massara = new InfoStation("massara", 30.071, 31.2723);
        InfoStation roadelfarag = new InfoStation("road el-farag", 30.0804, 31.2728);
        InfoStation stteresa = new InfoStation("st.teresa", 30.0883, 31.2728);
        InfoStation khalafawy = new InfoStation("khalafawy", 30.0979, 31.2728);
        InfoStation mezallat = new InfoStation("mezallat", 30.1049, 31.274);
        InfoStation kolietelzeraa = new InfoStation("koliet el-zeraa", 30.1137, 31.2761);
        InfoStation shobraelkheima = new InfoStation("shobra el kheima", 30.1223, 31.272);
        //line3
//        InfoStation airport = new InfoStation("airport",);
//        InfoStation ahmedgalal = new InfoStation("ahmed galal",);
        InfoStation adlymansour = new InfoStation("adlymansour", 30.1469, 31.4488);
        InfoStation elhaykeste = new InfoStation("el haykeste", 30.1436, 31.4321);
        InfoStation omaribnelkhattab = new InfoStation("omar ibn el-khattab", 30.1404, 31.4215);
        InfoStation qobaa = new InfoStation("qobaa", 30.1347, 31.4112);
        InfoStation heshambarakat = new InfoStation("hesham barakat", 30.131, 31.4002);
        InfoStation elnozha = new InfoStation("el-nozha", 30.1282, 31.3875);
        InfoStation nadielshams = new InfoStation("nadi el-shams", 30.1223, 31.3714);
        InfoStation alfmaskan = new InfoStation("alf maskan", 30.118, 31.3673);
        InfoStation heliopolissquare = new InfoStation("heliopolis square", 30.1079, 31.3655);
        InfoStation haroun = new InfoStation("haroun", 30.101, 31.3602);
        InfoStation alahram = new InfoStation("al-ahram", 30.0912, 31.3539);
        InfoStation koleyetelbanat = new InfoStation("koleyet el-banat", 30.0837, 31.3563);
        InfoStation stadium = new InfoStation("stadium", 30.0728, 31.3448);
        InfoStation fairzone = new InfoStation("fair zone", 30.0733, 31.3285);
        InfoStation abbassia = new InfoStation("abbassia", 30.0697, 31.3082);
        InfoStation abdoupasha = new InfoStation("abdou pasha", 30.0648, 31.3022);
        InfoStation elgeish = new InfoStation("el geish", 30.0618, 31.2943);
        InfoStation babelshaaria = new InfoStation("bab el shaaria", 30.0539, 31.2833);
        InfoStation maspero = new InfoStation("maspero", 30.0554, 31.2596);
        InfoStation safaahegazy = new InfoStation("safaa hegazy", 30.0623, 31.2498);
        InfoStation kitkat = new InfoStation("kit kat", 30.0667, 31.2404);
        //branch3
        InfoStation bulaqeldakroor = new InfoStation("bulaq el-dakroor", 30.0361, 31.2239);
        InfoStation gamaateldowalalarabiya = new InfoStation("gamaat el dowal al-arabiya", 30.0508, 31.2272);
        InfoStation wadielnil = new InfoStation("wadi el-nil", 30.0584, 31.2284);
        InfoStation eltawfikeya = new InfoStation("el-tawfikeya", 30.0652, 31.2298);
        InfoStation sudanstreet = new InfoStation("sudan street", 30.0697, 31.2327);
        InfoStation imbaba = new InfoStation("imbaba", 30.0758, 31.2349);
        InfoStation elbohy = new InfoStation("elbohy", 30.082, 31.2378);
        InfoStation alqawmeyaalarabiya = new InfoStation("al-qawmeya al-arabiya", 30.0932, 31.2363);
        InfoStation ringroad = new InfoStation("ring road", 30.0963, 31.227);
        InfoStation rodalfaragaxis = new InfoStation("rod al-farag axis", 30.1018, 31.2116);

        Collections.addAll(infoList, helwan, ainhelwan, helwanuniversity, wadihof, hadayeqhelwan, elmaasara, toraelasmant, kozzika, toraelbalad, sakanatelmaadi
                , maadi, hadayeqelmaadi, elzahraa, darelsalam, margirgis, elmalekelsaleh, alSayyedazeinab, saadzaghloul, sadat, nasser, orabi, alshohadaa, ghamra, eldemerdash
                , manshietelsadr, kobrielqobba, hammamatelqobba, sarayelqobba, hadayeqelzaitoun, helmeyetelzaitoun, elmatareyya, ainshams, ezbetelnakhl, elmarg, newelmarg
                , shobraelkheima, kolietelzeraa, mezallat, khalafawy, stteresa, roadelfarag, massara, attaba, naguib, opera, dokki, elbohooth, cairouniversity, faisal, giza, ommelmisryeen, sakiatmekki
                , sakiatmekki, elmounib, kitkat, safaahegazy, maspero, babelshaaria, elgeish, abdoupasha, abbassia, fairzone, stadium, koleyetelbanat, alahram, haroun, heliopolissquare, alfmaskan, nadielshams
                , elnozha, heshambarakat, qobaa, omaribnelkhattab, elhaykeste, adlymansour, rodalfaragaxis, ringroad, alqawmeyaalarabiya, elbohy, imbaba, sudanstreet, eltawfikeya, wadielnil, gamaateldowalalarabiya,
                bulaqeldakroor, adlymansour);

        calcDistance(lat, lon, infoList);
//            pref.saveList(infoList);
//        } else {
//            calcDistance(lat, lon, (ArrayList<InfoStation>) pref.getList());
//        }

    }

    @SuppressLint("SetTextI18n")
    private void calcDistance(double lat, double lon, ArrayList<InfoStation> infoList) {

        double latStation, lonStation, distance;
        //need inhance
        HashMap<String, Double> wallet = new HashMap<>();

        for (InfoStation infoStation : infoList) {

            latStation = infoStation.getLat();
            lonStation = infoStation.getLog();

            distance = DistanceCalculator.calculateDistance(lat, lon, latStation, lonStation);

            wallet.put(infoStation.getName(), distance);
        }

        Map.Entry<String, Double> firstEntry = wallet.entrySet().iterator().next();
        String firstName = firstEntry.getKey();
        double firstDistance = firstEntry.getValue();

        for (Map.Entry<String, Double> entry : wallet.entrySet()) {

            if (entry.getValue() < firstDistance) {
                firstDistance = entry.getValue();
                firstName = entry.getKey();
            }
        }
//        binding.txt.setText("nearest station is: " + firstName + " " + firstDistance);

        int position = getIndexInSpinner(binding.startStation, firstName);

        if (binding.startStation.getSelectedItem().toString().equals("please select")) {
            if (position != -1) {
                binding.startStation.setSelection(position);
                YoYo.with(Techniques.Bounce).duration(500).repeat(1).playOn(binding.startStation);
            }
        }else if (binding.arrivalStation.getSelectedItem().toString().equals("please select")) {
            if (position != -1) {
                binding.arrivalStation.setSelection(position);
                YoYo.with(Techniques.Bounce).duration(500).repeat(1).playOn(binding.arrivalStation);
            }
        }
    }

    private int getIndexInSpinner(Spinner spinner, String item) {

        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            if (spinner.getAdapter().getItem(i).toString().equals(item)) {
                return i; // Return the index if the item is found
            }
        }
        return -1; // Return -1 if the item is not found
    }

    public void getAddress(View view) {
        address = binding.startAddrassTxt.getText().toString();
//        addressDestination = binding.startAddrassTxt.getText().toString();

        if (address.isEmpty()) {
            YoYo.with(Techniques.Shake).duration(500).playOn(binding.addrassBtn);
        } else {

            airLocation = new AirLocation(this, this, true, 0, "");
            airLocation.start();
            flag = 3;
        }
    }

    private void getGeoCoder() {
        geocoder = new Geocoder(this);

        try {
            List<Address> addresses = geocoder.getFromLocationName(address + " cairo", 1);

            double latAddres = addresses.get(0).getLatitude();
            double lonAddres = addresses.get(0).getLongitude();

            stationsCoordinates(latAddres, lonAddres);

        } catch (Exception e) {
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        //airlocation
        airLocation = null;
        geocoder = null;
        binding = null;
        super.onDestroy();
    }
}