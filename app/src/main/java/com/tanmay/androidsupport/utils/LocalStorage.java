package com.tanmay.androidsupport.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by TaNMay on 3/2/2016.
 */
public class LocalStorage {

    private static LocalStorage instance = null;
    SharedPreferences sharedPreferences;

    public LocalStorage(Context context) {
        sharedPreferences = context.getSharedPreferences("Reg", 0);
    }

    public static LocalStorage getInstance(Context context) {
        if (instance == null) {
            synchronized (LocalStorage.class) {
                if (instance == null) {
                    instance = new LocalStorage(context);
                }
            }
        }
        return instance;
    }

    public String getFacebookAccessToken() {
        if (sharedPreferences.contains("FB_TOKEN")) {
            return sharedPreferences.getString("FB_TOKEN", null);
        } else {
            return null;
        }
    }

    public void setFacebookAccessToken(String fb_token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FB_TOKEN", fb_token);
        editor.commit();
    }

    public Boolean isLoggedInFb() {
        if (sharedPreferences.contains("FB_STATUS")) {
            return sharedPreferences.getBoolean("FB_STATUS", false);
        } else {
            return false;
        }
    }

    public void setLoggedInFb(Boolean fbStatus) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("FB_STATUS", fbStatus);
        editor.commit();
    }
}
