package com.inftel.isn.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.inftel.isn.R;
import com.inftel.isn.adapter.GroupAdapter;
import com.inftel.isn.model.Group;
import com.inftel.isn.request.GroupsRequest;

import java.util.List;

public class SearchGroupsActivity extends Activity {

    private ListView listView;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_groups);

        listView = (ListView) findViewById(R.id.listview_droup);
    }





    public String getEmail(){
        return email;
    }
    public void onClick(View v){

      //  EditText title = (EditText) findViewById(R.id.buscar);
        //TextView title = (TextView) findViewById(R.id.buscar);

      //  email = title.getText().toString();
        email=email.replaceAll("\\.","___");

     //   new GroupsRequest(this).execute();



    }

    public void loadListView(List<Group> group) {




        GroupAdapter adapter = new GroupAdapter(group, SearchGroupsActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Group group= (Group)parent.getAdapter().getItem(position);
                Group group1 =group;
                changeActivity(group);

            }
        });

        }



    public void changeActivity(Group group) {
        Intent intent = new Intent(this, CommentGroupActivity.class);
        intent.putExtra("group", group);
        //intent.putExtra("admin", group.getAdmin());
       // intent.putExtra("name", group.getName());
        startActivity(intent);
    }



}
