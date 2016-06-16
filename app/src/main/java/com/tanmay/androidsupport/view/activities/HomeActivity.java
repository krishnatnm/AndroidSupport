package com.tanmay.androidsupport.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;
import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.utils.ConstantClass;
import com.tanmay.androidsupport.view.adapter.DrawerAdapter;
import com.tanmay.androidsupport.view.interfaces.OnDrawerItemClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.fabric.sdk.android.Fabric;

public class HomeActivity extends AppCompatActivity implements OnDrawerItemClickListener {

    public static String TAG = "Home ==>";

    Context context;
    FloatingActionButton fab;
    Toolbar toolbar;
    Button quickView;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    LinearLayoutManager mLayoutManager;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onStart() {
        super.onStart();
        // init Flurry
        FlurryAgent.init(this, ConstantClass.MY_FLURRY_APIKEY);
        FlurryAgent.onStartSession(this, ConstantClass.MY_FLURRY_APIKEY);
        FlurryAgent.logEvent("#TNM--Home_onStart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Fabric.with(this, new Crashlytics());
        initView();

        logUser();

        context = this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.holo_red_dark)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new DrawerAdapter(this, ConstantClass.DRAWER, ConstantClass.DRAWER_ICONS);
        mRecyclerView.setAdapter(mAdapter);
        DrawerAdapter.click = HomeActivity.this;
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
//        Log.d(TAG, "Date and Time: " + currentDateandTime);

//        testDate();

        quickView.setText("Location Update Service");
        quickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LocationUpdate.class);
                startActivity(i);
            }
        });

//        FacebookHashKey.printKeyHash(this);
    }

    void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.ah_fab);
        mRecyclerView = (RecyclerView) findViewById(R.id.ad_recycler_view);
        mDrawer = (DrawerLayout) findViewById(R.id.ah_drawer_layout);
        quickView = (Button) findViewById(R.id.ch_quick_view);
    }

    @Override
    public void onDrawerItemClick(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(context, GoogleHome.class));
                break;
            case 1:
                startActivity(new Intent(context, HttpHome.class));
                break;
            case 2:
                startActivity(new Intent(context, PhoneHome.class));
                break;
            case 3:
                startActivity(new Intent(context, SocialHome.class));
                break;
            case 4:
                startActivity(new Intent(context, DesignHome.class));
                break;
            case 5:
                startActivity(new Intent(context, ChatHome.class));
                break;
            case 6:
                startActivity(new Intent(context, OtherHome.class));
                break;
            default:
                Toast.makeText(context, "To Be Done!", Toast.LENGTH_SHORT).show();
                mDrawer.closeDrawers();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    private void logUser() {
        Crashlytics.setUserIdentifier("#1803");
        Crashlytics.setUserEmail(getString(R.string.enter));
        Crashlytics.setUserName(getString(R.string.xyz));
    }

    void testDate() {
        String originalDate = "2016-09-07T00:00:00.000Z";
        String finalDate = originalDate.substring(8, 10) + "/" + originalDate.substring(5, 7) + "/" + originalDate.substring(0, 4);
        Log.d(TAG, "Final: " + finalDate);
    }
}
