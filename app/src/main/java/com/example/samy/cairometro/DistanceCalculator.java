package com.example.samy.cairometro;

public class DistanceCalculator {
    private static final double EARTH_RADIUS = 6371; // Radius of the Earth in kilometers

    public static double calculateDistance(double myLat, double myLon, double lat2, double lon2) {
        // Convert latitude and longitude from degrees to radians
        double myLatRad = Math.toRadians(myLat);
        double myLonRad = Math.toRadians(myLon);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Haversine formula
        double dLat = lat2Rad - myLatRad;
        double dLon = lon2Rad - myLonRad;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(myLatRad) * Math.cos(lat2Rad) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = (EARTH_RADIUS * c);

        return distance;
    }
}
