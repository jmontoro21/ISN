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

        adapter = new UsersAddedListAdapter((java.util.ArrayList<User>) group.getUser(), this);
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
        try {
            Gson gson = new Gson();

            String nuevo = "{ \"admin\" : \"jmontoro21@gmail.com\" , \"name\" : \"vayamierda\" , \"imageUrl\" : \"\" , \"user\" : [ { \"googleId\" : \"103877372006820839918\" , \"email\" : \"alfredo.gallego.gonzalez@gmail.com\" , \"name\" : \"Alfredo Gallego\" , \"imageUrl\" : \"https://lh5.googleusercontent.com/-i9X5ZiLaDKs/AAAAAAAAAAI/AAAAAAAAADI/9pIodSITkx0/photo.jpg?sz=250\"} , { \"_id\" : { \"$oid\" : \"551032deda06477fe1833e0f\"} , \"googleId\" : \"107628342645597570491\" , \"email\" : \"frodo.bolsonazos@gmail.com\" , \"name\" : \"Alfredo Gallego\" , \"imageUrl\" : \"https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg?sz=250\"} , { \"_id\" : \"55103b38da06439be39ceecd\" , \"googleId\" : \"104611636054460643894\" , \"email\" : \"jmontoro21@gmail.com\" , \"name\" : \"josé Fernández Montoro\" , \"imageUrl\" : \"https://lh3.googleusercontent.com/-pZZ7SAekdUs/AAAAAAAAAAI/AAAAAAAAAJM/fiGv2WJ1pSE/photo.jpg?sz=250\"}]}";
            String json = gson.toJson(group, Group.class);

            JSONObject group = new JSONObject(json);
            System.out.println("pantalla "+group.toString());
            new RestServicePost(group).execute("http://192.168.183.24:8080/InftelSocialNetwork-web/webresources/group/create");
        } catch (JSONException eq) {
            eq.printStackTrace();
        }

        for(User user: group.getUser()) {
            Toast data = Toast.makeText(this, user.getName(), Toast.LENGTH_SHORT);
            data.show();
        }
    }
}
