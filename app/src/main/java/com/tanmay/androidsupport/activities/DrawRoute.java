package com.tanmay.androidsupport.activities;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.dialogs.UtilityDialogs;
import com.tanmay.androidsupport.interfaces.OnUtilityDialogButtonClickListener;
import com.tanmay.androidsupport.location.DirectionsAPI;
import com.tanmay.androidsupport.location.DirectionsJsonParser;
import com.tanmay.androidsupport.location.GeocoderAPI;
import com.tanmay.androidsupport.location.GeocoderListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrawRoute extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback,
        LocationListener,
        OnUtilityDialogButtonClickListener,
        GeocoderListener {

    public static final String originLocation = "OriginLocation";
    public static final String destLocation = "DestinationLocation";
    public static String TAG = "Draw Route ==>";
    Context context;
    Toolbar toolbar;
    FloatingActionButton fab;
    SupportMapFragment mapFragment;
    GoogleMap googleMap;
    Location loc;
    LocationManager locationManager;
    Boolean isGPSEnabled;
    Boolean isNetworkEnabled;
    LatLng latLng, sLatLng, eLatLng;
    Double latitude, longitude;
    Handler handler;
    UtilityDialogs utilityDialogs;
    GeocoderAPI geocoderAPI;
    DirectionsAPI directionsAPI;
    int gotBoth = 0;
    Boolean isVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_route);
        isVisible = true;
        initView();
        context = this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Google Directions API");

        mapFragment.getMapAsync(this);
        directionsAPI = new DirectionsAPI();
        utilityDialogs = new UtilityDialogs(context, this);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (isVisible)
                    utilityDialogs.toDrawRoute();
                else
                    Log.d(TAG, "Alert Dialog Stopped!");
            }
        }, 2000);
        geocoderAPI = new GeocoderAPI();
        GeocoderAPI.geocoderListener = DrawRoute.this;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilityDialogs.toDrawRoute();
            }
        });
    }

    void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isVisible = false;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        setUpMap();
    }

    public void setUpMap() {
        //set this as map loaded callback
        googleMap.setOnMapLoadedCallback(this);
        googleMap.setMyLocationEnabled(true);
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
        loc = getLocation();
        if (loc != null) {
            latitude = loc.getLatitude();
            longitude = loc.getLongitude();
            latLng = new LatLng(latitude, longitude);

            if (gotBoth == 2 && sLatLng != null && eLatLng != null) {
                getRoute(sLatLng, eLatLng);
            } else {
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            }
        }
    }

    public Location getLocation() {
        Location location = null;
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            120000,
                            10, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                120000,
                                10, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latLng = new LatLng(latitude, longitude);
        if (gotBoth == 2 && sLatLng != null && eLatLng != null) {
            getRoute(sLatLng, eLatLng);
        } else {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onAcceptClick(String descOrigin, String descDest) {
        Log.d(TAG, "On Accept Click");
        gotBoth = 0;
        Log.d(TAG, "OnAcceptClick (Got Both): " + gotBoth);

        geocoderAPI.getCoords(context, descOrigin, originLocation);
        geocoderAPI.getCoords(context, descDest, destLocation);

        googleMap.clear();
    }

    @Override
    public void onAcceptClick(String title, File fileName) {

    }

    public void getRoute(LatLng pLatLng, LatLng dLatLng) {
        LatLng origin = pLatLng;
        LatLng dest = dLatLng;

        Log.d(TAG, "GetRoute: " + pLatLng + ", " + dLatLng);

        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(pLatLng).title("Origin"));
        googleMap.addMarker(new MarkerOptions().position(dLatLng).title("Destination"));

        List<Marker> markers = new ArrayList<Marker>();
        markers.add(googleMap.addMarker(new MarkerOptions().position(origin).title("Origin")));
        markers.add(googleMap.addMarker(new MarkerOptions().position(dest).title("Destination")));

        //Calculate the markers to get their position
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        for (Marker m : markers) {
            b.include(m.getPosition());
        }
        LatLngBounds bounds = b.build();
        //Change the padding as per needed
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 500, 500, 10);
        googleMap.animateCamera(cu);

        // Getting URL to the Google Directions API
        String url = directionsAPI.getDirectionsUrl(origin, dest);
        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception-downloadinUrl", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onAddressResponse(String label, String address) {

    }

    @Override
    public void onCoordinateResponse(String label, Double lat, Double lng) {
        if (label.equals(originLocation)) {
            Log.d(TAG, "On Coordinate Response: " + label);
            gotBoth++;
            sLatLng = new LatLng(lat, lng);
            Log.d(TAG, "sLatLng: " + sLatLng);
            if (gotBoth == 2 && sLatLng != null && eLatLng != null) {
                Log.d(TAG, "originLocation (Got Both): " + gotBoth);
                getRoute(sLatLng, eLatLng);
            }
        } else if (label.equals(destLocation)) {
            Log.d(TAG, "On Coordinate Response: " + label);
            gotBoth++;
            eLatLng = new LatLng(lat, lng);
            Log.d(TAG, "eLatLng: " + eLatLng);
            if (gotBoth == 2 && sLatLng != null && eLatLng != null) {
                Log.d(TAG, "destLocation (Got Both): " + gotBoth);
                getRoute(sLatLng, eLatLng);
            }
        } else {

            Toast.makeText(context, "Some error occurred!", Toast.LENGTH_SHORT).show();
        }
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {
        // Fetches data from url passed
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // A class to parse the Google Places in JSON format
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJsonParser parser = new DirectionsJsonParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(R.color.colorAccentDark);
            }
            // Drawing polyline in the Google Map for the i-th route
            googleMap.addPolyline(lineOptions);
        }
    }
}