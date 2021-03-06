package com.inftel.isn.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.inftel.isn.R;
import com.inftel.isn.activity.CommentGroupActivity;
import com.inftel.isn.activity.LoginGoogleActivity;
import com.inftel.isn.adapter.GroupAdapter;
import com.inftel.isn.model.Group;
import com.inftel.isn.request.GroupsRequest;

import java.util.List;

public class GroupFragment extends Fragment {
    private ListView listView;
    private String email;
    EditText title;

    private Handler handler;
    private Runnable runnable;

    @Override
    public void onPause(){
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        runnable = new Runnable() {
            @Override
            public void run() {
                new GroupsRequest(GroupFragment.this).execute();
                handler.postDelayed(this, 3000);
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 3000);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group, container, false);
        listView = (ListView) v.findViewById(R.id.listview_droup);

        SharedPreferences prefs = getActivity().getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        email = prefs.getString(LoginGoogleActivity.USER_KEY, "");

        email=email.replaceAll("\\.","___");

        new GroupsRequest(GroupFragment.this).execute();

        return  v;
    }

    public String getEmail(){
        return email;
    }

    public void loadListView(List<Group> group) {

        GroupAdapter adapter = new GroupAdapter(group, getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group= (Group)parent.getAdapter().getItem(position);
                changeActivity(group);

            }
        });

    }

    public void changeActivity(Group group) {
        Intent intent = new Intent(getActivity(), CommentGroupActivity.class);
        intent.putExtra("group", group);
        startActivity(intent);
    }

}
