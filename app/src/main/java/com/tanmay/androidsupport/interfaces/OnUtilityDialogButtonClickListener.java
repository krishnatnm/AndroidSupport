package com.tanmay.androidsupport.interfaces;

import java.io.File;

/**
 * Created by TaNMay on 3/8/2016.
 */
public interface OnUtilityDialogButtonClickListener {
    void onAcceptClick(String origin, String destination);

    void onAcceptClick(String title, File fileName);
}
