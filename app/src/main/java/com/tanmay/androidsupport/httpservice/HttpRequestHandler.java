package com.tanmay.androidsupport.httpservice;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by TaNMay on 3/2/2016.
 */
public class HttpRequestHandler {

    public static String TAG = "HttpRequestHandler ==>";

    public static void makeJsonObjReq(final JSONObject jObject, String url, int method,
                                      final SuccessListener listener, final String token) {
        Log.d(TAG, "Object: " + jObject);
        Log.d(TAG, "URL: " + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(method, url, jObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Response: " + response.toString());
                listener.onSuccess(response, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "VolleyError: " + error.getMessage());
                String err, errMsg;
                int errCode = 0;
                errMsg = "Error!";

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    errCode = response.statusCode;
                    if (errCode == 500) {
                        errMsg = "Error #500: Server Error!";
                    } else {
                        errMsg = "Error #" + errCode;
                    }
                } else if (error instanceof NoConnectionError) {
                    errMsg = "Error: No Internet Connection!";
                }
                listener.onError(error, errMsg, errCode);
            }
        }) {
            public java.util.Map<String, String> getHeaders()
                    throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(15000, 10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG);
    }

    public static void makeJsonArrayRequest(String url, final SuccessListener listener, final String token) {
        Log.d(TAG, "URL: " + url);
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "Response: " + response.toString());
                listener.onSuccess(null, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "VolleyError: " + error.getMessage());
                String err, errMsg;
                int errCode = 0;
                errMsg = "Error!";

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    errCode = response.statusCode;
                    if (errCode == 500) {
                        errMsg = "Error #500: Server Error!";
                    } else {
                        errMsg = "Error #" + errCode;
                    }
                } else if (error instanceof NoConnectionError) {
                    errMsg = "Error: No Internet Connection!";
                }
                listener.onError(error, errMsg, errCode);
            }
        }) {
            public java.util.Map<String, String> getHeaders()
                    throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", token);
                return headers;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(15000, 10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(req);
    }

    public static void makeCustomRequest(String url, JSONObject jObject, final SuccessListener listener, final int method, final String token) {
        CustomRequest req = new CustomRequest(method,
                url, jObject,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        Log.d(TAG, "Response: " + responseArray.toString());
                        listener.onSuccess(null, responseArray);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "VolleyError: " + error.getMessage());
                String err, errMsg;
                int errCode = 0;
                errMsg = "Error!";

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    errCode = response.statusCode;
                    if (errCode == 500) {
                        errMsg = "Error #500: Server Error!";
                    } else {
                        errMsg = "Error #" + errCode;
                    }
                } else if (error instanceof NoConnectionError) {
                    errMsg = "Error: No Internet Connection!";
                }
                listener.onError(error, errMsg, errCode);
            }
        }) {
            public java.util.Map<String, String> getHeaders()
                    throws com.android.volley.AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", token);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(req);
    }
}
