package com.inftel.isn.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.inftel.isn.R;
import com.inftel.isn.adapter.PublicsUsersCommentsListAdapter;
import com.inftel.isn.adapter.UsersAddedListAdapter;
import com.inftel.isn.model.Comment;
import com.inftel.isn.model.ProfileComments;
import com.inftel.isn.model.User;
import com.inftel.isn.request.DownloadImageTask;
import com.inftel.isn.request.RestServiceGet;
import com.inftel.isn.request.RestServicePost;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class ListPublicCommentActivity extends Activity {

    public static final String EMAIL_USER_PROFILE = "es.inftel.isn.user.google.id.name";


    private TextView userName;
    private ImageView imgProfile;
    private String emailProfile;
    private String emailLogin;

    private ListView listView;
    private PublicsUsersCommentsListAdapter adapter;
    private ProfileComments perfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_public_comment);
        loadProfile();
    }


    // carga el perfil del usuario
    public void loadProfile() {
        SharedPreferences prefs = this.getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);

        Intent intent = getIntent();

        // Email del perfil a cargar
        if (intent.getStringExtra(EMAIL_USER_PROFILE) != null) {
            emailProfile = intent.getStringExtra(EMAIL_USER_PROFILE);
        } else {
            emailProfile = "";
        }

        // Email del usuario logueado
        if (prefs.contains(LoginGoogleActivity.USER_KEY)) {
            emailLogin = prefs.getString(LoginGoogleActivity.USER_KEY, "");
        }


        userName = (TextView) this.findViewById(R.id.PublicCommentNameProfile);
        imgProfile = (ImageView) this.findViewById(R.id.PublicCommentImgProfile);


        // Compruebo si el perfil es del usuario logueado o de otro
        if ((emailProfile.isEmpty()) || (emailProfile.compareTo(emailLogin) == 0)) {
            // perfil usuario logueado
            if (prefs.contains(LoginGoogleActivity.USER_NAME)) {
                userName.setText(prefs.getString(LoginGoogleActivity.USER_NAME, ""));
            }
            if (prefs.contains(LoginGoogleActivity.USER_URL)) {
                new DownloadImageTask(imgProfile).execute(prefs.getString(LoginGoogleActivity.USER_URL, ""));
            }
            // extraer lista de comentarios
            loadCommentsList(emailLogin);

        } else {
            try {


                String formatEmail = emailProfile.replaceAll("\\.", "___");
                String userGet = new RestServiceGet().execute("http://192.168.1.123:8080/InftelSocialNetwork-web/webresources/users/" + formatEmail).get();

                Gson gson = new Gson();
                User perfil = gson.fromJson(userGet, User.class);

                userName.setText(perfil.getName());
                new DownloadImageTask(imgProfile).execute(perfil.getImageUrl(), "");


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();


                loadCommentsList(emailProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    // carga la lista de comentarios
    public void loadCommentsList(String email) {

        String formatEmail = email.replaceAll("\\.", "___");
        String respJSON = null;


        try {
            respJSON = new RestServiceGet().execute("http://192.168.1.123:8080/InftelSocialNetwork-web/webresources/profilecomments/email?email=" + formatEmail).get();




        System.out.println("el json esss " + respJSON);


            JSONObject json = null;

                json = new JSONObject(respJSON);

            JSONArray nuevo = null;

                nuevo = json.getJSONArray("commentsList");


            //Gson gson = new Gson();




            // Creates the json object which will manage the information received
            GsonBuilder builder = new GsonBuilder();

            // Register an adapter to manage the date types as long values
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });

            Gson gson = builder.create();






            if (nuevo.length() != 0) {
                for (int i = 0; i < nuevo.length(); i++) {
                    JSONObject object = null;

                        object = nuevo.getJSONObject(i);


                   //perfil = gson.fromJson(respJSON, ProfileComments.class);
                }

            }
                 perfil = gson.fromJson(respJSON, ProfileComments.class);

                System.out.println("email " + email);
                System.out.println("perfil " + perfil.getUserEmail());




           Collections.reverse(perfil.getCommentsList());

               perfil.getCommentsList().removeAll(Collections.singleton(null));

                listView = (ListView) findViewById(R.id.itemList);
                adapter = new PublicsUsersCommentsListAdapter(perfil, emailLogin, perfil.getCommentsList(), this);
                System.out.println("zzsz" + adapter.toString());
                listView.setAdapter(adapter);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

            }


    public void deleteItems(View v) {


        try {



       /* GsonBuilder builder = new GsonBuilder();

// Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        Gson gson = builder.create();
*/
       Gson gson = new Gson();


        String json = gson.toJson(perfil.getComment((int) v.getTag()),Comment.class);


            json = json.replaceAll("creationDate\":\".*?\"","creationDate\":1426701282429");




            JSONObject  comenatrio = new JSONObject(json);

        String formatEmail = emailLogin.replaceAll("\\.", "___");


            new RestServicePost(comenatrio).execute("http://192.168.1.117:8080/InftelSocialNetwork-web/webresources/profilecomments/deletecomment?userEmail=" + formatEmail);

            perfil.removecommentsList((int) v.getTag());

        Toast data = Toast.makeText(this, "User removed", Toast.LENGTH_LONG);
        data.show();
        adapter.notifyDataSetChanged();

        // actualizo perfil en la bd
           // eliminando el comentario

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        }

