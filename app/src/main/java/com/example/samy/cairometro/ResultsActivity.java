package com.example.samy.cairometro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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

public class ResultsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    List<String> line1 = Arrays.asList("helwan", "ainhelwan", "helwan university", "wadi hof", "hadayeq helwan",
            "el-maasara", "tora el-asmant", "kozzika", "tora el-balad", "sakanat el-maadi", "maadi", "hadayeq el-maadi",
            "dar el-salam", "el-zahraa", "mar girgis", "el-malek el-saleh", "alSayyeda zeinab", "saad zaghloul",
            "sadat", "nasser", "orabi", "al-shohadaa", "ghamra", "el-demerdash", "manshiet el-sadr ", "kobri el-qobba", "hammamat el-qobba", "saray el-qobba", "hadayeq el-zaitoun",
            "helmeyet el-zaitoun ", "el-matareyya", "ain shams", "ezbet el-nakhl", "el-marg", "new el-marg");

    List<String> line2 = Arrays.asList("el mounib", "sakiat mekki", "omm el misryeen", "giza", "faisal",
            "cairo university", "el-bohooth", "dokki", "opera", "sadat", "naguib", "attaba", "al-shohadaa", "massara",
            "road el-farag", "st.teresa", "khalafawy", "mezallat", "koliet el-zeraa", "shobra el kheima");

    //"airport", "ahmed galal",  this stations under construction
    List<String> line3 = Arrays.asList("adly mansour", "el haykestep", "omar ibn el-khattab",
            "qobaa", "hesham barakat", "el-nozha", "nadi el-shams", "alf maskan", "heliopolis square", "haroun",
            "al-ahram ", "koleyet el-banat ", "stadium", "fair zone", "abbassia", "abdou pasha",
            "el geish", "bab el shaaria ", "attaba", "nasser", "maspero", "safaa hegazy", "kit kat");

    List<String> branchLine3 = Arrays.asList("cairo university", "bulaq el-dakroor", "gamaat el dowal al-arabiya"
            , "wadi el-nil", "el-tawfikeya", "kit kat", "sudan street", "imbaba", "el-bohy", "al-qawmeya al-arabiya"
            , "ring road", "rod al-farag axis");

    private ActivityResultsBinding binding;
    private final ArrayList<String> emptyList = new ArrayList<>();
    List<String> testList= new ArrayList<>();
    TextToSpeech tts;

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

        tts = new TextToSpeech(this,this);

        Intent intent = getIntent();

        String startStation = intent.getStringExtra("startStation");
        String arrivalStation = intent.getStringExtra("arrivalStation");

        if (startStation == null || arrivalStation == null) {
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
        } else {
            getFirstLine(startStation, arrivalStation);
        }
    }

    private void getFirstLine(String startStation, String arrivalStation) {

        if (line1.contains(startStation)) {
            getLine1(startStation,arrivalStation);

        } else if (line2.contains(startStation)) {
            getLine2(startStation,arrivalStation);

        } else if (line3.contains(startStation)) {
            getLine3(startStation, arrivalStation);

        } else if (branchLine3.contains(startStation)) {
            getBranchLine3(startStation, arrivalStation);
        }
    }

    // line1
    private void getLine1(String startStation,String arrivalStation) {
        if (line1.contains(arrivalStation)){
            sameLine(line1, startStation, arrivalStation);
        }
        else if (line2.contains(arrivalStation)) {

            getFirstWay(line1, line2, startStation, arrivalStation,
                    getIndex(line1, "sadat"), getIndex(line2, "sadat"), (byte) 1);

            getFirstWay(line1, line2, startStation, arrivalStation,
                    getIndex(line1, "al-shohadaa"), getIndex(line2, "al-shohadaa"), (byte) -2);

        }
        else if (line3.contains(arrivalStation)) {

            getFirstWay(line1, line3, startStation, arrivalStation,
                    getIndex(line1, "nasser"), getIndex(line3, "nasser"), (byte) 1);

            getSecWay(line1, line3, startStation, arrivalStation, getIndex(line1, "sadat"),
                    getIndex(line3, "attaba"), line2, getIndex(line2, "sadat"),
                    getIndex(line2, "attaba"), (byte) -1);
        }
        else if (branchLine3.contains(arrivalStation)) {

            getSecWay(line1, branchLine3, startStation, arrivalStation, getIndex(line1, "nasser"),
                    getIndex(branchLine3, "kit kat"),
                    line3, getIndex(line3, "nasser"), getIndex(line3, "kit kat"), (byte) 0);

            getSecWay(line1, branchLine3, startStation, arrivalStation, getIndex(line1, "sadat"),
                    getIndex(branchLine3, "cairo university"),
                    line2, getIndex(line2, "sadat"), getIndex(line2, "cairo university"), (byte) -1);
        }
    }
    //line2
    private void getLine2(String startStation,String arrivalStation) {
        if (line2.contains(arrivalStation)){
            sameLine(line2, startStation, arrivalStation);
        }
        else if (line2.contains(startStation) && line1.contains(arrivalStation)) {
            getFirstWay(line2, line1, startStation, arrivalStation,
                    getIndex(line2, "al-shohadaa"), getIndex(line1, "al-shohadaa"), (byte) 1);

            getFirstWay(line2, line1, startStation, arrivalStation,
                    getIndex(line2, "sadat"), getIndex(line1, "sadat"), (byte) -2);


        }
        else if (line2.contains(startStation) && line3.contains(arrivalStation)) {
            getFirstWay(line2, line3, startStation, arrivalStation,
                    getIndex(line2, "attaba"), getIndex(line3, "attaba"), (byte) 1);

            getSecWay(line2, line3, startStation, arrivalStation, getIndex(line2, "sadat"),
                    getIndex(line3, "nasser"),
                    line1, getIndex(line1, "sadat"), getIndex(line1, "nasser"), (byte) -1);
        }
        else if (line2.contains(startStation) && branchLine3.contains(arrivalStation)) {
            getFirstWay(line2, branchLine3, startStation, arrivalStation,
                    getIndex(line2, "cairo university"),
                    getIndex(branchLine3, "cairo university"), (byte) 1);

            getSecWay(line2, branchLine3, startStation, arrivalStation, getIndex(line2, "attaba"),
                    getIndex(branchLine3, "kit kat"),
                    line3, getIndex(line3, "attaba"), getIndex(line3, "kit kat"), (byte) -1);
        }
    }
    // line3
    private void getLine3(String startStation,String arrivalStation){
        if (line3.contains(arrivalStation)){
            sameLine(line3, startStation, arrivalStation);
        }
        else if (line3.contains(startStation) && line1.contains(arrivalStation)) {
            getFirstWay(line3, line1, startStation, arrivalStation,
                    getIndex(line3, "nasser"), getIndex(line1, "nasser"), (byte) 1);

            getSecWay(line3, line1, startStation, arrivalStation, getIndex(line3, "attaba"),
                    getIndex(line1, "al-shohadaa"),
                    line2, getIndex(line2, "attaba"), getIndex(line2, "al-shohadaa"), (byte) -1);
        }
        //3 cases
        else if (line3.contains(startStation) && line2.contains(arrivalStation)) {
            getFirstWay(line3, line2, startStation, arrivalStation,
                    getIndex(line3, "attaba"), getIndex(line2, "attaba"), (byte) 1);

            getSecWay(line3, line2, startStation, arrivalStation, getIndex(line3, "nasser"),
                    getIndex(line2, "sadat"),
                    line1, getIndex(line1, "nasser"), getIndex(line1, "sadat"), (byte) -1);
        }
        // 3 cases
        else if (line3.contains(startStation) && branchLine3.contains(arrivalStation)) {
            getFirstWay(line3, branchLine3, startStation, arrivalStation,
                    getIndex(line3, "kit kat"), getIndex(branchLine3, "kit kat"), (byte) 1);

            getSecWay(line3, branchLine3, startStation, arrivalStation, getIndex(line3, "attaba"),
                    getIndex(branchLine3, "cairo university"),
                    line2, getIndex(line2, "attaba"), getIndex(line2, "cairo university"), (byte) -1);
        }

    }
    // branchLine3
    private void getBranchLine3(String startStation,String arrivalStation){
        if (branchLine3.contains(arrivalStation)){
            sameLine(branchLine3, startStation, arrivalStation);
        }
        else if (branchLine3.contains(startStation) && line1.contains(arrivalStation)) {
            getSecWay(branchLine3, line1, startStation, arrivalStation, getIndex(branchLine3, "kit kat"),
                    getIndex(line1, "nasser"),
                    line3, getIndex(line3, "kit kat"), getIndex(line3, "nasser"), (byte) 1);

            getSecWay(branchLine3, line1, startStation, arrivalStation, getIndex(branchLine3, "cairo university"),
                    getIndex(line1, "sadat"),
                    line2, getIndex(line2, "cairo university"), getIndex(line2, "sadat"), (byte) -1);
        }
        else if (branchLine3.contains(startStation) && line2.contains(arrivalStation)) {
            getFirstWay(branchLine3, line2, startStation, arrivalStation,
                    getIndex(branchLine3, "cairo university"), getIndex(line2, "cairo university"), (byte) 1);

            getSecWay(branchLine3, line2, startStation, arrivalStation, getIndex(branchLine3, "kit kat"),
                    getIndex(line2, "attaba"),
                    line3, getIndex(line3, "kit kat"), getIndex(line3, "attaba"), (byte) -1);
        }
        // 3 cases
        else if (branchLine3.contains(startStation) && line3.contains(arrivalStation)) {
            getFirstWay(branchLine3, line3, startStation, arrivalStation,
                    getIndex(branchLine3, "kit kat"), getIndex(line3, "kit kat"), (byte) 1);

            // bug in sec take direction
            getSecWay(branchLine3, line3, startStation, arrivalStation, getIndex(branchLine3, "cairo university"),
                    getIndex(line3, "attaba"),
                    line2, getIndex(line2, "cairo university"), getIndex(line2, "attaba"), (byte) -1);

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
            testList.addAll(subList);
            Collections.reverse(testList);
            binding.route.setText("Your Route : " + testList);
            testList.clear();
        }
        calcTimeAndCost(route, emptyList, (byte) 1);
    }

    @SuppressLint("SetTextI18n")
    public void getFirstWay(List<String> startLine, List<String> arrivalLine, String start, String end, byte
            indexOfSwitchedStation1, byte indexOfSwitchedStation2, byte statusView)  {
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

        if (statusView == 1) {
            if (stage1 < 0) {
                binding.direction.setText(" Direction: " + firstDirection);
                subList = startLine.subList(startIndex, indexOfSwitchedStation1 + 1);
                binding.route.setText(subList.toString());

            } else {
                binding.direction.setText(" Direction: " + reversFirstDirection);
                subList = startLine.subList(indexOfSwitchedStation1, startIndex + 1);
                testList.addAll(subList);
                Collections.reverse(testList);
                binding.route.setText(testList.toString());
                testList.clear();
            }
            if (stage3 < 0) {

                showViews(statusView); //0
                binding.switchStation.setText("Then you switch at " + nameSwitchStation + " station");
//                speak(nameSwitchStation);
                tts.speak(nameSwitchStation,TextToSpeech.QUEUE_ADD,null,null);
                transitionsStations.add(nameSwitchStation);
                binding.secDirection.setText("And Take : " + secDirection + " Direction");
                subList = arrivalLine.subList(indexOfSwitchedStation2, arrivalIndex + 1);
                binding.secRoute.setText(subList.toString());

            } else {

                showViews(statusView); //0
                binding.switchStation.setText("Then you switch at " + nameSwitchStation + " station");
//                speak(nameSwitchStation);
                tts.speak(nameSwitchStation,TextToSpeech.QUEUE_ADD,null,null);
                transitionsStations.add(nameSwitchStation);
                binding.secDirection.setText("And Take : " + reversSecDirection + " Direction");
                subList = arrivalLine.subList(arrivalIndex, indexOfSwitchedStation2 + 1);
                testList.addAll(subList);
                Collections.reverse(testList);
                binding.secRoute.setText(testList.toString());
                testList.clear();
            }
        }
        else if (statusView == -2) {
            showViews((byte) -2);
            if (stage1 < 0) {
                binding.direction2.setText(" Direction: " + firstDirection);
                subList = startLine.subList(startIndex, indexOfSwitchedStation1 + 1);
                binding.route2.setText(subList.toString());

            } else {
                binding.direction2.setText(" Direction: " + reversFirstDirection);
                subList = startLine.subList(indexOfSwitchedStation1, startIndex + 1);
                testList.addAll(subList);
                Collections.reverse(testList);
                binding.route2.setText(testList.toString());
                testList.clear();
            }
            if (stage3 < 0) {

                showViews(statusView);
                binding.switchStation2.setText("Then you switch at " + nameSwitchStation + " station");
//                speak(nameSwitchStation);
                transitionsStations.add(nameSwitchStation);
                binding.secDirection2.setText("And Take : " + secDirection + " Direction");
                subList = arrivalLine.subList(indexOfSwitchedStation2, arrivalIndex + 1);
                binding.secRoute2.setText(subList.toString());

            } else {

                showViews(statusView);
                binding.switchStation2.setText("Then you switch at " + nameSwitchStation + " station");
//                speak(nameSwitchStation);
                transitionsStations.add(nameSwitchStation);
                binding.secDirection2.setText("And Take : " + reversSecDirection + " Direction");
                subList = arrivalLine.subList(arrivalIndex, indexOfSwitchedStation2 + 1);
                testList.addAll(subList);
                Collections.reverse(testList);
                binding.secRoute2.setText(testList.toString());
                testList.clear();
            }
        }
        calcTimeAndCost(route1, transitionsStations, statusView);
    }

    @SuppressLint("SetTextI18n")
    public void getSecWay(List<String> startLine, List<String> arrivalLine, String start, String end, byte
            indexOfSwitchedStation1, byte indexOfSwitchedStation2, List<String> lineLink, int indexOfLinkedStation1,
                          int indexOfLinkedStation2, byte statusView) {
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

        if (statusView == -1) {
            showViews((byte) -1);
            if (stage2 < 0) {
                binding.direction2.setText(" Direction: " + firstDirection);
                binding.route2.setText(startLine.subList(startIndex, indexOfSwitchedStation1 + 1).toString());
            } else {
                binding.direction2.setText(" Direction: " + reversFirstDirection);
                subList = startLine.subList(indexOfSwitchedStation1, startIndex + 1);
                testList.addAll(subList);
                Collections.reverse(testList);
                binding.route2.setText(testList.toString());
                testList.clear();
            }
            if (stage4 < 0) {
                binding.switchStation2.setText("Then you switch at " + nameSwitchStation + " station");
//                speak(nameSwitchStation);
                transitionsStations.add(nameSwitchStation);

                if (indexOfLinkedStation1 != -1) {
                    getRouteLink(lineLink, (byte) indexOfLinkedStation1, (byte) indexOfLinkedStation2, transitionsStations, statusView);
                    route2 += numOfLink;
                }
                binding.secDirection2.setVisibility(View.VISIBLE);
                binding.secDirection2.setText("And Take : " + secDirection.toLowerCase() + " Direction");
                subList = arrivalLine.subList(indexOfSwitchedStation2, arrivalIndex + 1);
                binding.secRoute2.setText(subList.toString());
            } else {
                binding.switchStation2.setText("Then you switch at " + nameSwitchStation + " station");
//                speak(nameSwitchStation);
                transitionsStations.add(nameSwitchStation);

                if (indexOfLinkedStation1 != -1) {
                    getRouteLink(lineLink, (byte) indexOfLinkedStation1, (byte) indexOfLinkedStation2, transitionsStations, statusView);
                    route2 += numOfLink;
                }
                binding.secDirection2.setText("And Take : " + reversSecDirection + " Direction");
                subList = arrivalLine.subList(arrivalIndex, indexOfSwitchedStation2 + 1);
                testList.addAll(subList);
                Collections.reverse(testList);
                binding.secRoute2.setText(testList.toString());
                testList.clear();
            }
        }
        else {
            if (stage2 < 0) {
                binding.direction.setText(" Direction: " + firstDirection);
                binding.route.setText(startLine.subList(startIndex, indexOfSwitchedStation1 + 1).toString());
            } else {
                binding.direction.setText(" Direction: " + reversFirstDirection);
                subList = startLine.subList(indexOfSwitchedStation1, startIndex + 1);
                testList.addAll(subList);
                Collections.reverse(testList);
                binding.route.setText(testList.toString());
                testList.clear();
            }
            if (stage4 < 0) {
                binding.switchStation.setText("Then you switch at " + nameSwitchStation + " station");
//                speak(nameSwitchStation);
                transitionsStations.add(nameSwitchStation);

                if (indexOfLinkedStation1 != -1) {
                    showViews(statusView);//1
                    getRouteLink(lineLink, (byte) indexOfLinkedStation1, (byte) indexOfLinkedStation2, transitionsStations, statusView);
                    route2 += numOfLink;
                }
                binding.secDirection.setText("And Take : " + secDirection + " Direction");
                subList = arrivalLine.subList(indexOfSwitchedStation2, arrivalIndex + 1);
                binding.secRoute.setText(subList.toString());
            } else {
                binding.switchStation.setText("Then you switch at " + nameSwitchStation + " station");
//                speak(nameSwitchStation);
                transitionsStations.add(nameSwitchStation);

                if (indexOfLinkedStation1 != -1) {
                    showViews(statusView);//1
                    getRouteLink(lineLink, (byte) indexOfLinkedStation1, (byte) indexOfLinkedStation2, transitionsStations, statusView);
                    route2 += numOfLink;
                }
                binding.secDirection.setText("And Take : " + reversSecDirection + " Direction");
                subList = arrivalLine.subList(arrivalIndex, indexOfSwitchedStation2 + 1);
                testList.addAll(subList);
                Collections.reverse(testList);
                binding.secRoute.setText(testList.toString());
                testList.clear();
            }
        }

        calcTimeAndCost(route2, transitionsStations, statusView);
    }

    @SuppressLint("SetTextI18n")
    public void getRouteLink(List<String> lineLink, byte indexOfLinkedStation1, byte indexOfLinkedStation2,
                             List<String> transitionsStations, byte statusView) {
        List<String> subList;
        byte indx;
        String name;
        byte stage = (byte) (indexOfLinkedStation1 - indexOfLinkedStation2);// sadat cu

        String dircetion = lineLink.get(0);
        String reversDircetion = lineLink.get(lineLink.size() - 1);

        if (statusView == 1 || statusView ==0) {
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
                testList.addAll(subList);
                Collections.reverse(testList);
                binding.linkRout.setText(testList.toString());
                testList.clear();

                indx = (byte) (subList.size() - 1);
                name = subList.get(indx);
                binding.linkSwitch.setText("Then you switch at " + name + " station");
                transitionsStations.add(name);
            }
        } else { //-1
            if (stage < 0) {
                binding.linkDirection2.setText("And take: " + reversDircetion + " Direction");
                subList = lineLink.subList(indexOfLinkedStation1, indexOfLinkedStation2 + 1);
                binding.linkRout2.setText(subList.toString());

                indx = (byte) (subList.size() - 1);
                name = subList.get(indx);
                binding.linkSwitch2.setText("Then you switch at " + name + " station");
                transitionsStations.add(name);

            } else {
                binding.linkDirection2.setText("And take: " + dircetion + " Direction");
                subList = lineLink.subList(indexOfLinkedStation2, indexOfLinkedStation1 + 1);
                testList.addAll(subList);
                Collections.reverse(testList);
                binding.linkRout2.setText(testList.toString());
                testList.clear();

                indx = (byte) (subList.size() - 1);
                name = subList.get(indx);
                binding.linkSwitch2.setText("Then you switch at " + name + " station");
                transitionsStations.add(name);
            }
        }
    }

    @SuppressLint({"SetTextI18n", "SuspiciousIndentation"})
    public void calcTimeAndCost(byte totalStations, List<String> transitionsStations, byte statusView) {

        byte timeMin, timeHour, reminingMin;
        byte numOfTransitionsStations = (byte) transitionsStations.size();

        if (statusView == 1 || statusView == 0) {

            //num of station
            binding.numStations.setText("Number of station: " + Math.abs(totalStations));

            //time
            timeMin = (byte) (2 * Math.abs(totalStations));
            timeHour = (byte) (timeMin / 60);
            reminingMin = (byte) (timeMin % 60);
            if (timeHour >= 1)
                binding.time.setText("Time: " + timeHour + " Hours and " + reminingMin + " Min");
            else binding.time.setText("Time: " + timeMin + " Min");

            // cost
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
        else {

            //num of station
            binding.numStations2.setVisibility(View.VISIBLE);
            binding.numStations2.setText("Number of station: " + Math.abs(totalStations));

            //time
            binding.time2.setVisibility(View.VISIBLE);
            timeMin = (byte) (2 * Math.abs(totalStations));
            timeHour = (byte) (timeMin / 60);
            reminingMin = (byte) (timeMin % 60);
            if (timeHour >= 1) {
                binding.time2.setText("Time: " + timeHour + " Hours and " + reminingMin + " Min");
            } else binding.time2.setText("Time: " + timeMin + " Min");

            //cost
            binding.cost2.setVisibility(View.VISIBLE);
            if (totalStations <= 9) {
                binding.cost2.setText("Ticket= " + 6 + " EGP");
            } else if (totalStations <= 16) {
                binding.cost2.setText("Ticket= " + 8 + " EGP");
            } else if (totalStations <= 23) {
                binding.cost2.setText("Ticket= " + 12 + " EGP");
            } else if (totalStations <= 39) {
                binding.cost2.setText("Ticket= " + 15 + " EGP");
            }
        }

////            for (String transitionsStation : transitionsStations) {
////                binding.transitionsStations.setText("You will take in -> " + transitionsStation+ " 5 Min "+ "\n");
////            }
//        }
    }

    private void showViews(byte statusView) {

        if (statusView == 1) {
            binding.switchStation.setVisibility(View.VISIBLE);
            binding.secDirection.setVisibility(View.VISIBLE);
            binding.secRoute.setVisibility(View.VISIBLE);

        } else if (statusView == 0) {
            binding.switchStation.setVisibility(View.VISIBLE);
            binding.secDirection.setVisibility(View.VISIBLE);
            binding.secRoute.setVisibility(View.VISIBLE);
            binding.linkSwitch.setVisibility(View.VISIBLE);
            binding.linkRout.setVisibility(View.VISIBLE);
            binding.linkDirection.setVisibility(View.VISIBLE);
        } else if (statusView == -1) {
            binding.direction2.setVisibility(View.VISIBLE);
            binding.route2.setVisibility(View.VISIBLE);
            binding.switchStation2.setVisibility(View.VISIBLE);

            binding.linkDirection2.setVisibility(View.VISIBLE);
            binding.linkRout2.setVisibility(View.VISIBLE);
            binding.linkSwitch2.setVisibility(View.VISIBLE);

            binding.secDirection2.setVisibility(View.VISIBLE);
            binding.secRoute2.setVisibility(View.VISIBLE);

        } else if (statusView == -2) {
            binding.direction2.setVisibility(View.VISIBLE);
            binding.route2.setVisibility(View.VISIBLE);
            binding.switchStation2.setVisibility(View.VISIBLE);
            binding.secDirection2.setVisibility(View.VISIBLE);
            binding.secRoute2.setVisibility(View.VISIBLE);
        }
    }

    private byte getIndex(List<String> line, String station) {
        return (byte) line.indexOf(station);
    }

//    private void speak(String switchedStation){
//        tts.speak(switchedStation,TextToSpeech.QUEUE_FLUSH,null,null);
//    }
    
    @Override
    public void onInit(int i) {

    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}

// compare between routes
//transitions Stations
//cost