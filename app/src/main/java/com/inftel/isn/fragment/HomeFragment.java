package com.inftel.isn.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.inftel.isn.activity.LoginGoogleActivity;
import com.inftel.isn.activity.MenuActivity;
import com.inftel.isn.adapter.PublicsUsersCommentsListAdapter;
import com.inftel.isn.adapter.SimpleArrayAdapter;
import com.inftel.isn.model.Comment;
import com.inftel.isn.model.ProfileComments;
import com.inftel.isn.model.User;
import com.inftel.isn.request.DownloadImageTask;
import com.inftel.isn.request.RestServiceGet;
import com.inftel.isn.request.RestServicePost;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {
    public static final String EMAIL_USER_PROFILE = "es.inftel.isn.user.google.id.name";
    public static final String IP = "192.168.1.117";


    private TextView userName;
    private ImageView imgProfile;
    private String emailProfile;
    private String emailLogin;

    public String getEmailLogin() {
        return emailLogin;
    }

    public void setEmailLogin(String emailLogin) {
        this.emailLogin = emailLogin;
    }

    private ListView listView;
    private PublicsUsersCommentsListAdapter adapter;
    private ProfileComments perfil;
    private ListView eventListView;
    private Activity act;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        eventListView = (ListView) v.findViewById(R.id.itemList);

        act = getActivity();

       // emailLogin =  savedInstanceState.getString("emailGoogle");



        System.out.println("login en fragmen " + emailLogin );



/*
        try {

            final ImageButton cmdOK_PIN = (ImageButton) v.findViewById(R.id.btnDelete);
            cmdOK_PIN.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {


                    try {



       *//* GsonBuilder builder = new GsonBuilder();

// Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        Gson gson = builder.create();
*//*
                        Gson gson = new Gson();


                        String json = gson.toJson(perfil.getComment((int) v.getTag()),Comment.class);


                        json = json.replaceAll("creationDate\":\".*?\"","creationDate\":1426701282429");




                        JSONObject  comenatrio = new JSONObject(json);

                        String formatEmail = emailLogin.replaceAll("\\.", "___");


                        new RestServicePost(comenatrio).execute("http://192.168.1.117:8080/InftelSocialNetwork-web/webresources/profilecomments/deletecomment?userEmail=" + formatEmail);

                        perfil.removecommentsList((int) v.getTag());

                        Toast data = Toast.makeText(getActivity(), "User removed", Toast.LENGTH_LONG);
                        data.show();
                        adapter.notifyDataSetChanged();

                        // actualizo perfil en la bd
                        // eliminando el comentario



                    } catch (Exception e) {
                        e.printStackTrace();
                    }






                }
            });

        } catch (ClassCastException e) {
            e.printStackTrace();
        }*/




        loadProfile();






        /*//Array estatico provisional
        ArrayList objetos = new ArrayList<String>();
        objetos.add("paco");objetos.add("hola");objetos.add("hola");objetos.add("hola");objetos.add("paco");objetos.add("paco");

        SimpleArrayAdapter adapter = new SimpleArrayAdapter(objetos, getActivity());
        eventListView.setAdapter(adapter);*/
        return v;
    }





    // carga el perfil del usuario
    public void loadProfile() {

        SharedPreferences prefs = getActivity().getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);

       // SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

       // emailLogin = sp.getString(LoginGoogleActivity.USER_KEY,"");
        //System.out.println("fhhfhfhfhfhf " + emailLogin);

        // Email del usuario logueado
        if (prefs.contains(LoginGoogleActivity.USER_KEY)) {


           // emailLogin = sp.getString(LoginGoogleActivity.USER_KEY,"");

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
        String respJSON = null;


        try {
            respJSON = new RestServiceGet().execute("http://" +IP+ ":8080/InftelSocialNetwork-web/webresources/profilecomments/email?email=" + formatEmail).get();

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

                    object = nuevo.getJSONObject(i);


                    //perfil = gson.fromJson(respJSON, ProfileComments.class);
                }

            }

            perfil = gson.fromJson(respJSON, ProfileComments.class);

            System.out.println("gson " + respJSON);


            Collections.reverse(perfil.getCommentsList());

            perfil.getCommentsList().removeAll(Collections.singleton(null));


            System.out.println("perfil " + perfil.getUserEmail());
            System.out.println("email " + emailLogin);

            System.out.println("lista " + perfil.getCommentsList().get(0).getText());

            adapter = new PublicsUsersCommentsListAdapter(perfil, emailLogin, perfil.getCommentsList(),act);
            System.out.println("zzsz" + adapter.toString());

            eventListView.setAdapter(adapter);



        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        }
    }


    public void deleteItem(View v) {


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


            new RestServicePost(comenatrio).execute("http://" +IP+ ":8080/InftelSocialNetwork-web/webresources/profilecomments/deletecomment?userEmail=" + formatEmail);

            perfil.removecommentsList((int) v.getTag());

            Toast data = Toast.makeText(getActivity(), "User removed", Toast.LENGTH_LONG);
            data.show();
            adapter.notifyDataSetChanged();

            // actualizo perfil en la bd
            // eliminando el comentario



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
