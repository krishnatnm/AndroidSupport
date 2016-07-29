package com.tanmay.androidsupport.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.adapter.GenericAdapter;

public class ParallaxWithWebView extends AppCompatActivity {

    public static String TAG = "ParallaxWebView ==> ";

    Context context;

    Toolbar toolbar;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallax_with_web_view);

        initView();
        context = this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbarLayout.setTitle("Hello World!");
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GenericAdapter(context);
        mRecyclerView.setAdapter(mAdapter);
    }

    void initView() {
        toolbar = (Toolbar) findViewById(R.id.apwv_toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.apwv_recycler_view);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.apwv_collapsing_toolbar_layout);
    }
}
