package com.inftel.isn.activity;

import android.app.ListActivity;
import android.content.Intent;
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

import java.util.ArrayList;

public class UserSearchActivity extends ListActivity {

    private Group group;
    int textlength=0;
    private ArrayList<User> array_sort;
    private EditText editText;
    private ListView listView;
    private ArrayList<User> users;

    private void crearUsuariosProvisionales(){
        users = new ArrayList<>();
        User user1 = new User("1", "googleId 1", "email 1", "Usuario 1", "Imagen 1");
        User user2 = new User("2", "googleId 2", "email 2", "Usuario 2", "Imagen 2");
        User user3 = new User("3", "googleId 3", "email 3", "Usuario 3", "Imagen 3");
        User user4 = new User("4", "googleId 4", "email 4", "Usuario 4", "Imagen 4");
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        crearUsuariosProvisionales();

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("group");
        group = gson.fromJson(strObj, Group.class);

        listView = (ListView)findViewById(android.R.id.list);
        editText = (EditText)findViewById(R.id.editText3);

        array_sort=new ArrayList<> (users);

        setListAdapter(new UsersListAdapter(array_sort, UserSearchActivity.this));

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
                setListAdapter(new UsersListAdapter(array_sort, UserSearchActivity.this));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                group.addUserToList(array_sort.get(position));
                Toast.makeText(getApplicationContext(), array_sort.get(position).getName()+" añadido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void intentUsersGroup(View v) {
        Gson gson = new Gson();
        Intent intent = new Intent(this, AddUsersGroupActivity.class);
        intent.putExtra("usersGroup", gson.toJson(group));
        startActivity(intent);
    }
}

