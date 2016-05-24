package com.tanmay.androidsupport.httpservice;

import android.os.AsyncTask;

import org.json.JSONObject;

/**
 * Created by TaNMay on 3/3/2016.
 */
public class AsyncTaskForPatch extends AsyncTask<JSONObject, Integer, String> {

    String url, token, etag;

    public AsyncTaskForPatch(String url, String token, String etag) {
        this.url = url;
        this.token = token;
        this.etag = etag;
    }

    @Override
    protected String doInBackground(JSONObject... params) {
        HttpPatchRequest.sendPatch(url, params[0], token, etag);
        return "Executed";
    }
}