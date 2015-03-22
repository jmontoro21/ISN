package com.inftel.isn.request;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

public class RestServiceGet extends AsyncTask<String, Integer, JSONArray> {

    protected JSONArray doInBackground(String... urls) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet g = new HttpGet(urls[0]);
        g.setHeader("Accept", "application/json");
        g.setHeader("Content-type", "application/json");
        JSONArray respJSON = null;
        try {
            HttpResponse resp = httpClient.execute(g);
            String respStr = EntityUtils.toString(resp.getEntity());
            respJSON = new JSONArray(respStr);

        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return respJSON;
    }
}