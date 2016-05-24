package com.tanmay.androidsupport.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;
import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.utils.LocalStorage;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Facebook extends AppCompatActivity {

    public static String TAG = "Facebook ==>";
    Context context;
    Toolbar toolbar;
    FloatingActionButton fab;
    RelativeLayout profileSec;
    TextView name, birthday, gender, locationName;
    ImageView profilePic;
    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ProfileTracker profileTracker;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook);

        ////////////////// To get hash key //////////////////
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.tanmay.androidsupport",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        /////////////////////////////////////////////////////

        context = this;
        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Facebook");

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                Log.d(TAG, "Access Token Tracker");
                Log.d(TAG, "Old Access Token: " + oldAccessToken);
                Log.d(TAG, "Current Access Token: " + currentAccessToken);

                if (currentAccessToken == null) {
                    profileSec.setVisibility(View.GONE);
                    LocalStorage.getInstance(context).setLoggedInFb(false);
                }
            }
        };

        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

        if (LocalStorage.getInstance(context).isLoggedInFb()) {
            profileSec.setVisibility(View.VISIBLE);
            getFacebookProfile();
        }

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                Log.d(TAG, "Profile Tracker");
                Log.d(TAG, "Old Profile: " + oldProfile);
                Log.d(TAG, "Current Profile: " + currentProfile);
            }
        };

        if (AccessToken.getCurrentAccessToken() != null) {
            getFacebookProfile();
        }

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "Login onSuccess");

                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "Login onCancel");

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "Login onError");

                    }
                });

        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "LoginCallback onSuccess");
                Log.d(TAG, "Access token: " + accessToken);
                LocalStorage.getInstance(context).setLoggedInFb(true);
                profileSec.setVisibility(View.VISIBLE);
                getFacebookProfile();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "LoginCallback onCancel");
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "LoginCallback onError");
                // App code
            }
        });
        fab.setVisibility(View.INVISIBLE);
    }

    void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        profileSec = (RelativeLayout) findViewById(R.id.profile);
        name = (TextView) findViewById(R.id.name);
        gender = (TextView) findViewById(R.id.gender);
        birthday = (TextView) findViewById(R.id.birthday);
        locationName = (TextView) findViewById(R.id.location);
        profilePic = (ImageView) findViewById(R.id.profile_picture);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    void getFacebookProfile() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d(TAG, "GraphResponse: " + response.toString());

                        if (object != null) {
                            Log.d(TAG, "Object: " + object.toString());

                            userId = object.optString("id");
                            name.setText(object.optString("name"));
                            gender.setText("(" + object.optString("gender").substring(0, 1).toUpperCase()
                                    + object.optString("gender").substring(1) + ")");
                            birthday.setText("Date of Birth: " + object.optString("birthday"));
                            locationName.setText("Location: " + object.optJSONObject("location").optString("name"));

                            getFacebookDpDirectly(userId);
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,location");
        request.setParameters(parameters);
        request.executeAsync();
    }

    void getFacebookDpDirectly(String userId) {
        String dpUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";
        Picasso.with(context).load(dpUrl)
                .placeholder(R.drawable.android)
                .error(R.drawable.android)
                .resizeDimen(R.dimen.facebook_dp, R.dimen.facebook_dp)
                .centerCrop()
                .into(profilePic);

    }

}
