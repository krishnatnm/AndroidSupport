package com.tanmay.androidsupport.view.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.permissions.PermissionUtil;
import com.tanmay.androidsupport.view.adapter.DocumentAdapter;
import com.tanmay.androidsupport.view.dialogs.CustomDialogs;
import com.tanmay.androidsupport.view.interfaces.OnDialogButtonClickListener;
import com.tanmay.androidsupport.view.models.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CameraUpload extends AppCompatActivity implements OnDialogButtonClickListener {

    private static final int CAMERA_REQUEST = 1888;
    private static final int REQUEST_READ_STORAGE = 1;
    public static String TAG = "CameraUpload ==>";
    private static String[] PERMISSIONS_READ_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};

    Context context;

    Toolbar toolbar;
    RecyclerView documents;
    RelativeLayout entireLayout;
    TextView info;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    ArrayList<Document> documentList;

    CustomDialogs customDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_upload);

        context = this;
        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        documentList = new ArrayList<Document>();
        customDialogs = new CustomDialogs(context, this);

        setDocumentGrid();
        if (documentList.size() != 0) {
            info.setVisibility(View.GONE);
        } else {
            info.setVisibility(View.VISIBLE);
        }
    }

    void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        documents = (RecyclerView) findViewById(R.id.ccu_recycler_view);
        info = (TextView) findViewById(R.id.ccu_info);
        entireLayout = (RelativeLayout) findViewById(R.id.ccu_entire_layout);
    }

    public void onAddDocumentClick(View view) {
        if ((int) Build.VERSION.SDK_INT < 23) {
            Log.d(TAG, Build.VERSION.SDK_INT + " API Level");
            startNextActivity();
        } else {
            Log.d(TAG, Build.VERSION.SDK_INT + " API Level 23");
            requestPhonePermissions();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            storeImage(photo, "test");
//            imageView.setImageBitmap(photo);

        }
    }

    public void storeImage(Bitmap image, String name) {
        File pictureFile = getOutputMediaFile(name);
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

        customDialogs.inputText(pictureFile);

    }

    public File getOutputMediaFile(String name) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + name + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public void setDocumentGrid() {
        documents.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        documents.setLayoutManager(mLayoutManager);
        mAdapter = new DocumentAdapter(this, documentList);
        documents.setAdapter(mAdapter);
    }

    @Override
    public void onAcceptClick(String origin, String destination) {

    }

    @Override
    public void onAcceptClick(String title, File file) {
        Document document = new Document();
        document.setFileName(file);
        document.setTitle(title);
        documentList.add(document);

        if (documentList.size() != 0) {
            info.setVisibility(View.GONE);
        }

        mAdapter.notifyDataSetChanged();
    }

    void startNextActivity() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void requestPhonePermissions() {
        boolean requestPermission = PermissionUtil.requestLocationPermissions(this);
        if (requestPermission == true) {
            if (checkSelfPermission(PERMISSIONS_READ_STORAGE[0]) == PackageManager.PERMISSION_GRANTED) {
                startNextActivity();
            } else {
                // Display a SnackBar with an explanation and a button to trigger the request.
                ActivityCompat.requestPermissions(CameraUpload.this, PERMISSIONS_READ_STORAGE, REQUEST_READ_STORAGE);
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
}
