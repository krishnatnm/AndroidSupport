package com.tanmay.androidsupport.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.tanmay.androidsupport.R;

public class OtpInput extends AppCompatActivity {

    public static String TAG = "OTP ==>";

    Context context;

    Toolbar toolbar;
    EditText one, two, three, four;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_input);

        initView();
        context = this;
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);

        one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                two.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                three.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                four.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pDialog.setMessage("Click outside to cancel...");
                pDialog.setCancelable(true);
                pDialog.show();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        one = (EditText) findViewById(R.id.coi_one);
        two = (EditText) findViewById(R.id.coi_two);
        three = (EditText) findViewById(R.id.coi_three);
        four = (EditText) findViewById(R.id.coi_four);
    }
}
