package com.tanmay.androidsupport.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.utils.ConstantClass;
import com.tanmay.androidsupport.view.adapter.LandingAdapter;
import com.tanmay.androidsupport.view.interfaces.OnLandingItemClickListener;

import java.util.ArrayList;

public class OtherHome extends AppCompatActivity implements OnLandingItemClickListener {

    public static String TAG = "OtherHome ==>";
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
        FlurryAgent.logEvent("#TNM--OthersHome");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        initView();
        context = this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Others");
        FlurryAgent.logEvent("#TNM--OtherHome_onCreate");

        listItems.add("Flurry Analytics");

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LandingAdapter(context, listItems);
        mRecyclerView.setAdapter(mAdapter);
        LandingAdapter.click = OtherHome.this;
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
                Intent i = new Intent(context, FlurryAnalytics.class);
                startActivity(i);
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
}
