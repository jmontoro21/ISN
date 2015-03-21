package com.inftel.isn.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.inftel.isn.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<String> objects;
    Activity activity;

    public UsersListAdapter(ArrayList<String> objetos, Activity activity) {
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
        View itemView = inflater.inflate(R.layout.user_item, parent, false);
        TextView textTitulo = (TextView) itemView.findViewById(R.id.name);
        TextView textDescription = (TextView) itemView.findViewById(R.id.description);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.userImg);
        ImageButton imageButton = (ImageButton) itemView.findViewById(R.id.deleteImg);
        imageButton.setTag(position);

        Picasso.with(activity).load(R.drawable.user).into(imageView);
        textTitulo.setText("Usuario " + objects.get(position));
        textDescription.setText("El más pro del barrio");
        return itemView;
    }
}