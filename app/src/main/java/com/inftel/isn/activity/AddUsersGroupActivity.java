package com.inftel.isn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inftel.isn.R;
import com.inftel.isn.adapter.UsersAddedListAdapter;
import com.inftel.isn.model.Group;
import com.inftel.isn.model.User;
import com.inftel.isn.request.RestServicePost;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        try {
            Gson gson = new Gson();
            String json = gson.toJson(group, Group.class);
            JSONObject group = new JSONObject(json);
            new RestServicePost(group).execute("http://192.168.183.24:8080/InftelSocialNetwork-web/webresources/group/create");
        } catch (JSONException eq) {
            eq.printStackTrace();
        }

        for(User user: group.getUsersList()) {
            Toast data = Toast.makeText(this, user.getName(), Toast.LENGTH_SHORT);
            data.show();
        }
    }
}
