package com.inftel.isn.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.inftel.isn.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreatePostActivity extends Activity {
    private final static String EMPTY_CONTENT_ALERT = "POST NEEDS TO HAVE SOME CONTENT...";
    private final static String CANT_POST_ALERT = "Can't post!";
    private final static String INVALID_IMAGE_URL = "Image URL invalid";
    private final static String INVALID_YOUTUBE = "Invalid Youtube link";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
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


    public void doPost(View view){
        EditText content = (EditText) findViewById(R.id.postContent);
        EditText imageURL = (EditText) findViewById(R.id.imageURL);
        EditText youtubeURL = (EditText) findViewById(R.id.youtube);
        String entryContent = content.getText().toString();
        String image = imageURL.getText().toString();
        String youtube = youtubeURL.getText().toString();
        if(!entryContent.isEmpty() && entryContent.length()>9) {
            //save post to database via async REST (on postExecute reload postList)
            if(!image.isEmpty()) {
                if((image.substring(0,3)).equalsIgnoreCase("www")) {
                    // por si un usuario mete la URL sin http:// (si es https que lo ponga...)
                    image = "http://"+image;
                    Log.i("url-replaced",image);
                }

                try {
                    URL url = new URL(image);
                    //guardar el campo image
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
                    //guardar campo
                }else{
                    showDialog(CANT_POST_ALERT, INVALID_YOUTUBE);
                }
            }
        }else{
            //show alert dialog
            showDialog(CANT_POST_ALERT, EMPTY_CONTENT_ALERT);
        }
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
}
