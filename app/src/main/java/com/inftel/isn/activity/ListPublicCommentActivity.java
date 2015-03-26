package com.inftel.isn.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.inftel.isn.R;
import com.inftel.isn.adapter.PublicsUsersCommentsListAdapter;
import com.inftel.isn.adapter.UsersAddedListAdapter;
import com.inftel.isn.model.Comment;
import com.inftel.isn.model.Following;
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

    public static final String EMAIL_USER_PROFILE = "es.inftel.isn.user.google.id.nameUser";
    public static final String IP = "192.168.1.123";
    private TextView userName;
    private ImageView imgProfile;
    private  String emailProfile;
    private  String emailLogin;
    private ListView listView;
    private PublicsUsersCommentsListAdapter adapter;
    private ProfileComments perfil;
    private ImageButton btnSeguir;
    private ImageButton btnDSeguir;
    private ImageButton compartir;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_public_comment);

        loadProfile();

    }


    // carga el perfil del usuario
    public void loadProfile() {
        SharedPreferences prefs = this.getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        Intent intent = this.getIntent();

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
        if ((emailProfile.isEmpty())) {
            // perfil usuario logueado
            if (prefs.contains(LoginGoogleActivity.USER_NAME)) {
                userName.setText(prefs.getString(LoginGoogleActivity.USER_NAME, ""));
            }
            if (prefs.contains(LoginGoogleActivity.USER_URL)) {
                new DownloadImageTask(imgProfile).execute(prefs.getString(LoginGoogleActivity.USER_URL, ""));
            }

            // extraer lista de comentarios
            loadCommentsList(emailLogin);

        }else if(emailProfile.compareTo(emailLogin) == 0 )
        {
            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
        }
        else {
            try {

                String formatEmail = emailProfile.replaceAll("\\.", "___");
                String userGet = new RestServiceGet().execute("http://"+IP+":8080/InftelSocialNetwork-web/webresources/users/" + formatEmail).get();

                if(!userGet.isEmpty() && userGet != null) {

                    Gson gson = new Gson();
                    JSONObject json = null;
                    JSONArray userArray = new  JSONArray(userGet);

                    for(int i=0; i<userArray.length();i++ )
                    {
                       json = new  JSONObject(userArray.get(i).toString());

                    }

                    User perfil = gson.fromJson(json.toString(), User.class);


                    userName.setText(perfil.getName());
                    new DownloadImageTask(imgProfile).execute(perfil.getImageUrl(), "");

                    loadBotonSeguir(emailLogin, emailProfile);
                    loadCommentsList(emailProfile);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
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
            respJSON = new RestServiceGet().execute("http://"+IP+":8080/InftelSocialNetwork-web/webresources/profilecomments/email?email=" + formatEmail).get();

            JSONObject json = null;

            json = new JSONObject(respJSON);

            JSONArray nuevo = null;

            nuevo = json.getJSONArray("commentsList");

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
                }
            }
            perfil = gson.fromJson(respJSON, ProfileComments.class);
            Collections.reverse(perfil.getCommentsList());
            perfil.getCommentsList().removeAll(Collections.singleton(null));
            listView = (ListView) findViewById(R.id.itemList);
            adapter = new PublicsUsersCommentsListAdapter(perfil, emailLogin, perfil.getCommentsList(), this);

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

    public void loadBotonSeguir(String emailLogin,String emailProfile)
    {
        try {
            emailLogin = emailLogin.replaceAll("\\.", "___");
            emailProfile = emailProfile.replaceAll("\\.", "___");

            String userGet = new RestServiceGet().execute("http://"+IP+":8080/InftelSocialNetwork-web/webresources/following/isFollow?followerEmail=" + emailLogin + "&followingUserEmail=" + emailProfile).get();

            if((userGet != null) &&(!userGet.isEmpty()) ) {

                // dejar de
                btnSeguir = (ImageButton) this.findViewById(R.id.Seguir);
                btnSeguir.setVisibility(View.INVISIBLE);

                btnDSeguir = (ImageButton) this.findViewById(R.id.DejarDeSeguir);
                btnDSeguir.setVisibility(View.VISIBLE);
            }
            else
            {
                //seguir

                btnSeguir = (ImageButton) this.findViewById(R.id.Seguir);
                btnSeguir.setVisibility(View.VISIBLE);

                btnDSeguir = (ImageButton) this.findViewById(R.id.DejarDeSeguir);
                btnDSeguir.setVisibility(View.INVISIBLE);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteItems(View v) {
        try {

            Gson gson = new Gson();
            String json = gson.toJson(perfil.getComment((int) v.getTag()),Comment.class);
            json = json.replaceAll("creationDate\":\".*?\"","creationDate\":1426701282429");
            JSONObject  comenatrio = new JSONObject(json);
            String formatEmail = emailLogin.replaceAll("\\.", "___");

            new RestServicePost(comenatrio).execute("http://"+IP+":8080/InftelSocialNetwork-web/webresources/profilecomments/deletecomment?userEmail=" + formatEmail);
            perfil.removecommentsList((int) v.getTag());

            Toast data = Toast.makeText(this, "User removed", Toast.LENGTH_LONG);
            data.show();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dejardeSeguir(View v)
    {
        try {

            String emaillogin = emailLogin.replaceAll("\\.", "___");
            String emaolprofile = emailProfile.replaceAll("\\.", "___");

            new RestServiceGet().execute("http://"+IP+":8080/InftelSocialNetwork-web/webresources/following/unFollow?followerEmail=" + emaillogin + "&followingUserEmail=" + emaolprofile);

           btnSeguir.setVisibility(View.VISIBLE);
           btnDSeguir.setVisibility(View.INVISIBLE);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void Seguir(View v)
    {
        try {
            String emaillogin = emailLogin.replaceAll("\\.", "___");
            String emaolprofile = emailProfile.replaceAll("\\.", "___");

            new RestServiceGet().execute("http://"+IP+":8080/InftelSocialNetwork-web/webresources/following/follow?followerEmail=" + emaillogin + "&followingUserEmail=" + emaolprofile);

            btnSeguir.setVisibility(View.INVISIBLE);
           btnDSeguir.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void shareItems(View v)
    {
        ImageButton botonCompartir =  (ImageButton) v.findViewById(R.id.btnShare);
        botonCompartir.getTag((int) v.getTag());
        botonCompartir.setVisibility(View.INVISIBLE);
        Gson gson = new Gson();

        String json = gson.toJson(perfil.getComment((int) v.getTag()),Comment.class);
        json = json.replaceAll("creationDate\":\".*?\"","creationDate\":1426701282429");
        JSONObject  comenatrio = null;
        try {
            comenatrio = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String formatEmail = emailLogin.replaceAll("\\.", "___");
        new RestServicePost(comenatrio).execute("http://"+IP+":8080/InftelSocialNetwork-web/webresources/profilecomments/share?userEmail=" + formatEmail);
        Toast data = Toast.makeText(this, "Comment Share", Toast.LENGTH_LONG);
        data.show();
    }
}

