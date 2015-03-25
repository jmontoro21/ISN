package com.inftel.isn.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;
import com.inftel.isn.R;
import com.inftel.isn.adapter.FollowListAdapter;
import com.inftel.isn.adapter.UsersAddedListAdapter;
import com.inftel.isn.model.Following;
import com.inftel.isn.model.User;
import com.inftel.isn.request.RestServiceGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alfredo on 24/03/2015.
 */
public class FollowerActivity extends Activity {


    private ListView listView;
    private User user;

//Gente que sigo
    private ArrayList<User> users = new ArrayList<>();

    private UsersAddedListAdapter adapter;

    private Following requestFollowerBBDD(){
        try {
            String respStr = new RestServiceGet().execute("http://192.168.183.24:8080/InftelSocialNetwork-web/webresources/following/"+user.getEmail()).get();
            Following siguiendo = new Following();
            JSONArray respJSON = new JSONArray(respStr);
            Gson gson = new Gson();
            if (respJSON.length() != 0) {
                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject object = respJSON.getJSONObject(i);
                    siguiendo.add(gson.fromJson(object.toString(), User.class));
                }
            }
            return siguiendo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followed);
        user = getIntent().getExtras().getParcelable("user");
        Following followings = requestFollowerBBDD();
        listView = (ListView)findViewById(R.id.list_followed);

        listView.setAdapter(new FollowListAdapter(followings.getFollowing(), this));
    }
}
