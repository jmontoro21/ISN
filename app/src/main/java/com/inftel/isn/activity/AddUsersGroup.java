package com.inftel.isn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.inftel.isn.R;
import com.inftel.isn.entity.Group;

public class AddUsersGroup extends Activity {

    private Group group;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users_group);

        Intent intent = getIntent();
        group = (Group)intent.getExtras().get("group");

        listView = (ListView)findViewById(R.id.itemList);
    }

    private void addUser() {

    }

}
