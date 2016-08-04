package com.tanmay.androidsupport.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.fragments.YouTubeFragment;

public class YouTubeActivity extends AppCompatActivity {

//    public static final String DEVELOPER_KEY = "AIzaSyAB8OqwMtCg5z4hYyjzxjxktg3OBRW-VKg";
//    private static final int RECOVERY_DIALOG_REQUEST = 1;

    public static String TAG = "YouTube ==>";

    Context context;

    Toolbar toolbar;
//    YouTubePlayerView youTubeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);

        initView();
        context = this;
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        youTubeView.initialize(DEVELOPER_KEY, this);

//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        YouTubeFragment youTubeFragment = new YouTubeFragment();
//        fragmentTransaction.add(R.id.cyt_fragment_container, youTubeFragment, "YouTube");
//        fragmentTransaction.commit();

        YouTubeFragment fragment = new YouTubeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.cyt_main, fragment)
                .addToBackStack(null)
                .commit();

    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//        if (!b) {
//            youTubePlayer.cueVideo("IssysxAisfo");
//        }
//    }

//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//        if (youTubeInitializationResult.isUserRecoverableError()) {
//            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
//        } else {
//            String errorMessage = String.format(getString(R.string.error_player), youTubeInitializationResult.toString());
//            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
//        }
//    }
}
