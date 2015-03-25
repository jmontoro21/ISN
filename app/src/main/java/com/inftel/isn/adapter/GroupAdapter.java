package com.inftel.isn.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inftel.isn.R;
import com.inftel.isn.model.Group;

import com.inftel.isn.request.DownloadImageTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by loubna on 23/03/2015.
 */
public class GroupAdapter extends BaseAdapter {
    private Context context;
    private  List<Group> objects;

    public GroupAdapter(List<Group> objetos, Context context) {
        this.context = context;
        this.objects= objetos;
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
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item, null);
        }


        TextView textTitulo = (TextView) convertView.findViewById(R.id.titulo_item);
        Group group= objects.get(position);
        textTitulo.setText(group.getName());
         TextView textDescription = (TextView) convertView.findViewById(R.id.descripcion_item);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.itemImg);
        TextView textFechaIni = (TextView) convertView.findViewById(R.id.fecha);

        String image =group.getImageUrl();

        new DownloadImageTask(imageView).execute(image);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        textFechaIni.setText(dateFormat.format(new Date()));

        textDescription.setText("Descripcion");
        return convertView;
    }
}
