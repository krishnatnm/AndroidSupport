package com.tanmay.androidsupport.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.view.adapter.TabsPagerAdapter;

public class TabsAndViewPager extends AppCompatActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    public static String TAG = "Tabs ==>";
    public static int tabCount;
    Context context;
    Toolbar toolbar;
    FloatingActionButton fab;
    TabHost mTabHost;
    ViewPager mViewPager;
    TabsPagerAdapter mAdapter;

    private static View createTabView(final Context context, final String text, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_icon, null);
        TextView tabTitle = (TextView) view.findViewById(R.id.tab_title);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_icon);
        tabTitle.setText(text);
        Picasso.with(context).load(icon)
                .resizeDimen(R.dimen.tab_icon, R.dimen.tab_icon)
                .centerCrop()
                .into(tabIcon);
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_and_view_pager);
        initView();

        context = this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tabs and View Pager");
        tabCount = 0;

        initialiseTabHost();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabCount < 9) {
                    setupTab(new TextView(context), "Extra", R.drawable.ic_extra);
                    mAdapter.notifyDataSetChanged();
                } else
                    Toast.makeText(context, "Please don't grow crazy!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    private void initialiseTabHost() {
        mTabHost.setup();

        setupTab(new TextView(this), "Home", R.drawable.ic_home);
        setupTab(new TextView(this), "News", R.drawable.ic_news);
        setupTab(new TextView(this), "Notifications", R.drawable.ic_notification);
        setupTab(new TextView(this), "Profile", R.drawable.ic_profile);

        mTabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onTabChanged(String tabId) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int pos = this.mViewPager.getCurrentItem();
        this.mTabHost.setCurrentTab(pos);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setupTab(final View view, final String tag, int icon) {
        tabCount++;
        View tabview = createTabView(mTabHost.getContext(), tag, icon);
        TabHost.TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return view;
            }
        });
        mTabHost.addTab(setContent);
    }

}