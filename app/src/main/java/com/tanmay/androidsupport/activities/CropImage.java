package com.tanmay.androidsupport.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.camera.CropImageIntentBuilder;
import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.permissions.PermissionUtil;
import com.tanmay.androidsupport.utils.MediaStoreUtils;

import java.io.File;

public class CropImage extends AppCompatActivity {

    private static final int REQUEST_READ_STORAGE = 1;
    public static String TAG = "CropImage ==>";
    private static int REQUEST_PICTURE = 1;
    private static int REQUEST_CROP_PICTURE = 2;
    private static String[] PERMISSIONS_READ_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};

    Context context;

    Toolbar toolbar;
    RelativeLayout entireLayout;
    ImageView croppedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        context = this;
        initView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        entireLayout = (RelativeLayout) findViewById(R.id.cci_entire_layout);
        croppedImage = (ImageView) findViewById(R.id.cci_cropped_image);
    }

    public void selectImage(View view) {
        if ((int) Build.VERSION.SDK_INT < 23) {
            Log.d(TAG, Build.VERSION.SDK_INT + " API Level");
            startNextActivity();
        } else {
            Log.d(TAG, Build.VERSION.SDK_INT + " API Level 23");
            requestPhonePermissions();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        File croppedImageFile = new File(getFilesDir(), "test.jpg");

        if ((requestCode == REQUEST_PICTURE) && (resultCode == RESULT_OK)) {
            // When the user is done picking a picture, let's start the CropImage Activity,
            // setting the output image file and size to 200x200 pixels square.
            Uri croppedImage = Uri.fromFile(croppedImageFile);

            CropImageIntentBuilder cropImage = new CropImageIntentBuilder(200, 200, croppedImage);
            cropImage.setOutlineColor(0xFF03A9F4);
            cropImage.setSourceImage(data.getData());

            startActivityForResult(cropImage.getIntent(this), REQUEST_CROP_PICTURE);
        } else if ((requestCode == REQUEST_CROP_PICTURE) && (resultCode == RESULT_OK)) {
            // When we are done cropping, display it in the ImageView.
            Log.d(TAG, "Path: " + croppedImageFile.getAbsolutePath());
            croppedImage.setImageBitmap(BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath()));
//            imageView.setImageBitmap(BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath()));
        }
    }

    private void requestPhonePermissions() {
        boolean requestPermission = PermissionUtil.requestLocationPermissions(this);
        if (requestPermission == true) {
            if (checkSelfPermission(PERMISSIONS_READ_STORAGE[0]) == PackageManager.PERMISSION_GRANTED) {
                startNextActivity();
            } else {
                // Display a SnackBar with an explanation and a button to trigger the request.
                ActivityCompat.requestPermissions(CropImage.this, PERMISSIONS_READ_STORAGE, REQUEST_READ_STORAGE);
            }
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_READ_STORAGE, REQUEST_READ_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_READ_STORAGE) {
            Log.i(TAG, "Received response for permissions request.");
            // We have requested multiple permissions for location, so all of them need to be checked.
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted.
                startNextActivity();
            } else {
                Log.i(TAG, "All permissions were NOT granted.");
                Snackbar.make(entireLayout, "Phone permissions were not granted. You cannot proceed.", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "To proceed, enable permissions from Settings -> Apps -> Android Support " +
                                "-> Permissions", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void startNextActivity() {
        startActivityForResult(MediaStoreUtils.getPickImageIntent(this), REQUEST_PICTURE);
    }
}
