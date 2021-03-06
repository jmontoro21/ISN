package com.inftel.isn.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.inftel.isn.R;
import com.inftel.isn.activity.LoginGoogleActivity;
import com.inftel.isn.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAddedListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<User> objects;
    Activity activity;

    public UsersAddedListAdapter(ArrayList<User> objetos, Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.objects = objetos;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.user_item_erasable, parent, false);
        TextView textTitulo = (TextView) itemView.findViewById(R.id.name);
        TextView textDescription = (TextView) itemView.findViewById(R.id.description);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.userImg);
        ImageButton imageButton = (ImageButton) itemView.findViewById(R.id.deleteImg);
        imageButton.setTag(position);

        SharedPreferences prefs = activity.getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        String email = prefs.getString(LoginGoogleActivity.USER_KEY, "");
        if(objects.get(position).getEmail().equals(email))
            imageButton.setVisibility(View.INVISIBLE);

        Picasso.with(activity).load(R.drawable.user).into(imageView);
        textTitulo.setText(objects.get(position).getName());
        textDescription.setText(objects.get(position).getEmail());
        return itemView;
    }
}