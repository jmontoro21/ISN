package com.inftel.isn.request;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inftel.isn.model.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Alfredo on 20/03/2015.
 */


public class SearchUserAsyncTask extends AsyncTask<String, Void, ArrayList<User>> {

    private Activity activity;
    private ListView lvDetail;
    private User user;
    private TextView tvNoResult;

    public SearchUserAsyncTask(Activity activity, ListView lvDetail, User user,  TextView tvNoResult){
        this.lvDetail = lvDetail;
        this.activity = activity;
        this.user = user;
        this.tvNoResult = tvNoResult;
    }

    @Override
    protected ArrayList<User> doInBackground(String... params){

        Reader reader =  GET(params[0]);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<User>>() {}.getType();

        return gson.fromJson(reader, type);

    }
    public static Reader GET(String params){
        InputStream inputStream = null;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(params);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpGet);
            inputStream = httpResponse.getEntity().getContent();
        } catch (Exception e) {
            Log.e(SearchUserAsyncTask.class.getSimpleName(), "InputStream", e);
        }

        return new InputStreamReader(inputStream);
    }

    @Override
    protected void onPostExecute(ArrayList<User> listUser) {

    }


}


