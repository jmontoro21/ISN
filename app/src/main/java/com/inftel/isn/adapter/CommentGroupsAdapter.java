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
import com.inftel.isn.model.Comment;
import com.inftel.isn.request.DownloadImageTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by loubna on 23/03/2015.
 */
public class CommentGroupsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Comment> objects;

    public CommentGroupsAdapter(ArrayList<Comment> objetos, Context context) {
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
        Comment comment= objects.get(position);
        textTitulo.setText(comment.getAuthor().getEmail());
        TextView textDescription = (TextView) convertView.findViewById(R.id.descripcion_item);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.itemImg);
        TextView textFechaIni = (TextView) convertView.findViewById(R.id.fecha);
        //ImageView img1 = null;
        // Picasso.with(context).load(R.drawable.user).into(imageView);
        String image =comment.getImageUrl();

        new DownloadImageTask(imageView).execute(image);
        // Picasso.with(context).load(String.valueOf(new DownloadImageTask(img1).execute(image))).into(imageView);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        textFechaIni.setText(dateFormat.format(comment.getCreationDate()));
        // textTitulo.setText("Titulo");
        textDescription.setText(comment.getText());
        return convertView;
    }
}
