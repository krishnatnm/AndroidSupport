package com.tanmay.androidsupport.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.services.LocationUpdateService;

import java.util.Calendar;

public class LocationUpdate extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static String TAG = "LocationUpdate ==>";

    Toolbar toolbar;

    Context context;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_update);
        initView();
        context = this;
        setSupportActionBar(toolbar);

        if (!isGooglePlayServicesAvailable()) {
            Toast.makeText(context, "Google Play Services not available!", Toast.LENGTH_SHORT).show();
            finish();
        }

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        } else {
            startLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            System.out.println(TAG + " " + status);
            return false;
        }
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.
                requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public Location getLocation() {
        return mCurrentLocation;
    }

    public void onGetLocationClick(View view) {
        Location loc = mCurrentLocation;
        if (loc != null) {
            String locationStr = "Location Coordinates: " + mCurrentLocation.getLatitude()
                    + ", " + mCurrentLocation.getLongitude();
            Toast.makeText(context, locationStr, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Location Not Available right now! Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onServiceStartClick(View view) {
        getLocationService();
    }

    public void onServiceStopClick(View view) {
        Intent i = new Intent(context, LocationUpdateService.class);
        stopService(i);
    }

    public void getLocationService() {
        long frequency = 1 * 60 * 1000;                                                             // 1 min
        Calendar cal = Calendar.getInstance();
        Intent mServiceIntent = new Intent(context, LocationUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 1, mServiceIntent, 0);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), frequency, pendingIntent);
    }

    public void stopLocationUpdates() {
        System.out.println(TAG + " Stop Location Updates!");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
