package com.tanmay.androidsupport.view.activities;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.location.GeocoderAPI;
import com.tanmay.androidsupport.location.PlacesAutoCompleteAdapter;
import com.tanmay.androidsupport.utils.ConstantClass;
import com.tanmay.androidsupport.view.interfaces.GeocoderListener;

import java.util.HashMap;
import java.util.Map;

public class SearchPlaces extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GeocoderListener {

    public static final String currentAddress = "CurrentAddress";
    public static final String locationCoordinates = "LocationCoordinates";
    public static String TAG = "SearchPlaces==>";
    AutoCompleteTextView autocompleteView;
    TextView selected, current;
    Toolbar toolbar;
    FloatingActionButton fab;
    Context context;
    String description;
    Location mLastLocation;
    GeocoderAPI geocoderAPI;
    GoogleApiClient mGoogleApiClient;
    Map<String, String> placeParams;

    @Override
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
        FlurryAgent.onStartSession(this, ConstantClass.MY_FLURRY_APIKEY);
        FlurryAgent.logEvent("#TNM--SearchPlaces");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .enableAutoManage(this, this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        setContentView(R.layout.activity_search_places);
        initView();
        context = this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Google Places API");

        placeParams = new HashMap<String, String>();
        geocoderAPI = new GeocoderAPI();
        GeocoderAPI.geocoderListener = SearchPlaces.this;

        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(context, R.layout.autocomplete_list_item));
        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get data associated with the specified position
                // in the list (AdapterView)
                description = (String) parent.getItemAtPosition(position);
                hideSoftKeyboard(SearchPlaces.this);
                geocoderAPI.getCoords(context, description, locationCoordinates);

                //param keys and values have to be of String type
                placeParams.put("Address", description);
                //up to 10 params can be logged with each event

                Answers.getInstance().logCustom(new CustomEvent("User #2203")
                        .putCustomAttribute("Address", description));

//                Answers.getInstance().logContentView(new ContentViewEvent()
//                        .putContentName("Address")
//                        .putContentType("Search Place")
//                        .putContentId("#2203")
//                        .putCustomAttribute("Address Lookup", description));

//                Crashlytics.setString("Address Lookup", description);
//                Crashlytics.logException(new Throwable("Event Log: Address Lookup"));
            }
        });

    }

    void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        selected = (TextView) findViewById(R.id.selected);
        current = (TextView) findViewById(R.id.current);
        autocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "On Connected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d(TAG, "Latitude: " + mLastLocation.getLatitude() + "Longitude: " + mLastLocation.getLongitude());
            geocoderAPI.getAddress(context, mLastLocation.getLatitude(), mLastLocation.getLongitude(), currentAddress);
        } else {
            Log.d(TAG, "Current Lcoation: " + "null");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "On Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "On Connection Failed");
    }

    @Override
    public void onAddressResponse(String label, String address) {
        if (label.equals(currentAddress)) {
            current.setText("Your Location: " + address
                    + "\n\nLatitude: " + mLastLocation.getLatitude()
                    + "\nLongitude: " + mLastLocation.getLongitude());
        } else {
            Toast.makeText(context, "Some error occurred!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCoordinateResponse(String label, Double lat, Double lng) {
        if (label.equals(locationCoordinates)) {
            selected.setText("Selected Location: " + description
                    + "\n\nLatitude: " + lat
                    + "\nLongitude: " + lng);
            placeParams.put("Coordinates", lat + ", " + lng);
            FlurryAgent.logEvent("#TNM--AddressLookup", placeParams);

            Answers.getInstance().logCustom(new CustomEvent("User #2204")
                    .putCustomAttribute("Coordinates", lat + ", " + lng));

//            Answers.getInstance().logContentView(new ContentViewEvent()
//                    .putContentName("Coordinates")
//                    .putContentType("Search Place")
//                    .putContentId("#2204")
//                    .putCustomAttribute("Coordinate Lookup", lat + ", " + lng));

//            Crashlytics.setString("Coordinates of Address", lat + ", " + lng);
//            Crashlytics.logException(new Throwable("Event Log: Location"));
        } else {
            Toast.makeText(context, "Some error occurred!", Toast.LENGTH_SHORT).show();
            placeParams.put("Coordinates", "Failed!");
            FlurryAgent.logEvent("#TNM--AddressLookup", placeParams);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }
}
