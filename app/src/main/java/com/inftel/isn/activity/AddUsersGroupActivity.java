package com.inftel.isn.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.inftel.isn.R;
import com.inftel.isn.adapter.UsersListAdapter;
import com.inftel.isn.model.Group;

import java.util.ArrayList;

public class AddUsersGroupActivity extends Activity {

    private Group group;
    private ListView listView;
    private UsersListAdapter adapter;
    private ArrayList objetos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users_group);

        listView = (ListView)findViewById(R.id.userList);

        //Array estatico provisional
        objetos = new ArrayList<String>();
        objetos.add("1");objetos.add("2");objetos.add("3");objetos.add("4");objetos.add("5");objetos.add("6");

        adapter = new UsersListAdapter(objetos, this);
        listView.setAdapter(adapter);
    }

    public void deleteItem(View v) {
        objetos.remove((int)v.getTag());
        adapter.notifyDataSetChanged();
    }

}
