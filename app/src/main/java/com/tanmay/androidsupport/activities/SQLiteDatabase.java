package com.tanmay.androidsupport.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tanmay.androidsupport.R;

public class SQLiteDatabase extends AppCompatActivity {

    public static String TAG = "SQLite ==>";

    Context context;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_database);

        initView();
        context=this;
        setSupportActionBar(toolbar);


    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

}
