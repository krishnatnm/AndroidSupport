package com.tanmay.androidsupport.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.dialogs.CustomDialogs;
import com.tanmay.androidsupport.interfaces.OnCustomDialogButtonClickListener;

public class CustomDialogActivity extends AppCompatActivity implements OnCustomDialogButtonClickListener {

    public static String TAG = "UtilityDialogs ==>";

    Context context;

    Toolbar toolbar;

    CustomDialogs customDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dialog);

        initView();
        context = this;
        setSupportActionBar(toolbar);

        customDialogs = new CustomDialogs(context, this);
    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    public void onRoundedDialogClick(View view) {
        customDialogs.roundedCornerDialog();
    }

}
