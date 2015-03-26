package com.inftel.isn.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inftel.isn.R;
import com.inftel.isn.model.User;
import com.inftel.isn.request.RestServiceGet;

public class LoginSelectionActivity extends Activity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public static final String IP = "192.168.183.24";
    public static final String USER_KEY = "es.inftel.isn.user.google.id.email";
    public static final String USER_NAME = "es.inftel.isn.user.google.id.name";
    public static final String USER_ID = "es.inftel.isn.user.google.id.id";
    public static final String USER_URL = "es.inftel.isn.user.google.id.url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_selection);
    }

    public void googleLogin(View v){
        Intent intent = new Intent(this, LoginGoogleActivity.class);
        startActivity(intent);
    }

    public void qrLogin(View v){
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(LoginSelectionActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://details?id=" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    //Devolución del programa
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String loginEmail = intent.getStringExtra("SCAN_RESULT");
                try {
                    String respStr = new RestServiceGet().execute("http://"+IP+":8080/InftelSocialNetwork-web/webresources/users/email?email=" + loginEmail).get();
                    //Si esta vacio es porque no coincide con ningun User de la BBDD
                    if(respStr.isEmpty()){
                        Toast data = Toast.makeText(LoginSelectionActivity.this, "Login incorrecto", Toast.LENGTH_SHORT);
                        data.show();
                    } else {
                        Gson gson = new Gson();
                        User user = gson.fromJson(respStr, User.class);
                        SharedPreferences prefs = this.getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
                        SharedPreferences.Editor e = prefs.edit();
                        e.putString(USER_KEY, user.getEmail());
                        e.putString(USER_NAME, user.getName());
                        e.putString(USER_ID, user.getGoogleId());
                        e.putString(USER_URL, user.getImageUrl());
                        e.commit();
                        Intent i = new Intent(this, MenuActivity.class);
                        startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
