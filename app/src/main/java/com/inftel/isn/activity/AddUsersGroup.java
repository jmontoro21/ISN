package com.inftel.isn.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.inftel.isn.R;
import com.inftel.isn.adapter.UsersListAdapter;
import com.inftel.isn.model.Group;

import java.util.ArrayList;

public class AddUsersGroup extends Activity {

    private Group group;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users_group);

        listView = (ListView)findViewById(R.id.userList);

        //Array estatico provisional
        ArrayList objetos = new ArrayList<String>();
        objetos.add("paco");objetos.add("hola");objetos.add("hola");objetos.add("hola");objetos.add("paco");objetos.add("paco");

        UsersListAdapter adapter = new UsersListAdapter(objetos, this);
        listView.setAdapter(adapter);
    }

}
