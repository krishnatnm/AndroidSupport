package com.tanmay.androidsupport.datahandlers;

import android.util.Log;

import com.android.volley.VolleyError;
import com.tanmay.androidsupport.httpservice.HttpRequestHandler;
import com.tanmay.androidsupport.httpservice.SuccessListener;
import com.tanmay.androidsupport.activities.HttpHome;
import com.tanmay.androidsupport.interfaces.ResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by TaNMay on 3/14/2016.
 */
public class CustomRequestDataHandler implements SuccessListener {

    public static String TAG = "CustomReqDH ==>";
    ResponseListener listener;

    public CustomRequestDataHandler(HttpHome listener) {
        this.listener = listener;
    }

    public void sendRequest(JSONObject jObject, String url, String token, int method) {
        Log.d(TAG, "URL: " + url);
        Log.d(TAG, "JSON Object: " + jObject.toString() + "..");
        Log.d(TAG, "Token: " + token + "..");
        Log.d(TAG, "Method: " + method);
        HttpRequestHandler.makeCustomRequest(url, jObject, this, method,  token);
    }

    @Override
    public void onSuccess(JSONObject jObject, JSONArray jArray) {
        if (jArray != null) {
            listener.gotArrayResponse(jArray);
        }
    }

    @Override
    public void onError(VolleyError vError, String errMsg, int errCode) {
        listener.errorResponse();
    }
}