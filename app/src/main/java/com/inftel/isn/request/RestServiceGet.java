package com.inftel.isn.request;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RestServiceGet extends AsyncTask<String, Integer, String> {

    protected String doInBackground(String... urls) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet g = new HttpGet(urls[0]);
        g.setHeader("Accept", "application/json");
        g.setHeader("Content-type", "application/json");
        String respStr = "";
        try {
            HttpResponse resp = httpClient.execute(g);
            respStr = EntityUtils.toString(resp.getEntity());


        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return respStr;
    }
}