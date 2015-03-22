package com.inftel.isn.request;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by José on 20/03/2015.
 */
public class RestServiceGetUserData extends AsyncTask<String, Integer, String> {

    private TextView name;

    public RestServiceGetUserData(TextView name) {
        this.name = name;
    }

    protected String doInBackground(String... urls) {

        HttpClient httpClient = new DefaultHttpClient();

        System.out.println("dir " + urls[0]);

        String respuesta = "";

        HttpGet g = new HttpGet(urls[0]);
        g.setHeader("Accept", "application/json");
        g.setHeader("Content-type", "application/json");
        try {

            HttpResponse resp = httpClient.execute(g);
            String respStr = EntityUtils.toString(resp.getEntity());

            int i;

            JSONArray respJSON = new JSONArray(respStr);

            if (respJSON.length() != 0) {
                for (i = 0; i < respJSON.length(); i++) {
                    JSONObject object = respJSON.getJSONObject(i);
                    respuesta = object.getString("name");
                }
            }
        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return respuesta;
    }
    protected void onPostExecute(String result) {
        //reloadActivity();
        name.setText(result);
    }
}
