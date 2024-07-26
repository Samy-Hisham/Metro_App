package com.example.samy.cairometro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class InfoStation implements Serializable {

    String name;
    double lat;
    double log;

    public InfoStation() {
    }

    public InfoStation(String name, double lat, double log) {
        this.name = name;
        this.lat = lat;
        this.log = log;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public String convertToString(){
        return  name + "," + lat + "," + log;
    }

    public static InfoStation fromString(String str){

        String[] parts = str.split(",");
        return new InfoStation(parts[0], Double.parseDouble(parts[1]),Double.parseDouble(parts[2]));
    }

}
