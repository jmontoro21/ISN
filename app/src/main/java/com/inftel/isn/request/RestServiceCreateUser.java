package com.inftel.isn.request;

import android.os.AsyncTask;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by inftel10 on 16/3/15.
 */
public class RestServiceCreateUser extends AsyncTask<String, Integer, String> {

    private JSONObject json;

    public RestServiceCreateUser(JSONObject json) {
        this.json = json;
    }

    protected String doInBackground(String... urls) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(urls[0]);
        httpPost.setHeader("content-type", "application/json");
        StringEntity entity;
        try {
            entity = new StringEntity(json.toString());
            httpPost.setEntity(entity);
            httpClient.execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(Integer... progress){        }

    protected void onPostExecute(String result) {

    }
}
