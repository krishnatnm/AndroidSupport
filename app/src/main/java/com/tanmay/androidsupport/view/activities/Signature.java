package com.tanmay.androidsupport.view.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.view.dialogs.CustomDialogs;
import com.tanmay.androidsupport.view.interfaces.OnDialogButtonClickListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Signature extends AppCompatActivity implements OnDialogButtonClickListener {

    public static String TAG = "Signature ==>";

    Context context;

    Toolbar toolbar;
    SignaturePad mSignaturePad;

    CustomDialogs customDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        context = this;
        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        customDialogs = new CustomDialogs(context, this);

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });
    }

    void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSignaturePad = (SignaturePad) findViewById(R.id.cs_signature_pad);
    }

    public void onSaveClick(View view) {
        if (mSignaturePad.isEmpty()) {
            Toast.makeText(context, "Please sign here!", Toast.LENGTH_SHORT).show();
        } else {
            Bitmap bitmap = mSignaturePad.getSignatureBitmap();
            storeImage(bitmap);
        }
    }

    public void onClearClick(View view) {
        mSignaturePad.clear();
    }

    public void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions!");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    public File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public void onViewPrevious(View view) {
        customDialogs.viewSignature(getOutputMediaFile());
    }

    @Override
    public void onAcceptClick(String origin, String destination) {

    }

    @Override
    public void onAcceptClick(String title, File fileName) {

    }
}
