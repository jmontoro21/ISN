package com.inftel.isn.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.inftel.isn.R;
import com.inftel.isn.adapter.FollowerListAdapter;
import com.inftel.isn.adapter.UsersAddedListAdapter;
import com.inftel.isn.model.Following;
import com.inftel.isn.model.User;
import com.inftel.isn.request.RestServiceGet;

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
    private Following followings;
    Following siguiendo = new Following();

//Gente que sigo
    private ArrayList<User> users = new ArrayList<>();

    private UsersAddedListAdapter adapter;

    private Following requestFollowerBBDD() {

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

        followings = requestFollowerBBDD();
        listView = (ListView)findViewById(R.id.list_follower);

            listView.setAdapter(new FollowerListAdapter( followings.getFollowing(), this));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
                {
                    Intent i = new Intent(getApplicationContext(), ListPublicCommentActivity.class);
                    i.putExtra(ListPublicCommentActivity.EMAIL_USER_PROFILE, siguiendo.getUser().getEmail());
                    startActivity(i);
                }
            });
    }
}
