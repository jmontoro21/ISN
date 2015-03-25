package com.inftel.isn.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.inftel.isn.R;
import com.inftel.isn.model.Comment;
import com.inftel.isn.request.DownloadImageTask;
import com.inftel.isn.request.RestServicePost;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
    private ImageView addImageView;
    private ImageView youtubeImage;
    private String image = "";
    private String youtube = "";
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);
        Comment newComment = new Comment();
        newComment.setText("mierda");
        newComment.setImageUrl("asfdasfas");
        newComment.setVideoUrl("asdasfasfsd");

        Gson gsonComment = new Gson();
        String jsonComment = gsonComment.toJson(newComment, Comment.class);
        JSONObject comment = null;
        try {
            comment = new JSONObject(jsonComment);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new RestServicePost(comment).execute("http://192.168.183.24:8080/InftelSocialNetwork-web/webresources/profilecomments/insert/alfredo.gallego.gonzalez@gmail.com");
        System.out.println("parate");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        addImageView = (ImageView) findViewById(R.id.addImageToComment);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        EditText edtView=(EditText)findViewById(R.id.postContent);
        edtView.setInputType(0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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


    public void doPost(){
        String tipo = intent.getStringExtra(COMMENT_TYPE);
        EditText content = (EditText) findViewById(R.id.postContent);
        String entryContent = content.getText().toString();
        Comment comment = new Comment();
        if(!entryContent.isEmpty() && entryContent.length()>5) {
            //save post to database via async REST (on postExecute reload postList)
            Log.i("db","user <USUARIO> saving post content:\n"+entryContent);
            comment.setText(entryContent);
            comment.setImageUrl("");
            comment.setVideoUrl("");
            if(!image.isEmpty()) {
                comment.setImageUrl(image);
            }
            if(!youtube.isEmpty()) {
                comment.setVideoUrl(youtube);
            }
        }else{
            //show alert dialog
            showDialog(CANT_POST_ALERT, EMPTY_CONTENT_ALERT);
        }

        Gson gsonComment = new Gson();
        String jsonComment = gsonComment.toJson(comment, Comment.class);
        JSONObject jsoncomment = null;
        try {
            jsoncomment = new JSONObject(jsonComment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(tipo.equals("publico")){
            Log.i("nc","profile");
            new RestServicePost(jsoncomment).execute(CREATE_PROFILE_POST_URL+"CERVEZA");
        }else if(tipo.equals("grupo")){
            Log.i("nc","grupo");
            //llamada rest grupoComment
        }else if(tipo.equals("nota")){
            Log.i("nc","private-nota");
            //llamda rest nota privada
            new RestServicePost(jsoncomment).execute(CREATE_NOTA_URL+"CERVEZA");
        }


    }

    public void changeImage (View v) throws IOException {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Comment image");
        alert.setMessage("Insert URL");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                image = input.getText().toString();
                if(!image.isEmpty()){
                    if((image.substring(0,3)).equalsIgnoreCase("www")) {
                        // por si un usuario mete la URL sin http:// (si es https que lo ponga...)
                        image = "http://"+image;
                    }

                    try {
                        URL url = new URL(image);

                        //guardar el campo image
                        if(image.toLowerCase().endsWith(".bmp") || image.toLowerCase().endsWith(".png") ||
                                image.toLowerCase().endsWith(".gif") ||image.toLowerCase().endsWith(".jpg")
                                || image.toLowerCase().endsWith("jpeg")){
                            Log.i("db","user " + " <USUARIO> " + " saving image URL "+image);
                            new DownloadImageTask((ImageView) findViewById(R.id.addImageToComment)).execute(image);

                        }else{
                            showDialog(CANT_POST_ALERT, NOT_AN_IMAGE);
                        }
                    }
                    catch (MalformedURLException e) {
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

    public void changeYoutube (View v) throws IOException {

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
                if (matcher.matches()){
                    vId = matcher.group(1);
                }
                if(vId != null){
                    Log.i("db","user " + " <USUARIO> " + " saving youtube video "+vId);
                    int id = getResources().getIdentifier("com.inftel.isn:drawable/youtubeok", null, null);
                    youtubeImage.setImageResource(id);
                    youtube = vId;
                }else{
                    showDialog(CANT_POST_ALERT, INVALID_YOUTUBE);
                    int id = getResources().getIdentifier("com.inftel.isn:drawable/youtubeerror", null, null);
                    youtubeImage.setImageResource(id);
                    youtube="";
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

    // mostrar mensaje de error
    private void showDialog(String title, String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).show();
    }

    public void keyClicked(View view){
        EditText content = (EditText) findViewById(R.id.postContent);
        if(view.getId() == R.id.n1){
            content.setText(content.getText().toString() +"1");

        }
        if(view.getId() == R.id.n2){
            content.setText(content.getText().toString() + "2");
        }
        if(view.getId() == R.id.n3){
            content.setText(content.getText().toString() +"3");
        }
        if(view.getId() == R.id.n4){
            content.setText(content.getText().toString() +"4");
        }
        if(view.getId() == R.id.n5){
            content.setText(content.getText().toString() +"5");
        }
        if(view.getId() == R.id.n6){
            content.setText(content.getText().toString() +"6");
        }
        if(view.getId() == R.id.n7){
            content.setText(content.getText().toString() +"7");
        }
        if(view.getId() == R.id.n8){
            content.setText(content.getText().toString() +"8");
        }
        if(view.getId() == R.id.n9){
            content.setText(content.getText().toString() +"9");
        }
        if(view.getId() == R.id.n0){
            content.setText(content.getText().toString() +"0");
        }
        if(view.getId() == R.id.q){
            content.setText(content.getText().toString() +"q");
        }
        if(view.getId() == R.id.w){
            content.setText(content.getText().toString() +"w");
        }
        if(view.getId() == R.id.e){
            content.setText(content.getText().toString() +"e");
        }
        if(view.getId() == R.id.r){
            content.setText(content.getText().toString() +"r");
        }
        if(view.getId() == R.id.t){
            content.setText(content.getText().toString() +"t");
        }
        if(view.getId() == R.id.y){
            content.setText(content.getText().toString() +"y");
        }
        if(view.getId() == R.id.u){
            content.setText(content.getText().toString() +"u");
        }
        if(view.getId() == R.id.i){
            content.setText(content.getText().toString() +"i");
        }
        if(view.getId() == R.id.o){
            content.setText(content.getText().toString() +"o");
        }
        if(view.getId() == R.id.p){
            content.setText(content.getText().toString() +"p");
        }
        if(view.getId() == R.id.a){
            content.setText(content.getText().toString() +"a");
        }
        if(view.getId() == R.id.s){
            content.setText(content.getText().toString() +"s");
        }
        if(view.getId() == R.id.d){
            content.setText(content.getText().toString() +"d");
        }
        if(view.getId() == R.id.f){
            content.setText(content.getText().toString() +"f");
        }
        if(view.getId() == R.id.g){
            content.setText(content.getText().toString() +"g");
        }
        if(view.getId() == R.id.h){
            content.setText(content.getText().toString() +"h");
        }
        if(view.getId() == R.id.j){
            content.setText(content.getText().toString() +"j");
        }
        if(view.getId() == R.id.k){
            content.setText(content.getText().toString() +"k");
        }
        if(view.getId() == R.id.l){
            content.setText(content.getText().toString() +"l");
        }
        if(view.getId() == R.id.enye){
            content.setText(content.getText().toString() +"ñ");
        }
        if(view.getId() == R.id.z){
            content.setText(content.getText().toString() +"z");
        }
        if(view.getId() == R.id.x){
            content.setText(content.getText().toString() +"x");
        }
        if(view.getId() == R.id.c){
            content.setText(content.getText().toString() +"c");
        }
        if(view.getId() == R.id.v){
            content.setText(content.getText().toString() +"v");
        }
        if(view.getId() == R.id.b){
            content.setText(content.getText().toString() +"b");
        }
        if(view.getId() == R.id.n){
            content.setText(content.getText().toString() +"n");
        }
        if(view.getId() == R.id.m){
            content.setText(content.getText().toString() +"m");
        }
        if(view.getId() == R.id.comma){
            content.setText(content.getText().toString() +",");
        }
        if(view.getId() == R.id.point){
            content.setText(content.getText().toString() +".");
        }
        if(view.getId() == R.id.menos){
            content.setText(content.getText().toString() +"-");
        }
        if(view.getId() == R.id.borrar){
            if(content.getText().toString().length()>=1) {
                content.setText(content.getText().toString().substring(0, content.getText().toString().length() - 1));
            }
        }
        if(view.getId() == R.id.espacio){
            content.setText(content.getText().toString() +" ");
        }
        if(view.getId() == R.id.enviarcomentario){
            intent = getIntent();
            doPost();
        }
        content.refreshDrawableState();
    }

    private class HttpRequestTask extends AsyncTask<Comment, Void, Void> {
        @Override
        protected Void doInBackground(Comment... params) {
            HttpResponse httpResponse= null;
            try {
               // Comment micomentario = params[0];
                Gson gson = new Gson();
                String json = gson.toJson(params[0], Comment.class);

                HttpPost httpPost = new HttpPost(CREATE_PROFILE_POST_URL);
                StringEntity entity = new StringEntity(json, "UTF-8");
                entity.setContentType("application/json;charset=UTF-8");
                entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));

                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setEntity(entity);
                httpResponse = new DefaultHttpClient().execute(httpPost);



              //  Log.e(httpResponse.getStatusLine());

            } catch (Exception e) {
                Log.e("CreatePostActivity", e.getMessage(), e);
            }

            return null;
        }

        protected void onPostExecute(Void nothing) {
            Log.i("async","done");
        }

    }
}
