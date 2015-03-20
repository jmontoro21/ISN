package com.inftel.isn.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.inftel.isn.R;
import com.inftel.isn.request.DownloadImageTask;


public class ListPublicCommentActivity extends Activity {

    public static final String EMAIL_USER_PROFILE = "es.inftel.isn.user.google.id.name";


    private TextView userName;
    private ImageView imgProfile;
    private String emailProfile;
    private String emailLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_public_comment);

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
        } else {
            // otro perfil
            // leer datos de usuario de la bd



        }

        //cargar lista de comentarios con fotos, fexa...

    }
}
