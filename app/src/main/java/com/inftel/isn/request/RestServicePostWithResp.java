package com.inftel.isn.request;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class RestServicePostWithResp extends AsyncTask<String, Integer, String> {

    private JSONObject json;
    private ImageButton botonCompartir;

    private Integer position;


    public RestServicePostWithResp(JSONObject json, ImageButton botonCompartir, Integer position) {
        this.json = json;

        this.botonCompartir = botonCompartir;
        this.position = position;
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

            HttpResponse response = httpClient.execute(httpPost);

            String responseBody = EntityUtils.toString(response.getEntity());


           // System.out.println("respuesta222 " + responseBody);

        if(responseBody.compareTo("true") == 0)
        {
            botonCompartir.setTag(position);
            botonCompartir.setVisibility(View.VISIBLE);

        }
                    else
        {
            botonCompartir.setVisibility(View.INVISIBLE);
        }







        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}