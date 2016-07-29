package com.tanmay.androidsupport.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.permissions.PermissionUtil;
import com.tanmay.androidsupport.utils.ConstantClass;
import com.tanmay.androidsupport.adapter.LandingAdapter;
import com.tanmay.androidsupport.interfaces.OnLandingItemClickListener;

import java.util.ArrayList;

public class GoogleHome extends AppCompatActivity implements OnLandingItemClickListener {

    private static final int REQUEST_LOCATION = 1;
    public static String TAG = "GoogleHome ==>";
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    Context context;
    ArrayList<String> listItems = new ArrayList<>();

    RecyclerView mRecyclerView;
    Toolbar toolbar;
    RelativeLayout entireLayout;

    @Override
    protected void onStart() {
        super.onStart();
        // init Flurry
        FlurryAgent.init(this, ConstantClass.MY_FLURRY_APIKEY);
        FlurryAgent.onStartSession(this, ConstantClass.MY_FLURRY_APIKEY);
        FlurryAgent.logEvent("#TNM--GoogleHome");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        initView();

        context = this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Google APIs");
        FlurryAgent.logEvent("#TNM--GoogleHome_onCreate");

        listItems.add("Google Places API Autocomplete View");
        listItems.add("Google Directions API");
        listItems.add("Google Fit API");
        listItems.add("Location Update Service");

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LandingAdapter(context, listItems);
        mRecyclerView.setAdapter(mAdapter);
        LandingAdapter.click = GoogleHome.this;
    }

    private void requestLocationPermissions() {
        boolean requestPermission = PermissionUtil.requestLocationPermissions(this);
        if (requestPermission == true) {
            if (checkSelfPermission(PERMISSIONS_LOCATION[0]) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSIONS_LOCATION[1]) == PackageManager.PERMISSION_GRANTED) {
                startNextActivity();
            } else {
                // Display a SnackBar with an explanation and a button to trigger the request.
                ActivityCompat.requestPermissions(GoogleHome.this, PERMISSIONS_LOCATION, REQUEST_LOCATION);
            }
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            Log.i(TAG, "Received response for permissions request.");
            // We have requested multiple permissions for location, so all of them need to be checked.
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted.
                startNextActivity();
            } else {
                Log.i(TAG, "All permissions were NOT granted.");
                Snackbar.make(entireLayout, "All permissions were not granted. You cannot proceed.", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "To proceed, enable permissions from Settings -> Apps -> Android Support " +
                                "-> Permissions", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void startNextActivity() {
        Intent i = new Intent(context, DrawRoute.class);
        startActivity(i);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        entireLayout = (RelativeLayout) findViewById(R.id.entire_layout);
    }

    @Override
    public void onItemClick(View view) {
        int position = mRecyclerView.getChildAdapterPosition(view);
        switch (position) {
            case 0:
                Intent i = new Intent(context, SearchPlaces.class);
                startActivity(i);
                break;
            case 1:
                if ((int) Build.VERSION.SDK_INT < 23) {
                    Log.d(TAG, Build.VERSION.SDK_INT + " API Level");
                    startNextActivity();
                } else {
                    Log.d(TAG, Build.VERSION.SDK_INT + " API Level 23");
                    requestLocationPermissions();
                }
                break;
            case 2:
                startActivity(new Intent(context, GoogleFit.class));
                break;
            case 3:
                startActivity(new Intent(context, LocationUpdate.class));
                break;
            default:
                Toast.makeText(context, "To Be Done!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
