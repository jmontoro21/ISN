package com.inftel.isn.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.List;

/**
 * Created by Alfredo on 24/03/2015.
 */
public class FollowedActivity extends Activity {


    private ListView listView;
    private String emailLogin;
    private User user;

    private ArrayList<User> users = new ArrayList<>();

    private UsersAddedListAdapter adapter;

    private List<Following> requestFollowedBBDD(){
        try {

            String respStr = new RestServiceGet().execute("http://192.168.183.24:8080/InftelSocialNetwork-web/webresources/following/followed?email="+emailLogin).get();///email/"+user.getEmail()
            System.out.println("respuesta es: "+ respStr);
            if(!respStr.isEmpty() && (respStr != null)){
                List<Following> seguidores = new ArrayList<>();
                JSONArray respJSON = new JSONArray(respStr);
                Gson gson = new Gson();

                if (respJSON.length() != 0) {
                    for (int i = 0; i < respJSON.length(); i++) {

                        JSONObject object = respJSON.getJSONObject(i);
                        seguidores.add(gson.fromJson(object.toString(), Following.class));
                    }
                }
                return seguidores;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followed);

        SharedPreferences prefs = this.getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        if (prefs.contains(LoginGoogleActivity.USER_KEY)) {
            emailLogin = prefs.getString(LoginGoogleActivity.USER_KEY, "");
        }

        user = getIntent().getExtras().getParcelable("user");
        List<Following> followings = requestFollowedBBDD();
        listView = (ListView)findViewById(R.id.list_followed);

        if(!followings.isEmpty()){
            listView.setAdapter(new FollowListAdapter(followings, this));
        }



    }
}
