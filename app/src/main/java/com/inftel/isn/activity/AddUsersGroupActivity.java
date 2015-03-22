package com.inftel.isn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inftel.isn.R;
import com.inftel.isn.adapter.UsersAddedListAdapter;
import com.inftel.isn.model.Group;
import com.inftel.isn.model.User;

public class AddUsersGroupActivity extends Activity{

    private Group group;
    private ListView listView;
    private UsersAddedListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users_group);

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("usersGroup");
        group = gson.fromJson(strObj, Group.class);

        listView = (ListView)findViewById(R.id.list_users);

        adapter = new UsersAddedListAdapter(group.getUsersList(), this);
        listView.setAdapter(adapter);
    }

    public void deleteItem(View v) {
        group.removeUserList((int) v.getTag());
        Toast data = Toast.makeText(this, "User removed", Toast.LENGTH_LONG);
        data.show();
        adapter.notifyDataSetChanged();
    }

    public void intentList(View v) {
        Gson gson = new Gson();
        Intent i = new Intent(this, UserSearchActivity.class);
        i.putExtra("group", gson.toJson(group));
        startActivity(i);
    }

    public void intentCreated(View v) {
        //Aqui subo el grupo "group" a la BBDD ya relleno
        for(User user: group.getUsersList()) {
            Toast data = Toast.makeText(this, user.getName(), Toast.LENGTH_SHORT);
            data.show();
        }
    }
}
