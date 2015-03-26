package com.inftel.isn.request;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.inftel.isn.model.Comment;
import com.inftel.isn.model.Group;
import com.inftel.isn.model.GroupComments;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by loubna on 26/03/2015.
 */
public class DeleteCommentGroupRequest  extends AsyncTask<String, Integer, Void> {

    public static final String IP = "192.168.183.24";
    private Comment comment;
    private String admin;
    private  String name;
    public DeleteCommentGroupRequest(Comment comment, String admin, String name) {
        this.comment=comment;
        this.admin = admin;
        this.name=name;

    }

    protected Void doInBackground(String... urls) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(comment, Comment.class);
            JSONObject groupJson = new JSONObject(json);

            new RestServicePost(groupJson).execute("http://" + IP + ":8080/InftelSocialNetwork-web/webresources/groupcomment/deletecomment?admin" + admin + "/&name/" + name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
