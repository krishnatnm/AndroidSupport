package com.tanmay.androidsupport.view.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.tanmay.androidsupport.R;

public class CopyToClipbpard extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab;
    Context context;
    EditText sourceText, sampleSpace;
    Button copy;
    ClipboardManager clipboard;
    ClipData clip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_to_clipbpard);

        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Copy Text to Cipboard");
        context = this;

        fab.setVisibility(View.GONE);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(CopyToClipbpard.this);
                clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clip = ClipData.newPlainText(context.getResources().getString(R.string.text_copied),
                        sourceText.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });
    }

    void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        sourceText = (EditText) findViewById(R.id.source_text);
        sampleSpace = (EditText) findViewById(R.id.sample_space);
        copy = (Button) findViewById(R.id.copy_button);
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}