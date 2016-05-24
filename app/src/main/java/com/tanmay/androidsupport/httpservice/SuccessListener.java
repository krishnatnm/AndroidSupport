package com.tanmay.androidsupport.httpservice;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by TaNMay on 3/2/2016.
 */
public interface SuccessListener {

    public void onSuccess(JSONObject jObject, JSONArray jArray);

    public void onError(VolleyError vError, String errMsg, int errCode);

}
