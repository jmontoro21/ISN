package com.inftel.isn.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private ImageView addImageView;
    private ImageView youtubeImage;
    private String image = "";
    private String youtube = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        addImageView = (ImageView) findViewById(R.id.addImageToComment);
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


    public void doPost(View view){
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

        new RestServicePost(jsoncomment).execute(CREATE_PROFILE_POST_URL+"CERVEZA");

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
