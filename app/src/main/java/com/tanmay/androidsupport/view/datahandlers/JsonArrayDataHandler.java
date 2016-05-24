package com.tanmay.androidsupport.view.datahandlers;

import android.util.Log;

import com.android.volley.VolleyError;
import com.tanmay.androidsupport.httpservice.HttpRequestHandler;
import com.tanmay.androidsupport.httpservice.SuccessListener;
import com.tanmay.androidsupport.view.activities.HttpHome;
import com.tanmay.androidsupport.view.interfaces.ResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by TaNMay on 3/14/2016.
 */
public class JsonArrayDataHandler implements SuccessListener {

    public static String TAG = "JsonArrayDH ==>";
    ResponseListener listener;

    public JsonArrayDataHandler(HttpHome listener) {
        this.listener = listener;
    }

    public void sendRequest(String url, String token, int method) {
        Log.d(TAG, "URL: " + url);
        Log.d(TAG, "Token: " + token + "..");
        Log.d(TAG, "Method: " + method);
        HttpRequestHandler.makeJsonArrayRequest(url, this, token);
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
