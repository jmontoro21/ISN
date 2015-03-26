package com.inftel.isn.request;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inftel.isn.fragment.GroupFragment;
import com.inftel.isn.model.Group;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

/**
 * Created by loubna on 23/03/2015.
 */
public class GroupsRequest  extends AsyncTask<String, Integer, List<Group>> {
    private GroupFragment source;


    public GroupsRequest(GroupFragment source) {
        this.source = source;
        ;
    }
    @Override
    protected List<Group> doInBackground(String... urls) {


        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(new URL("http://192.168.183.24:8080/InftelSocialNetwork-web/webresources/group/email/"+ source.getEmail()).toString());
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            // Execute HTTP Get Request
            HttpResponse response = httpclient.execute(httpGet);
            String json = EntityUtils.toString(response.getEntity(), "UTF-8");
            Log.d("JSON", json);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Group>>(){}.getType();
            List<Group> groups = gson.fromJson(json, listType);
            return groups;
        } catch (Exception e) {
            Log.e("Error", e.getMessage(),e);
        }
        return null;
    }

    @Override
    public void onProgressUpdate(Integer... progress) {
    }

    @Override
    protected void onPostExecute(List<Group> object) {
        (source).loadListView(object);
    }
}
