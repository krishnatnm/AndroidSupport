package com.tanmay.androidsupport.location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by TaNMay on 3/8/2016.
 */
public class DirectionsAPI {

    public DirectionsAPI() {

    }

    public String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Waypoints
        String waypoints = "waypoints=";
        waypoints += origin.latitude + "," + origin.longitude + "|";
        waypoints += dest.latitude + "," + dest.longitude + "|";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

}
