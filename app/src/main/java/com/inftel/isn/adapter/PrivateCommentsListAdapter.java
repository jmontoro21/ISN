package com.inftel.isn.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inftel.isn.R;
import com.inftel.isn.model.Comment;
import com.inftel.isn.model.PrivateComments;
import com.inftel.isn.request.DownloadImageTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PrivateCommentsListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<Comment> objects;
    Activity activity;
    PrivateComments perfil;

    String emailGoogle;

    public PrivateCommentsListAdapter(PrivateComments perfil, String emailGoogle, ArrayList<Comment> objetos, Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.objects = objetos;
        this.emailGoogle = emailGoogle;
        this.perfil = perfil;
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
        View itemView = inflater.inflate(R.layout.private_comment_item, parent, false);

        ImageView ImgPerfil = (ImageView) itemView.findViewById(R.id.userImg);
        if(objects!=null && objects.get(position) != null && objects.get(position).getAuthor() != null
                && objects.get(position).getAuthor().getImageUrl()!=null) {
            new DownloadImageTask(ImgPerfil).execute(objects.get(position).getAuthor().getImageUrl(), "");
        }


        TextView textNombre = (TextView) itemView.findViewById(R.id.name);
        textNombre.setText(objects.get(position).getAuthor().getName());
        TextView textFecha = (TextView) itemView.findViewById(R.id.date);
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        textFecha.setText(formateador.format(objects.get(position).getCreationDate()));
        TextView textDescription = (TextView) itemView.findViewById(R.id.description);
        textDescription.setText(objects.get(position).getText());


        if(objects.get(position).getImageUrl() != null  && !objects.get(position).getImageUrl().isEmpty()) {
            ImageView imgComentario = (ImageView) itemView.findViewById(R.id.imgComment);
            new DownloadImageTask(imgComentario).execute(objects.get(position).getImageUrl(), "");
        }

        if(objects.get(position).getVideoUrl() != null && !objects.get(position).getVideoUrl().isEmpty() ) {
            WebView videoComentario = (WebView) itemView.findViewById(R.id.videoComment);

            WebSettings settings = videoComentario.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setAllowFileAccess(true);
            settings.setPluginState(WebSettings.PluginState.ON);


            String html = "";
            html += "<html><body>";
            html += "<iframe width=\"560\" height=\"315\" src=\"http://www.youtube.com/embed/"+ objects.get(position).getVideoUrl() +"?rel=0\" frameborder=\"0\" allowfullscreen></iframe>";
            html += "</body></html>";

            videoComentario.loadData(html, "text/html", null);

        }

        return itemView;
    }
}