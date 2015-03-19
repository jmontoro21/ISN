package com.inftel.isn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.inftel.isn.R;
import com.inftel.isn.model.Group;
import com.inftel.isn.request.DownloadImageTask;

import java.io.IOException;

public class CreateGroupActivity extends Activity {
    EditText editText;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    public void createGroup(View v) {
        Group group = new Group();
        group.setName(editText.getText().toString());
        group.setImageUrl("imageURL");
        group.setAdmin("currentLogin");

        Intent i = new Intent(this, AddUsersGroup.class);
        startActivity(i);
    }

    public void changeImage (View v) throws IOException {
        new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute("http://www.online-image-editor.com//styles/2014/images/example_image.png");
    }




}
