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

/**
 * Created by TaNMay on 3/17/2016.
 */
public class SocialHome extends AppCompatActivity implements OnLandingItemClickListener {

    private static final int REQUEST_CONTACTS = 1;
    public static String TAG = "SocialHome ==>";
    private static String[] PERMISSIONS_CONTACTS = {Manifest.permission.READ_CONTACTS};
    Context context;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    ArrayList<String> listItems = new ArrayList<>();

    RecyclerView mRecyclerView;
    Toolbar toolbar;
    RelativeLayout entireLayout;

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, ConstantClass.MY_FLURRY_APIKEY);
        FlurryAgent.logEvent("#TNM--SocialHome");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        initView();
        context = this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Social");
        FlurryAgent.logEvent("#TNM--SocialHome_onCreate");

        listItems.add("Facebook");
        listItems.add("Google");

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LandingAdapter(context, listItems);
        mRecyclerView.setAdapter(mAdapter);
        LandingAdapter.click = SocialHome.this;
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
                startActivity(new Intent(context, Facebook.class));
                break;
            case 1:
                if ((int) Build.VERSION.SDK_INT < 23) {
                    Log.d(TAG, Build.VERSION.SDK_INT + " API Level");
                    startNextActivity();
                } else {
                    Log.d(TAG, Build.VERSION.SDK_INT + " API Level 23");
                    requestContactPermissions();
                }
                break;
            default:
                Toast.makeText(context, "To Be Done!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    private void requestContactPermissions() {
        boolean requestPermission = PermissionUtil.requestLocationPermissions(this);
        if (requestPermission == true) {
            if (checkSelfPermission(PERMISSIONS_CONTACTS[0]) == PackageManager.PERMISSION_GRANTED) {
                startNextActivity();
            } else {
                // Display a SnackBar with an explanation and a button to trigger the request.
                ActivityCompat.requestPermissions(SocialHome.this, PERMISSIONS_CONTACTS, REQUEST_CONTACTS);
            }
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACTS, REQUEST_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_CONTACTS) {
            Log.i(TAG, "Received response for permissions request.");
            // We have requested multiple permissions for location, so all of them need to be checked.
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted.
                startNextActivity();
            } else {
                Log.i(TAG, "All permissions were NOT granted.");
                Snackbar.make(entireLayout, "Contact permissions were not granted. You cannot proceed.", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
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
        startActivity(new Intent(context, Google.class));
    }
}
