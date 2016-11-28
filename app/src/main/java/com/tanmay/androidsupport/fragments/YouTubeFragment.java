package com.tanmay.androidsupport.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tanmay.androidsupport.R;

/**
 * Created by TaNMay on 01/08/16.
 */
public class YouTubeFragment extends Fragment {

    public static final String DEVELOPER_KEY = "AIzaSyAB8OqwMtCg5z4hYyjzxjxktg3OBRW-VKg";
    public static final String VIDEO_ID = "IssysxAisfo";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yt_test_fragment, container, false);

//        youTubePlayerFragment = (YouTubePlayerView) view.findViewById(R.id.ytf_youtube);
//        youTubePlayerFragment.initialize(DEVELOPER_KEY, this);

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    player.loadVideo(VIDEO_ID);
//                    player.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                // YouTube error
                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
            }
        });

        return view;
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
//            youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
//        } else {
//            String errorMessage = "There was an error initializing the YouTubePlayer!";
//            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
//        }
//    }
}
