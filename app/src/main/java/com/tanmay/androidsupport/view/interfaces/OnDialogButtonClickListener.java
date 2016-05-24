package com.tanmay.androidsupport.view.interfaces;

import java.io.File;

/**
 * Created by TaNMay on 3/8/2016.
 */
public interface OnDialogButtonClickListener {
    void onAcceptClick(String origin, String destination);

    void onAcceptClick(String title, File fileName);
}
