package com.inftel.isn.request;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.inftel.isn.model.Group;

import org.json.JSONException;
import org.json.JSONObject;

public class RestServiceDeleteGroup extends AsyncTask<String, Integer, Void> {

    public static final String IP = "192.168.183.24";
    private Group group;

    public RestServiceDeleteGroup(Group group) {
        this.group = group;
    }

    protected Void doInBackground(String... urls) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(group, Group.class);
            JSONObject groupJson = new JSONObject(json);
            new RestServicePost(groupJson).execute("http://"+IP+":8080/InftelSocialNetwork-web/webresources/group/delete?admin=" + urls[0] + "&"+group.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}