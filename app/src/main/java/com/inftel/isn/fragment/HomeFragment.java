package com.inftel.isn.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.inftel.isn.R;
import com.inftel.isn.activity.LoginGoogleActivity;
import com.inftel.isn.adapter.PublicsUsersCommentsListAdapter;
import com.inftel.isn.model.Comment;
import com.inftel.isn.model.ProfileComments;
import com.inftel.isn.request.RestServiceGet;
import com.inftel.isn.request.RestServicePost;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;

public class HomeFragment extends Fragment {
    public static final String IP = "192.168.183.24";

    private String emailLogin;

    public void setEmailLogin(String emailLogin) {
        this.emailLogin = emailLogin;
    }

    private PublicsUsersCommentsListAdapter adapter;
    private ProfileComments perfil;
    private ListView eventListView;
    private Activity act;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        eventListView = (ListView) v.findViewById(R.id.itemList);

        act = getActivity();

        loadProfile();

        return v;
    }



    // carga el perfil del usuario
    public void loadProfile() {

        SharedPreferences prefs = getActivity().getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);

        // Email del usuario logueado
        if (prefs.contains(LoginGoogleActivity.USER_KEY)) {
            emailLogin = prefs.getString(LoginGoogleActivity.USER_KEY, "");
            System.out.println("fhhfhfhfhfhf");
        }
        // extraer lista de comentarios
        loadCommentsList(emailLogin);

    }


    // carga la lista de comentarios
    public void loadCommentsList(String email) {

        if(email != null && !email.isEmpty())
        {

            String formatEmail = email.replaceAll("\\.", "___");

            try {
                String respJSON = new RestServiceGet().execute("http://" +IP+ ":8080/InftelSocialNetwork-web/webresources/profilecomments/email?email=" + formatEmail).get();

                if(respJSON.isEmpty()){

                } else {
                    // Creates the json object which will manage the information received
                    GsonBuilder builder = new GsonBuilder();

                    // Register an adapter to manage the date types as long values
                    builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            return new Date(json.getAsJsonPrimitive().getAsLong());
                        }
                    });

                    Gson gson = builder.create();


                    System.out.println("------comentario--------- " + respJSON);

                    perfil = gson.fromJson(respJSON, ProfileComments.class);

                    Collections.reverse(perfil.getCommentsList());

                    perfil.getCommentsList().removeAll(Collections.singleton(null));

                    adapter = new PublicsUsersCommentsListAdapter(perfil, emailLogin, perfil.getCommentsList(), act);

                    eventListView.setAdapter(adapter);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void deleteItem(View v) {

        try {
            Gson gson = new Gson();

            String json = gson.toJson(perfil.getComment((int) v.getTag()),Comment.class);

            json = json.replaceAll("creationDate\":\".*?\"","creationDate\":1426701282429");

            JSONObject comentario = new JSONObject(json);

            String formatEmail = emailLogin.replaceAll("\\.", "___");

            new RestServicePost(comentario).execute("http://" +IP+ ":8080/InftelSocialNetwork-web/webresources/profilecomments/deletecomment?userEmail=" + formatEmail);

            perfil.removecommentsList((int) v.getTag());

            Toast data = Toast.makeText(getActivity(), "User removed", Toast.LENGTH_LONG);
            data.show();
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
