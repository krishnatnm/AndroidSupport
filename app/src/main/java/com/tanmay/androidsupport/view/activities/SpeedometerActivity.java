package com.tanmay.androidsupport.view.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.view.customui.CustomSpeedometer;

public class SpeedometerActivity extends AppCompatActivity {

    public static String TAG = "Speedometer ==>";

    FloatingActionButton fab;
    Toolbar toolbar;
    Context context;

    CustomSpeedometer speedometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedometer_view);
        initView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Speedometer View");

        fab.setVisibility(View.INVISIBLE);

        speedometer.setMaxSpeed(50);
        speedometer.setLabelConverter(new CustomSpeedometer.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });
        speedometer.setMaxSpeed(50);
        speedometer.setMajorTickStep(5);
        speedometer.setMinorTicks(4);
        speedometer.addColoredRange(0, 30, Color.GREEN);
        speedometer.addColoredRange(30, 45, Color.YELLOW);
        speedometer.addColoredRange(45, 50, Color.RED);
        speedometer.setSpeed(33, 1000, 300);
    }

    void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        speedometer = (CustomSpeedometer) findViewById(R.id.speedometer);

    }

}
