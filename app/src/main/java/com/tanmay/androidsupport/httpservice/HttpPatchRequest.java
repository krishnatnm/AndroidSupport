package com.tanmay.androidsupport.httpservice;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by TaNMay on 3/3/2016.
 */
public class HttpPatchRequest {

    public static String TAG = "HttpPatchRequest ==>";

    public static void sendPatch(String url, JSONObject jObject, String token, String etag) {

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPatch httpPatch = new HttpPatch(url);
            StringEntity se = new StringEntity(jObject.toString());

            Log.d(TAG, "URL: " + url);
            Log.d(TAG, "Etag: " + etag);
            Log.d(TAG, "StringEntity: " + jObject.toString());

            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8"));
            httpPatch.setEntity(se);
            httpPatch.setHeader("Content-Type", "application/json; charset=utf-8");

            httpPatch.setHeader("Authorization", token);
            httpPatch.setHeader("If-Match", etag);

            // Making HTTP Request
            HttpResponse response = httpClient.execute(httpPatch);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // EntityUtils to get the reponse content
                String content = EntityUtils.toString(entity);
                System.out.println("Content de entity utils ==> \n" + content);
            }
            // writing response to log
            Log.d(TAG, "Http Response: " + response.toString());
        } catch (ClientProtocolException e) {
            // writing exception to log
            e.printStackTrace();
        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }

    }
}