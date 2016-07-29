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

import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.permissions.PermissionUtil;
import com.tanmay.androidsupport.adapter.LandingAdapter;
import com.tanmay.androidsupport.interfaces.OnLandingItemClickListener;

import java.util.ArrayList;

public class PhoneHome extends AppCompatActivity implements OnLandingItemClickListener {

    public static String TAG = "PhoneHome ==>";
    private static String[] PERMISSIONS_CONTACTS = {Manifest.permission.READ_CONTACTS};
    private static final int REQUEST_CONTACTS = 1;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    Context context;
    ArrayList<String> listItems = new ArrayList<>();

    RecyclerView mRecyclerView;
    Toolbar toolbar;
    RelativeLayout entireLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        initView();

        context = this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Phone Functionalities");

        listItems.add("Search Contacts");
        listItems.add("Copy Text to Cipboard");
        listItems.add("Tabs and View Pager");
        listItems.add("Crop Image");
        listItems.add("Camera Upload");
        listItems.add("OTP");
        listItems.add("Custom Dialogs");

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LandingAdapter(context, listItems);
        mRecyclerView.setAdapter(mAdapter);
        LandingAdapter.click = PhoneHome.this;
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
                if ((int) Build.VERSION.SDK_INT < 23) {
                    Log.d(TAG, Build.VERSION.SDK_INT + " API Level");
                    startNextActivity();
                } else {
                    Log.d(TAG, Build.VERSION.SDK_INT + " API Level 23");
                    requestContactPermissions();
                }
                break;
            case 1:
                startActivity(new Intent(context, CopyToClipbpard.class));
                break;
            case 2:
                startActivity(new Intent(context, TabsAndViewPager.class));
                break;
            case 3:
                startActivity(new Intent(context, CropImage.class));
                break;
            case 4:
                startActivity(new Intent(context, CameraUpload.class));
                break;
            case 5:
                startActivity(new Intent(context, OtpInput.class));
                break;
            case 6:
                startActivity(new Intent(context, CustomDialogActivity.class));
                break;
            default:
                Toast.makeText(context, "To Be Done!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void requestContactPermissions() {
        boolean requestPermission = PermissionUtil.requestLocationPermissions(this);
        if (requestPermission == true) {
            if (checkSelfPermission(PERMISSIONS_CONTACTS[0]) == PackageManager.PERMISSION_GRANTED) {
                startNextActivity();
            } else {
                // Display a SnackBar with an explanation and a button to trigger the request.
                ActivityCompat.requestPermissions(PhoneHome.this, PERMISSIONS_CONTACTS, REQUEST_CONTACTS);
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
        startActivity(new Intent(context, AddContacts.class));
    }
}