package com.inftel.isn.activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inftel.isn.R;
import com.inftel.isn.adapter.UsersListAdapter;
import com.inftel.isn.model.Group;
import com.inftel.isn.model.User;
import com.inftel.isn.request.RestServiceGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserSearchActivity extends ListActivity {
    public static final String IP = "192.168.183.24";
    private Group group;
    int textlength=0;
    private ArrayList<User> array_sort;
    private EditText editText;
    private ListView listView;
    private ArrayList<User> users;
    public ArrayList<User> listUsersGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        users = new ArrayList<>();
        requestUsersBBDD();

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("group");
        group = gson.fromJson(strObj, Group.class);

        listUsersGroup = (ArrayList) group.getUser();

        listView = (ListView)findViewById(android.R.id.list);
        editText = (EditText)findViewById(R.id.editText3);

        array_sort = new ArrayList<> (users);

        setListAdapter(new UsersListAdapter(array_sort, listUsersGroup, UserSearchActivity.this));

        editText.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                textlength = editText.getText().length();
                array_sort.clear();
                for (int i = 0; i < users.size(); i++)
                {
                    if (textlength <= users.get(i).getName().length())
                    {
                        if(users.get(i).getName().toLowerCase().contains(
                                editText.getText().toString().toLowerCase().trim()))
                        {
                            array_sort.add(users.get(i));
                        }
                    }
                }
                setListAdapter(new UsersListAdapter(array_sort, listUsersGroup, UserSearchActivity.this));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                if(listUsersGroup.contains(array_sort.get(position))){
                    group.removeUserFromList(array_sort.get(position));
                    arg1.setBackgroundColor(Color.TRANSPARENT);
                    Toast.makeText(getApplicationContext(), array_sort.get(position).getName()+" eliminado", Toast.LENGTH_SHORT).show();
                }
                else{
                    group.addUserToList(array_sort.get(position));
                    arg1.setBackgroundColor(Color.parseColor("#81F781"));
                    Toast.makeText(getApplicationContext(), array_sort.get(position).getName()+" añadido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void requestUsersBBDD(){
        try {
            String respStr = new RestServiceGet().execute("http://192.168.183.24:8080/InftelSocialNetwork-web/webresources/users").get();
            JSONArray respJSON = new JSONArray(respStr);
            Gson gson = new Gson();
            User user;
            SharedPreferences prefs = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
            String email = prefs.getString(LoginGoogleActivity.USER_KEY, "");
            if (respJSON.length() != 0) {
                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject object = respJSON.getJSONObject(i);
                    user = gson.fromJson(object.toString(), User.class);
                    if(!user.getEmail().equals(email))
                        users.add(gson.fromJson(object.toString(), User.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void intentUsersGroup(View v) {
        Gson gson = new Gson();
        Intent intent = new Intent(this, AddUsersGroupActivity.class);
        intent.putExtra("usersGroup", gson.toJson(group));
        startActivity(intent);
    }
}

