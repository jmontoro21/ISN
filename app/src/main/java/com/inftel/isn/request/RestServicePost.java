package com.inftel.isn.request;

import android.os.AsyncTask;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

public class RestServicePost extends AsyncTask<String, Integer, String> {

    private JSONObject json;

    public RestServicePost(JSONObject json) {
        this.json = json;
    }

    protected String doInBackground(String... urls) {
        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(urls[0]);
            StringEntity entity = new StringEntity(json.toString(), "UTF-8");
            entity.setContentType("application/json;charset=UTF-8");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));

            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");

            httpClient.execute(httpPost);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}