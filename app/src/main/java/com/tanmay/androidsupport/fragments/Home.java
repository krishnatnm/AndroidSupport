package com.tanmay.androidsupport.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tanmay.androidsupport.R;

/**
 * Created by TaNMay on 3/15/2016.
 */
public class Home extends Fragment {

    TextView genericText;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_generic, container, false);
        initView();
        genericText.setText("Home");
        return rootView;

    }

    void initView() {
        genericText = (TextView) rootView.findViewById(R.id.generic_text);
    }
}