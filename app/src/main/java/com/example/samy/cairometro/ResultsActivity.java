package com.example.samy.cairometro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.samy.cairometro.databinding.ActivityResultsBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    List<String> line1 = Arrays.asList("helwan", "ainhelwan", "helwan university", "wadi hof", "hadayeq helwan",
            "el-maasara", "tora el-asmant", "kozzika", "tora el-balad", "sakanat el-maadi", "maadi", "hadayeq el-maadi",
            "dar el-salam", "el-zahraa", "mar girgis", "el-malek el-saleh", "alSayyeda zeinab", "saad zaghloul",
            "sadat", "nasser", "orabi", "al-shohadaa", "ghamra", "el-demerdash", "manshiet el-sadr ", "kobri el-qobba", "hammamat el-qobba", "saray el-qobba", "hadayeq el-zaitoun",
            "helmeyet el-zaitoun ", "el-matareyya", "ain shams", "ezbet el-nakhl", "el-marg", "new el-marg");

    List<String> line2 = Arrays.asList("el mounib", "sakiat mekki", "omm el misryeen", "giza", "faisal",
            "cairo university", "el-bohooth", "dokki", "opera", "sadat", "naguib", "attaba", "al-shohadaa", "massara",
            "road el-farag", "st.teresa", "khalafawy", "mezallat", "koliet el-zeraa", "shobra el kheima");

    List<String> line3 = Arrays.asList("airport", "ahmed galal", "adly mansour", "el haykestep", "omar ibn el-khattab",
            "qobaa", "hesham barakat", "el-nozha", "nadi el-shams", "alf maskan", "heliopolis square", "haroun",
            "al-ahram ", "koleyet el-banat ", "stadium", "fair zone", "abbassia", "abdou pasha",
            "el geish", "bab el shaaria ", "attaba", "nasser", "maspero", "safaa hegazy", "kit kat");

    List<String> branchLine3 = Arrays.asList("cairo university", "bulaq el-dakroor", "gamaat el dowal al-arabiya"
            , "wadi el-nil", "el-tawfikeya", "kit kat", "sudan street", "imbaba", "el-bohy", "al-qawmeya al-arabiya"
            , "ring road", "rod al-farag axis");

    private ActivityResultsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        String startStation = intent.getStringExtra("startStation");
        String arrivalStation = intent.getStringExtra("arrivalStation");

        if (startStation == null || arrivalStation == null) {
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
        } else {
            getData(startStation, arrivalStation);
        }
    }

    private void getData(String startStation, String arrivalStation) {

        if (line1.contains(startStation) && line1.contains(arrivalStation)) {
            sameLine(line1, startStation, arrivalStation);

        } else if (line2.contains(startStation) && line2.contains(arrivalStation)) {
            sameLine(line2, startStation, arrivalStation);

        } else if (line3.contains(startStation) && line3.contains(arrivalStation)) {
            sameLine(line3, startStation, arrivalStation);

        } else if (branchLine3.contains(startStation) && branchLine3.contains(arrivalStation)) {
            sameLine(branchLine3, startStation, arrivalStation);
        }
        // line1
        else if (line1.contains(startStation) && line2.contains(arrivalStation)) {

            getFirstWay(line1, line2, startStation, arrivalStation,
                    getIndex(line1, "al-shohadaa"), getIndex(line2, "al-shohadaa"));

        } else if (line1.contains(startStation) && line3.contains(arrivalStation)) {
            getFirstWay(line1, line3, startStation, arrivalStation,
                    getIndex(line2, "nasser"), getIndex(line3, "nasser"));

        } else if (line1.contains(startStation) && branchLine3.contains(arrivalStation)) {
            getSecWay(line1, branchLine3, startStation, arrivalStation, getIndex(line1, "nasser"),
                    getIndex(branchLine3, "kit kat"),
                    line3, getIndex(line3, "nasser"), getIndex(line3, "kit kat"));

        }
        //line2
        else if (line2.contains(startStation) && line1.contains(arrivalStation)) {
            getFirstWay(line2, line1, startStation, arrivalStation,
                    getIndex(line2, "al-shohadaa"), getIndex(line1, "al-shohadaa"));
        } else if (line2.contains(startStation) && line3.contains(arrivalStation)) {
            getFirstWay(line2, line3, startStation, arrivalStation,
                    getIndex(line2, "attaba"), getIndex(line3, "attaba"));

        } else if (line2.contains(startStation) && branchLine3.contains(arrivalStation)) {
            getFirstWay(line2, branchLine3, startStation, arrivalStation,
                    getIndex(line2, "cairo university"), getIndex(branchLine3, "cairo university"));

        }
        // line3
        else if (line3.contains(startStation) && line1.contains(arrivalStation)) {
            getFirstWay(line3, line1, startStation, arrivalStation,
                    getIndex(line2, "nasser"), getIndex(line3, "nasser"));
        } else if (line3.contains(startStation) && line2.contains(arrivalStation)) {
            getFirstWay(line3, line2, startStation, arrivalStation,
                    getIndex(line3, "attaba"), getIndex(line2, "attaba"));

        } else if (line3.contains(startStation) && branchLine3.contains(arrivalStation)) {
            getFirstWay(branchLine3, line3, startStation, arrivalStation,
                    getIndex(branchLine3, "kit kat"), getIndex(line3, "kit kat"));
        }
        // branchLine3
        else if (branchLine3.contains(startStation) && line1.contains(arrivalStation)) {
            getSecWay(branchLine3, line1, startStation, arrivalStation, getIndex(branchLine3, "kit kat"),
                    getIndex(line1, "nasser"),
                    line3, getIndex(line3, "kit kat"), getIndex(line3, "nasser"));

        } else if (branchLine3.contains(startStation) && line2.contains(arrivalStation)) {
            getFirstWay(branchLine3, line2, startStation, arrivalStation,
                    getIndex(branchLine3, "cairo university"), getIndex(line2, "cairo university"));

        } else if (branchLine3.contains(startStation) && line3.contains(arrivalStation)) {
            getFirstWay(branchLine3, line3, startStation, arrivalStation,
                    getIndex(branchLine3, "kit kat"), getIndex(line3, "kit kat"));
        }
    }

    @SuppressLint("SetTextI18n")
    public void sameLine(List<String> line, String start, String end) {

        byte startIndex = (byte) line.indexOf(start);
        byte arrivalIndex = (byte) line.indexOf(end);

        byte route = (byte) (startIndex - arrivalIndex);
        List<String> emptyList = List.of("");

        if ((route) < 0) {
            binding.direction.setText("Dircetion: " + line.get(line.size() - 1).toUpperCase());
            binding.route.setText("Your Route : " + line.subList(startIndex, arrivalIndex + 1));
        } else {
            binding.direction.setText("Dircetion: " + line.get(0).toUpperCase());
            List<String> subList = line.subList(arrivalIndex, startIndex + 1);
            Collections.reverse(subList);
            binding.route.setText("Your Route : " + subList);
        }
        calcTimeAndCost(route, emptyList);
    }


    @SuppressLint("SetTextI18n")
    public void getFirstWay(List<String> startLine, List<String> arrivalLine, String start, String end, byte
            indexOfSwitchedStation1, byte indexOfSwitchedStation2) {
        byte startIndex = (byte) startLine.indexOf(start);
        byte stage1 = (byte) (startIndex - indexOfSwitchedStation1);

        byte arrivalIndex = (byte) arrivalLine.indexOf(end);
        byte stage3 = (byte) (indexOfSwitchedStation2 - arrivalIndex);

        byte route1 = (byte) (Math.abs(stage1) + Math.abs(stage3));
        ArrayList<String> transitionsStations = new ArrayList<>();

        List<String> subList;

        String firstDirection = startLine.get(startLine.size() - 1).toUpperCase();
        String reversFirstDirection = startLine.get(0).toUpperCase();
        String secDirection = arrivalLine.get(arrivalLine.size() - 1).toUpperCase();
        String reversSecDirection = arrivalLine.get(0).toUpperCase();
        String nameSwitchStation = startLine.get(indexOfSwitchedStation1);

        if (stage1 < 0) {
            binding.direction.setText(" Direction: " + firstDirection);
            binding.route.setText(startLine.subList(startIndex, indexOfSwitchedStation1 + 1).toString());

        } else {
            binding.direction.setText(" Direction: " + reversFirstDirection);
            subList = startLine.subList(indexOfSwitchedStation1, startIndex + 1);
            Collections.reverse(subList);
            binding.route.setText(subList.toString());
        }
        if (stage3 < 0) {

            showViews((byte) 0);

            binding.switchStation.setText("Then you switch at " + nameSwitchStation + " station");
            transitionsStations.add(nameSwitchStation);
            binding.secDirection.setText("And Take : " + secDirection + " Direction");
            subList = arrivalLine.subList(indexOfSwitchedStation2, arrivalIndex + 1);
            binding.secRoute.setText(subList.toString());

        } else {

            showViews((byte) 0);

            binding.switchStation.setText("Then you switch at " + nameSwitchStation + " station");
            transitionsStations.add(nameSwitchStation);
            binding.secDirection.setText("And Take : " + reversSecDirection + " Direction");
            subList = arrivalLine.subList(arrivalIndex, indexOfSwitchedStation2 + 1);
            Collections.reverse(subList);
            binding.secRoute.setText(subList.toString());
        }
        calcTimeAndCost(route1, transitionsStations);
    }

    @SuppressLint("SetTextI18n")
    public void getSecWay(List<String> startLine, List<String> arrivalLine, String start, String end, byte
            indexOfSwitchedStation1, byte indexOfSwitchedStation2, List<String> lineLink, int indexOfLinkedStation1,
                          int indexOfLinkedStation2) {
        byte startIndex = (byte) startLine.indexOf(start);
        byte stage2 = (byte) (startIndex - indexOfSwitchedStation1);

        byte arrivalIndex = (byte) arrivalLine.indexOf(end);
        byte stage4 = (byte) (indexOfSwitchedStation2 - arrivalIndex);

        byte route2 = (byte) (Math.abs(stage2) + Math.abs(stage4));

        String firstDirection = startLine.get(startLine.size() - 1).toUpperCase();
        String reversFirstDirection = startLine.get(0).toUpperCase();
        String secDirection = arrivalLine.get(arrivalLine.size() - 1).toUpperCase();
        String reversSecDirection = arrivalLine.get(0).toUpperCase();
        String nameSwitchStation = startLine.get(indexOfSwitchedStation1);
        byte numOfLink = (byte) Math.abs(indexOfLinkedStation1 - indexOfLinkedStation2);

        List<String> subList;
        ArrayList<String> transitionsStations = new ArrayList<>();

        if (stage2 < 0) {
            binding.direction.setText(" Direction: " + firstDirection);
            binding.route.setText(startLine.subList(startIndex, indexOfSwitchedStation1 + 1).toString());
        } else {
            binding.direction.setText(" Direction: " + reversFirstDirection);
            subList = startLine.subList(indexOfSwitchedStation1, startIndex + 1);
            Collections.reverse(subList);
            binding.route.setText(subList.toString());
        }
        if (stage4 < 0) {
            showViews((byte) 1);
            binding.switchStation.setText("Then you switch at " + nameSwitchStation + " station");
            transitionsStations.add(nameSwitchStation);

            if (indexOfLinkedStation1 != -1) {
                getRouteLink(lineLink, (byte) indexOfLinkedStation1, (byte) indexOfLinkedStation2, transitionsStations);
                route2 += numOfLink;
            }

            binding.secDirection.setText("And Take : " + secDirection + " Direction");
            subList = arrivalLine.subList(indexOfSwitchedStation2, arrivalIndex + 1);
            binding.secRoute.setText(subList.toString());
        } else {
            showViews((byte) 1);
            binding.switchStation.setText("Then you switch at " + nameSwitchStation + " station");
            transitionsStations.add(nameSwitchStation);

            if (indexOfLinkedStation1 != -1) {
                getRouteLink(lineLink, (byte) indexOfLinkedStation1, (byte) indexOfLinkedStation2, transitionsStations);
                route2 += numOfLink;
            }

            binding.secDirection.setText("And Take : " + reversSecDirection + " Direction");
            subList = arrivalLine.subList(arrivalIndex, indexOfSwitchedStation2 + 1);
            Collections.reverse(subList);
            binding.secRoute.setText(subList.toString());
        }
        calcTimeAndCost(route2, transitionsStations);

    }

    @SuppressLint("SetTextI18n")
    public void getRouteLink(List<String> lineLink, byte indexOfLinkedStation1, byte indexOfLinkedStation2,
                             List<String> transitionsStations) {
        List<String> subList;
        byte indx;
        String name;
        byte stage = (byte) (indexOfLinkedStation1 - indexOfLinkedStation2);// sadat cu

        String dircetion = lineLink.get(0);
        String reversDircetion = lineLink.get(lineLink.size() - 1);

        if (stage < 0) {
            binding.linkDirection.setText("And take: " + reversDircetion + " Direction");
            subList = lineLink.subList(indexOfLinkedStation1, indexOfLinkedStation2 + 1);
            binding.linkRout.setText(subList.toString());

            indx = (byte) (subList.size() - 1);
            name = subList.get(indx);
            binding.linkSwitch.setText("Then you switch at " + name + " station");
            transitionsStations.add(name);

        } else {
            binding.linkDirection.setText("And take: " + dircetion + " Direction");
            subList = lineLink.subList(indexOfLinkedStation2, indexOfLinkedStation1 + 1);
            Collections.reverse(subList);
            binding.linkRout.setText(subList.toString());

            indx = (byte) (subList.size() - 1);
            name = subList.get(indx);
            binding.linkSwitch.setText("Then you switch at " + name + " station");
            transitionsStations.add(name);
        }
    }

    @SuppressLint({"SetTextI18n", "SuspiciousIndentation"})
    public void calcTimeAndCost(byte totalStations, List<String> transitionsStations) {
        byte timeMin, timeHour, reminingMin;
        byte numOfTransitionsStations = (byte) transitionsStations.size();

        //num of station
        binding.numStations.setText("Number of station: " + Math.abs(totalStations));

        //time
        if (transitionsStations.get(0).isEmpty()) {

            timeMin = (byte) (2 * Math.abs(totalStations));
            timeHour = (byte) (timeMin / 60);
            reminingMin = (byte) (timeMin % 60);
            if (timeHour >= 1)
                binding.time.setText("Time: " + timeHour + " Hours and " + reminingMin + " Min");
            else binding.time.setText("Time: " + timeMin + " Min");

        } else {

            binding.transitionsStations.setVisibility(View.VISIBLE);

            timeMin = (byte) (2 * Math.abs(totalStations) + (numOfTransitionsStations * 3));
            timeHour = (byte) (timeMin / 60);
            reminingMin = (byte) (timeMin % 60);
            if (timeHour >= 1)
                binding.time.setText("Time: " + timeHour + " Hours and " + reminingMin + " Min");
            else binding.time.setText("Time: " + timeMin + " Min");

//            StringBuilder ts = new StringBuilder();
//            for (String item : transitionsStations) {
//                ts.append(item).append("\n");
//            }

//            for (String transitionsStation : transitionsStations) {
//                binding.transitionsStations.setText("You will take in -> " + transitionsStation+ " 5 Min "+ "\n");
//            }
        }

        //cost
        if (totalStations <= 9) {
            binding.cost.setText("Ticket= " + 6 + " EGP");
        } else if (totalStations <= 16) {
            binding.cost.setText("Ticket= " + 8 + " EGP");
        } else if (totalStations <= 23) {
            binding.cost.setText("Ticket= " + 12 + " EGP");
        } else if (totalStations <= 39) {
            binding.cost.setText("Ticket= " + 15 + " EGP");
        }
    }

    private void showViews(byte status) {

        if (status == 0) {
            binding.switchStation.setVisibility(View.VISIBLE);
            binding.secDirection.setVisibility(View.VISIBLE);
            binding.secRoute.setVisibility(View.VISIBLE);
        } else {
            binding.switchStation.setVisibility(View.VISIBLE);
            binding.secDirection.setVisibility(View.VISIBLE);
            binding.secRoute.setVisibility(View.VISIBLE);
            binding.linkDirection.setVisibility(View.VISIBLE);
            binding.linkRout.setVisibility(View.VISIBLE);
            binding.linkSwitch.setVisibility(View.VISIBLE);
        }
    }

    private byte getIndex(List<String> line, String station) {
        return (byte) line.indexOf(station);
    }
}

// compare between routes
//transitions Stations