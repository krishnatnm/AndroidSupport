package com.tanmay.androidsupport.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TaNMay on 6/16/2016.
 */
public class LocationUpdateService extends Service implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static String TAG = "LocUpdateService =>=>";

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println(TAG + " On Create!");

        if (!isGooglePlayServicesAvailable()) {
            System.out.println(TAG + " Google Play Services not available");
            stopSelf();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println(TAG + " On Start Command!");
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        } else {
            startLocationUpdates();
        }
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println(TAG + " On Bind!");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println(TAG + " On Destroy!");
//        mGoogleApiClient.disconnect();
        stopLocationUpdates();
    }

    public void createLocationRequest() {
        System.out.println(TAG + " Create Location Request!");
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
        System.out.println(TAG + " Start Location Updates!");
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.
                requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        Location loc = getLocation();
        if (loc != null) {
            Log.d(TAG, "THE LOCATION - " + loc.getLatitude() + ", " + loc.getLongitude());
        } else {
            System.out.println(TAG + " NULL Location");
        }
    }

    public Location getLocation() {
        return mCurrentLocation;
    }

    private void updateLocationDetails() {
        if (mCurrentLocation != null) {
            Double lat = mCurrentLocation.getLatitude();
            Double lng = mCurrentLocation.getLongitude();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());

            System.out.println(TAG + " FINAL LOCATION -- " + "at " + currentDateandTime + " -- " +
                    "(" + lat + ", " + lng + ")" +
                    " Accuracy: " + mCurrentLocation.getAccuracy() +
                    " Provider: " + mCurrentLocation.getProvider());
        } else {
            System.out.println(TAG + " NULL location");
        }
    }


    public void stopLocationUpdates() {
        System.out.println(TAG + " Stop Location Updates!");
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println(TAG + " On Connected!");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println(TAG + " On Connection Suspended!");
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println(TAG + " On Location Changed!");
        System.out.println(TAG + " Changed location: " + location);

        mCurrentLocation = location;
        updateLocationDetails();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println(TAG + " On Connection Failed! " + connectionResult.toString());
    }
}
