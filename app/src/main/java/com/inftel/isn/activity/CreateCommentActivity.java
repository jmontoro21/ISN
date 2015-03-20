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

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateCommentActivity extends Activity {
    private final static String EMPTY_CONTENT_ALERT = "Post entry is too short.";
    private final static String CANT_POST_ALERT = "Can't post!";
    private final static String INVALID_IMAGE_URL = "Image URL invalid";
    private final static String INVALID_YOUTUBE = "Invalid Youtube link";
    private final static String NOT_AN_IMAGE = "URL is not a valid image";
    private final static String CREATE_PROFILE_POST_URL = "http://192.168.183.24:8080/InftelSocialNetwork-web/webresources/profilecomments/insertcomment/userEmail/";
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
        //EditText youtubeURL = (EditText) findViewById(R.id.youtube);
        String entryContent = content.getText().toString();
        //String youtube = youtubeURL.getText().toString();
        Comment comment = new Comment();
        if(!entryContent.isEmpty() && entryContent.length()>9) {
            //save post to database via async REST (on postExecute reload postList)
            Log.i("db","user <USUARIO> saving post content:\n"+entryContent);
            comment.setText(entryContent);
            comment.setCreationDate(new Date());
            if(!image.isEmpty()) {
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
                        comment.setImageUrl(image);
                    }else{
                        showDialog(CANT_POST_ALERT, NOT_AN_IMAGE);
                    }
                }
                catch (MalformedURLException e) {
                    //mandar campo image al carajo
                    showDialog(CANT_POST_ALERT, INVALID_IMAGE_URL);
                }

            }
            if(!youtube.isEmpty()) {
                String vId = null;
                Pattern pattern = Pattern.compile(".*(?:youtu.be\\/|v\\/|u\\/\\w\\/|embed\\/|watch\\?v=)([^#\\&\\?]*).*");
                Matcher matcher = pattern.matcher(youtube);
                if (matcher.matches()){
                    vId = matcher.group(1);
                }
                if(vId != null){
                    Log.i("db","user " + " <USUARIO> " + " saving youtube video "+vId);
                    comment.setVideoUrl(vId);
                }else{
                    showDialog(CANT_POST_ALERT, INVALID_YOUTUBE);
                }
            }
        }else{
            //show alert dialog
            showDialog(CANT_POST_ALERT, EMPTY_CONTENT_ALERT);
        }
        HttpRequestTask hrt = new HttpRequestTask();
        hrt.execute(comment);
    }

    public void changeImage (View v) throws IOException {
        new DownloadImageTask((ImageView) findViewById(R.id.addImageToComment)).execute("http://www.online-image-editor.com//styles/2014/images/example_image.png");
        image = "http://www.online-image-editor.com//styles/2014/images/example_image.png";
    }

    public void changeYoutube (View v) throws IOException {
        youtubeImage = (ImageView) findViewById(R.id.addYoutubeVideoToComment);
        int id = getResources().getIdentifier("com.inftel.isn:drawable/" + "logo", null, null);
        youtubeImage.setImageResource(id);

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
            try {
                //final String url = CREATE_PROFILE_POST_URL+"eduard@tatopagao.es"+"/newComment/";
                //RestTemplate restTemplate = new RestTemplate();
                //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

               // restTemplate.postForEntity(url, micomentario, Comment.class);
                Comment micomentario = params[0];
                Gson gson = new Gson();
                String json = gson.toJson(micomentario, Comment.class);

                HttpPost httpPost = new HttpPost(CREATE_PROFILE_POST_URL+"soyeldirector@mepagastutodo.es"+"/newComment/");
                httpPost.setEntity(new StringEntity(json,"UTF-8"));
                new DefaultHttpClient().execute(httpPost);

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
