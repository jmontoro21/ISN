package com.inftel.isn.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.inftel.isn.R;

public class CreatePostActivity extends Activity {
    private final static String EMPTY_CONTENT_ALERT = "POST NEEDS TO HAVE SOME CONTENT...";
    private final static String CANT_POST_ALERT = "Can't post!";

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

    public void selectMultimedia(){
        // lanzar Dropbox
        // seleccionar URL
        // actuar sobre URL (filtrar, etc)
        // guardar URL en campo correspondiente de CreatePostActvity
    }

    public void doPost(){
        EditText content = (EditText) findViewById(R.id.postContent);
        String entryContent = content.getText().toString();
        if(!entryContent.isEmpty() && entryContent.length()>9) {
            //save post to database via async REST (on postExecute reload postList)

        }else{
            //show alert dialog
            new AlertDialog.Builder(this)
                    .setTitle(CANT_POST_ALERT)
                    .setMessage(EMPTY_CONTENT_ALERT)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }).show();
        }
    }
}
