package com.tanmay.androidsupport.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TabHost;

import com.tanmay.androidsupport.R;

/**
 * Created by TaNMay on 3/16/2016.
 */
public class TabFactory implements TabHost.TabContentFactory {

    private final Context mContext;

    public TabFactory(Context context) {
        mContext = context;
    }

    @SuppressLint("NewApi")
    public View createTabContent(String tag) {
        View v = new View(mContext);
        v.setMinimumWidth(0);
        v.setMinimumHeight(0);
        v.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        return v;
    }
}