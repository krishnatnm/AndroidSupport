package com.tanmay.androidsupport.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by TaNMay on 3/14/2016.
 */
public interface ResponseListener {
    void gotObjectResponse(JSONObject jObject);

    void gotArrayResponse(JSONArray jArray);

    void errorResponse();
}
