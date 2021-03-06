package com.inftel.isn.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inftel.isn.R;
import com.inftel.isn.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FollowerListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<User> objects;
    Activity activity;
    public ArrayList<User> selectedIds = new ArrayList<>();

    public FollowerListAdapter(List<User> objetos, Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.objects = objetos;
    }


    @Override
    public int getCount() {
        if(objects!=null) return objects.size();
        return 0;
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

        Picasso.with(activity).load(R.drawable.user).into(imageView);
        textTitulo.setText(objects.get(position).getName());
        textDescription.setText(objects.get(position).getEmail());
        return itemView;
    }
}