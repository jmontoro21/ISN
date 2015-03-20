package com.inftel.isn.request;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by José on 20/03/2015.
 */
public class RestServiceGet extends AsyncTask<String, Integer, String> {

    protected String doInBackground(String... urls) {




        HttpClient httpClient = new DefaultHttpClient();

        System.out.println("dir " + urls[0]);


        HttpGet g = new HttpGet(urls[0]);
        g.setHeader("Accept", "application/json");
        g.setHeader("Content-type", "application/json");
        try
        {

            HttpResponse resp = httpClient.execute(g);
            String respStr = EntityUtils.toString(resp.getEntity());

            int i;

            JSONArray respJSON = new JSONArray(respStr);

            if(respJSON.length() == 0)
            {
                System.out.println("inserto dato");
                // otra llamada asincrona
            }

            return null;
           /* for(i=0; i<respJSON.length(); i++){
                JSONObject object = respJSON.getJSONObject(i);
                String title = object.getString("title");
                String entryContent = object.getString("description");
                String lat = object.getString("latitude");
                String lg = object.getString("longitude");
                String im = object.getString("urlimage");
                //datasource.open();
                //datasource.createEntry(title, entryContent, lat, lg, im);
                //datasource.close();
            }*/
        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
        }
        return null;
    }

    protected void onProgressUpdate(Integer... progress){

    }

    protected void onPostExecute(String result) {
        //reloadActivity();
    }
}
