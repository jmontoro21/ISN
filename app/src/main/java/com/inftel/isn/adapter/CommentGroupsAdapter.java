package com.inftel.isn.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inftel.isn.R;
import com.inftel.isn.activity.CreateCommentActivity;
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
            convertView = mInflater.inflate(R.layout.public_comment_group_item, null);
        }

        ImageView ImgPerfil = (ImageView) convertView.findViewById(R.id.userImg);
        new DownloadImageTask(ImgPerfil).execute(objects.get(position).getAuthor().getImageUrl(), "");
        TextView textNombre = (TextView) convertView.findViewById(R.id.name);
        textNombre.setText(objects.get(position).getAuthor().getName());
        TextView textFecha = (TextView) convertView.findViewById(R.id.date);

        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        textFecha.setText( formateador.format(objects.get(position).getCreationDate()));
        TextView textDescription = (TextView) convertView.findViewById(R.id.description);
        textDescription.setText(objects.get(position).getText());

        if(objects.get(position).getImageUrl() != null  && !objects.get(position).getImageUrl().isEmpty()) {
            ImageView imgComentario = (ImageView) convertView.findViewById(R.id.imgComment);
            new DownloadImageTask(imgComentario).execute(objects.get(position).getImageUrl(), "");
        }

        if(objects.get(position).getVideoUrl() != null && !objects.get(position).getVideoUrl().isEmpty() ) {
            WebView viedoComentario = (WebView) convertView.findViewById(R.id.videoComment);

            WebSettings settings = viedoComentario.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setAllowFileAccess(true);
            settings.setPluginState(WebSettings.PluginState.ON);


            String html = "";
            html += "<html><body>";
            html += "<iframe width=\"560\" height=\"315\" src=\"http://www.youtube.com/embed/"+ objects.get(position).getVideoUrl() +"?rel=0\" frameborder=\"0\" allowfullscreen></iframe>";
            html += "</body></html>";

            viedoComentario.loadData(html, "text/html", null);

        }

        return convertView;
    }

}
