package com.inftel.isn.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableLayout;

import com.google.gson.Gson;
import com.inftel.isn.R;
import com.inftel.isn.model.Comment;
import com.inftel.isn.model.Group;
import com.inftel.isn.model.User;
import com.inftel.isn.request.DownloadImageTask;
import com.inftel.isn.request.RestServiceGet;
import com.inftel.isn.request.RestServicePost;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateCommentActivity extends Activity {
    private final static String EMPTY_CONTENT_ALERT = "Post entry is too short.";
    private final static String CANT_POST_ALERT = "Can't post!";
    private final static String INVALID_IMAGE_URL = "Image URL invalid";
    private final static String INVALID_YOUTUBE = "Invalid Youtube link";
    private final static String NOT_AN_IMAGE = "URL is not a valid image";
    private final static String CREATE_PROFILE_POST_URL = "http://192.168.183.24:8080/InftelSocialNetwork-web/webresources/profilecomments/insert?userEmail=";
    public final static String COMMENT_TYPE = "com.inftel.isn.activity.type.blablabla";
    private final static String CREATE_NOTA_URL = "http://192.168.183.24:8080/InftelSocialNetwork-web/webresources/privatecomment/insert?userEmail=";
    private final static String CREATE_COMMENT_FOR_GROUP = "http://192.168.183.24:8080/InftelSocialNetwork-web/webresources/groupcomment/insertcomment";
    private final static String USER_EMAIL = "http://192.168.183.24:8080/InftelSocialNetwork-web/webresources/users/email?email=";

    private ImageView addImageView;
    private ImageView youtubeImage;
    private String image = "";
    private String youtube = "";
    private Intent intent;
    private Switch mySwitch;
    private SharedPreferences prefs;
    private String emailLogin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);
        mySwitch = (Switch) findViewById(R.id.switch1);
        setSwitchStatus();
        addImageView = (ImageView) findViewById(R.id.addImageToComment);
        androidKeyboard();
        prefs = this.getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        emailLogin = prefs.getString(LoginGoogleActivity.USER_KEY, "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    public void doPost() {
        String tipo = intent.getStringExtra(COMMENT_TYPE);
        EditText content = (EditText) findViewById(R.id.postContent);
        String entryContent = content.getText().toString();
        Comment comment = new Comment();
        if (!entryContent.isEmpty() && entryContent.length() > 5) {
            //save post to database via async REST (on postExecute reload postList)
            Log.i("db", "user <USUARIO> saving post content:\n" + entryContent);

            User perfil = null;

            try {

                String formatEmail = emailLogin.replaceAll("\\.", "___");
                String userGet = new RestServiceGet().execute(USER_EMAIL + formatEmail).get();

                JSONObject json = new JSONObject(userGet);

                Gson gson = new Gson();

                perfil = gson.fromJson(json.toString(), User.class);


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            comment.setText(entryContent);
            comment.setImageUrl("");
            comment.setAuthor(perfil);
            comment.setVideoUrl("");
            if (!image.isEmpty()) {
                comment.setImageUrl(image);
            }
            if (!youtube.isEmpty()) {
                comment.setVideoUrl(youtube);
            }
        } else {
            //show alert dialog
            showDialog(CANT_POST_ALERT, EMPTY_CONTENT_ALERT);
        }

        Gson gsonComment = new Gson();
        String jsonCommentAsString = gsonComment.toJson(comment, Comment.class);
        JSONObject jsoncomment = null;

        if (tipo.equals("publico")) {
            try {
                jsoncomment = new JSONObject(jsonCommentAsString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("nc", "profile");
            String formatEmail = emailLogin.replaceAll("\\.", "___");
            new RestServicePost(jsoncomment).execute(CREATE_PROFILE_POST_URL + formatEmail);
        } else if (tipo.equals("grupo")) {
            Log.i("nc", "grupo");
            Intent i = getIntent();
            Group g;
            g = i.getParcelableExtra("GRUPO");
            String nombreAdmin = g.getAdmin();
            String nombreGrupo = g.getName();
            try {
                jsoncomment = new JSONObject(jsonCommentAsString);

            Log.i("nc", "profile");
            nombreAdmin = nombreAdmin.replaceAll("\\.", "___");

            new RestServicePost(jsoncomment).execute(CREATE_COMMENT_FOR_GROUP + "?admin=" + nombreAdmin +"&name=" + nombreGrupo );
            } catch (Exception e) {

                e.printStackTrace();
            }


        } else if (tipo.equals("nota")) {
            try {
                jsoncomment = new JSONObject(jsonCommentAsString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("nc", "private-nota");
            //llamda rest nota privada
            String formatEmail = emailLogin.replaceAll("\\.", "___");
            new RestServicePost(jsoncomment).execute(CREATE_NOTA_URL + formatEmail);
        }

        Intent intent = new Intent(this, MenuActivity.class);
        this.startActivity(intent);


    }

    public void changeImage(View v) throws IOException {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Comment image");
        alert.setMessage("Insert URL");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                image = input.getText().toString();
                if (!image.isEmpty()) {
                    if ((image.substring(0, 3)).equalsIgnoreCase("www")) {
                        // por si un usuario mete la URL sin http:// (si es https que lo ponga...)
                        image = "http://" + image;
                    }

                    try {
                        URL url = new URL(image);

                        //guardar el campo image
                        if (image.toLowerCase().endsWith(".bmp") || image.toLowerCase().endsWith(".png") ||
                                image.toLowerCase().endsWith(".gif") || image.toLowerCase().endsWith(".jpg")
                                || image.toLowerCase().endsWith("jpeg")) {
                            Log.i("db", "user " + " <USUARIO> " + " saving image URL " + image);
                            new DownloadImageTask((ImageView) findViewById(R.id.addImageToComment)).execute(image);

                        } else {
                            showDialog(CANT_POST_ALERT, NOT_AN_IMAGE);
                        }
                    } catch (MalformedURLException e) {
                        //mandar campo image al carajo
                        showDialog(CANT_POST_ALERT, INVALID_IMAGE_URL);
                    }
                }
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();

    }

    public void changeYoutube(View v) throws IOException {

        youtubeImage = (ImageView) findViewById(R.id.addYoutubeVideoToComment);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Youtube Video");
        alert.setMessage("Insert URL");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String vId = null;
                youtube = input.getText().toString();
                Pattern pattern = Pattern.compile(".*(?:youtu.be\\/|v\\/|u\\/\\w\\/|embed\\/|watch\\?v=)([^#\\&\\?]*).*");
                Matcher matcher = pattern.matcher(youtube);
                if (matcher.matches()) {
                    vId = matcher.group(1);
                }
                if (vId != null) {
                    Log.i("db", "user " + " <USUARIO> " + " saving youtube video " + vId);
                    int id = getResources().getIdentifier("com.inftel.isn:drawable/youtubeok", null, null);
                    youtubeImage.setImageResource(id);
                    youtube = vId;
                } else {
                    showDialog(CANT_POST_ALERT, INVALID_YOUTUBE);
                    int id = getResources().getIdentifier("com.inftel.isn:drawable/youtubeerror", null, null);
                    youtubeImage.setImageResource(id);
                    youtube = "";
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }

    public void setSwitchStatus() {
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    xxlKeyboard();
                } else {
                    androidKeyboard();
                }
            }
        });
    }

    // mostrar mensaje de error
    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).show();
    }

    public void keyClicked(View view) {
        EditText content = (EditText) findViewById(R.id.postContent);
        if (view.getId() == R.id.n1) {
            content.setText(content.getText().toString() + "1");

        }
        if (view.getId() == R.id.n2) {
            content.setText(content.getText().toString() + "2");
        }
        if (view.getId() == R.id.n3) {
            content.setText(content.getText().toString() + "3");
        }
        if (view.getId() == R.id.n4) {
            content.setText(content.getText().toString() + "4");
        }
        if (view.getId() == R.id.n5) {
            content.setText(content.getText().toString() + "5");
        }
        if (view.getId() == R.id.n6) {
            content.setText(content.getText().toString() + "6");
        }
        if (view.getId() == R.id.n7) {
            content.setText(content.getText().toString() + "7");
        }
        if (view.getId() == R.id.n8) {
            content.setText(content.getText().toString() + "8");
        }
        if (view.getId() == R.id.n9) {
            content.setText(content.getText().toString() + "9");
        }
        if (view.getId() == R.id.n0) {
            content.setText(content.getText().toString() + "0");
        }
        if (view.getId() == R.id.q) {
            content.setText(content.getText().toString() + "q");
        }
        if (view.getId() == R.id.w) {
            content.setText(content.getText().toString() + "w");
        }
        if (view.getId() == R.id.e) {
            content.setText(content.getText().toString() + "e");
        }
        if (view.getId() == R.id.r) {
            content.setText(content.getText().toString() + "r");
        }
        if (view.getId() == R.id.t) {
            content.setText(content.getText().toString() + "t");
        }
        if (view.getId() == R.id.y) {
            content.setText(content.getText().toString() + "y");
        }
        if (view.getId() == R.id.u) {
            content.setText(content.getText().toString() + "u");
        }
        if (view.getId() == R.id.i) {
            content.setText(content.getText().toString() + "i");
        }
        if (view.getId() == R.id.o) {
            content.setText(content.getText().toString() + "o");
        }
        if (view.getId() == R.id.p) {
            content.setText(content.getText().toString() + "p");
        }
        if (view.getId() == R.id.a) {
            content.setText(content.getText().toString() + "a");
        }
        if (view.getId() == R.id.s) {
            content.setText(content.getText().toString() + "s");
        }
        if (view.getId() == R.id.d) {
            content.setText(content.getText().toString() + "d");
        }
        if (view.getId() == R.id.f) {
            content.setText(content.getText().toString() + "f");
        }
        if (view.getId() == R.id.g) {
            content.setText(content.getText().toString() + "g");
        }
        if (view.getId() == R.id.h) {
            content.setText(content.getText().toString() + "h");
        }
        if (view.getId() == R.id.j) {
            content.setText(content.getText().toString() + "j");
        }
        if (view.getId() == R.id.k) {
            content.setText(content.getText().toString() + "k");
        }
        if (view.getId() == R.id.l) {
            content.setText(content.getText().toString() + "l");
        }
        if (view.getId() == R.id.enye) {
            content.setText(content.getText().toString() + "ñ");
        }
        if (view.getId() == R.id.z) {
            content.setText(content.getText().toString() + "z");
        }
        if (view.getId() == R.id.x) {
            content.setText(content.getText().toString() + "x");
        }
        if (view.getId() == R.id.c) {
            content.setText(content.getText().toString() + "c");
        }
        if (view.getId() == R.id.v) {
            content.setText(content.getText().toString() + "v");
        }
        if (view.getId() == R.id.b) {
            content.setText(content.getText().toString() + "b");
        }
        if (view.getId() == R.id.n) {
            content.setText(content.getText().toString() + "n");
        }
        if (view.getId() == R.id.m) {
            content.setText(content.getText().toString() + "m");
        }
        if (view.getId() == R.id.comma) {
            content.setText(content.getText().toString() + ",");
        }
        if (view.getId() == R.id.point) {
            content.setText(content.getText().toString() + ".");
        }
        if (view.getId() == R.id.menos) {
            content.setText(content.getText().toString() + "-");
        }
        if (view.getId() == R.id.borrar) {
            if (content.getText().toString().length() >= 1) {
                content.setText(content.getText().toString().substring(0, content.getText().toString().length() - 1));
            }
        }
        if (view.getId() == R.id.espacio) {
            content.setText(content.getText().toString() + " ");
        }
        if (view.getId() == R.id.enviarcomentario ||
                view.getId() == R.id.enviarcomentarionormal) {
            intent = getIntent();
            doPost();
        }
        content.refreshDrawableState();
    }

    private void xxlKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        EditText edtView = (EditText) findViewById(R.id.postContent);
        edtView.setInputType(0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        TableLayout tl = (TableLayout) findViewById(R.id.table);
        tl.setVisibility(View.VISIBLE);
        Button botonEnviarComentario = (Button) findViewById(R.id.enviarcomentarionormal);
        botonEnviarComentario.setVisibility(View.GONE);
    }

    private void androidKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED);
        }
        EditText edtView = (EditText) findViewById(R.id.postContent);
        edtView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        if (edtView.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        TableLayout tl = (TableLayout) findViewById(R.id.table);
        tl.setVisibility(View.GONE);

        Button botonEnviarComentario = (Button) findViewById(R.id.enviarcomentarionormal);
        botonEnviarComentario.setVisibility(View.VISIBLE);
    }
}
