package com.inftel.isn.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;
import com.inftel.isn.R;
import com.inftel.isn.adapter.FollowListAdapter;
import com.inftel.isn.adapter.FollowerListAdapter;
import com.inftel.isn.adapter.UsersAddedListAdapter;
import com.inftel.isn.model.Following;
import com.inftel.isn.model.User;
import com.inftel.isn.request.RestServiceGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Alfredo on 24/03/2015.
 */
public class FollowerActivity extends Activity {


    private ListView listView;
    private String emailLogin;
    private User user;

//Gente que sigo
    private ArrayList<User> users = new ArrayList<>();

    private UsersAddedListAdapter adapter;

    private Following requestFollowerBBDD() {
        Following siguiendo = new Following();
        try {
            String respStr = new RestServiceGet().execute("http://192.168.183.24:8080/InftelSocialNetwork-web/webresources/following/user?email=" + emailLogin).get();

            JSONObject respJSON = new JSONObject(respStr);
            Gson gson = new Gson();
            siguiendo = gson.fromJson(respJSON.toString(), Following.class);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return siguiendo;
    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        SharedPreferences prefs = this.getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        if (prefs.contains(LoginGoogleActivity.USER_KEY)) {
            emailLogin = prefs.getString(LoginGoogleActivity.USER_KEY, "");
        }

        Following followings = requestFollowerBBDD();
        listView = (ListView)findViewById(R.id.list_follower);

            listView.setAdapter(new FollowerListAdapter( followings.getFollowing(), this));

    }
}
