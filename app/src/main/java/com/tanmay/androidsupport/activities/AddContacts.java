package com.tanmay.androidsupport.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.permissions.PermissionUtil;
import com.tanmay.androidsupport.utils.Validator;

import java.io.IOException;
import java.io.InputStream;

public class AddContacts extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private static final int REQUEST_DIAL = 1;
    public static String TAG = "AddContacts ==>";
    private static String[] PERMISSIONS_DIAL = {Manifest.permission.CALL_PHONE};
    Toolbar toolbar;
    FloatingActionButton fabOk, fabAdd, fabCall;
    TextView theName, theNumber;
    EditText contactName, contactNumber;
    ImageView theImage;
    RelativeLayout theContact, contentAddContacts;
    String callNum;
    Context context;
    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        initView();
        context = this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Contacts");
        validator = new Validator();

        fabOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theContact.setVisibility(View.GONE);
                theImage.setImageResource(R.drawable.ic_self);
                theName.setText("");
                theNumber.setText("");

                hideSoftKeyboard(AddContacts.this);

                if (contactName.getText().toString().length() == 0
                        && contactNumber.getText().toString() == null) {
                    Snackbar.make(view, "Enter valid contact details!", Snackbar.LENGTH_LONG).show();
                } else if (!validator.isValidMobileNumber(contactNumber.getText().toString())) {
                    Snackbar.make(view, "Enter valid contact number!", Snackbar.LENGTH_LONG).show();
                } else {
                    theName.setText(contactName.getText().toString());
                    theNumber.setText(contactNumber.getText().toString());
                    theContact.setVisibility(View.VISIBLE);
                    fabCall.setVisibility(View.VISIBLE);
                }
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theContact.setVisibility(View.GONE);
                theImage.setImageResource(R.drawable.ic_self);
                theName.setText("");
                theNumber.setText("");
                // using native contacts selection
                // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
            }
        });

        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int) Build.VERSION.SDK_INT < 23) {
                    Log.d(TAG, Build.VERSION.SDK_INT + " API Level");
                    startNextActivity();
                } else {
                    Log.d(TAG, Build.VERSION.SDK_INT + " API Level 23");
                    requestPhonePermissions();
                }
            }
        });
    }

    void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabOk = (FloatingActionButton) findViewById(R.id.fab_ok);
        fabCall = (FloatingActionButton) findViewById(R.id.fab_call);
        contactName = (EditText) findViewById(R.id.contact_name);
        contactNumber = (EditText) findViewById(R.id.contact_number);
        theName = (TextView) findViewById(R.id.the_name);
        theNumber = (TextView) findViewById(R.id.the_number);
        theContact = (RelativeLayout) findViewById(R.id.the_contact);
        theImage = (ImageView) findViewById(R.id.the_image);
        contentAddContacts = (RelativeLayout) findViewById(R.id.content_add_contacts);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {

            //get result uri
            Uri result = data.getData();
            Log.v(TAG, "Got a result: " + result.toString());
            // get the phone number id from the Uri
            String id = result.getLastPathSegment();
            // query the phone numbers for the selected phone number id
            Cursor c = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone._ID + "=?",
                    new String[]{id}, null);
            //index of number
            int phoneIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            //index of name
            int nameIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            //index of photo
            int photoIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI);
            //there will be single number
            if (c.getCount() == 1) {
                if (c.moveToFirst()) {
                    String phone = c.getString(phoneIdx);
                    //if there is no contact name, will get number as display name
                    String name = c.getString(nameIdx);
                    //fixing the number obtained
                    phone = phone.replaceAll("[^[0-9]+]", "");
                    //free up the cursor
                    c.close();
                    Log.d(TAG, "Got details -- " + "Name: " + name + ", Phone: " + phone);

                    contactName.setText(name);
                    contactNumber.setText(phone);
                    theName.setText(name);
                    theNumber.setText(phone);
                    theContact.setVisibility(View.VISIBLE);
                    fabCall.setVisibility(View.VISIBLE);
                    theImage.setImageBitmap(retrieveContactPhoto(phone));
                } else {
                    Log.d("No results", "for contact pick");
                }
            }
        }
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void requestPhonePermissions() {
        boolean requestPermission = PermissionUtil.requestLocationPermissions(this);
        if (requestPermission == true) {
            if (checkSelfPermission(PERMISSIONS_DIAL[0]) == PackageManager.PERMISSION_GRANTED) {
                startNextActivity();
            } else {
                // Display a SnackBar with an explanation and a button to trigger the request.
                ActivityCompat.requestPermissions(AddContacts.this, PERMISSIONS_DIAL, REQUEST_DIAL);
            }
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_DIAL, REQUEST_DIAL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_DIAL) {
            Log.i(TAG, "Received response for permissions request.");
            // We have requested multiple permissions for location, so all of them need to be checked.
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted.
                startNextActivity();
            } else {
                Log.i(TAG, "All permissions were NOT granted.");
                Snackbar.make(contentAddContacts, "Phone permissions were not granted. You cannot proceed.", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
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
        callNum = theNumber.getText().toString();
        if (callNum == null) {
            Snackbar.make(contentAddContacts, "No valid Phone Number!", Snackbar.LENGTH_LONG).show();
        }
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + callNum));
        context.startActivity(callIntent);
    }

    public Bitmap retrieveContactPhoto(String number) {
        ContentResolver contentResolver = context.getContentResolver();
        String contactId = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};
        Cursor cursor = contentResolver.query(
                uri,
                projection,
                null,
                null,
                null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }
        Bitmap photo = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_self);
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactId)));
            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            }
//            assert inputStream != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }

}