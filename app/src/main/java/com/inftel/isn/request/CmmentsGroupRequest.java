package com.inftel.isn.request;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.inftel.isn.activity.CommentGroupActivity;
import com.inftel.isn.model.GroupComments;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by loubna on 23/03/2015.
 */
public class CmmentsGroupRequest extends AsyncTask<String, Integer, GroupComments> {

     private CommentGroupActivity source;
     private String name;
    private  String admin;

    public CmmentsGroupRequest(CommentGroupActivity source, String admin, String name) {
        this.source = source;
        this.name= name;
        this.admin= admin;
    }
    @Override
    protected  GroupComments doInBackground(String... urls) {


        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = null;
        try {
            String url = new URL("http://192.168.183.24:8080/InftelSocialNetwork-web/webresources/groupcomment/admin/"+ admin + "/name/" + name).toString();
            Log.i("URL", url);
            httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //HttpGet httpGet = new HttpGet("http://192.168.183.61:8080/InftelSocialNetwork-web/webresources/group/email/yo");

        try {
            // Execute HTTP Get Request
            HttpResponse response = httpclient.execute(httpGet);
            String json = EntityUtils.toString(response.getEntity(), "UTF-8");
            Log.d("JSON", json);

            // Creates the json object which will manage the information received
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });

            Gson gson = builder.create();
            //Gson gson = new Gson();
            Type listType = new TypeToken<GroupComments>(){}.getType();
            GroupComments comments = gson.fromJson(json, listType);
            return comments;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(),e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(GroupComments object) {
        (source).loadListView(object);
    }
}
