package com.inftel.isn.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.inftel.isn.R;
import com.inftel.isn.activity.LoginGoogleActivity;
import com.inftel.isn.model.Comment;
import com.inftel.isn.model.PrivateComments;
import com.inftel.isn.model.ProfileComments;
import com.inftel.isn.model.User;
import com.inftel.isn.request.DownloadImageTask;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PublicsUsersCommentsListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<Comment> objects;
    Activity activity;
    ProfileComments perfil;

    String emailGoogle;

    public PublicsUsersCommentsListAdapter(ProfileComments perfil, String emailGoogle, ArrayList<Comment> objetos, Activity activity) {
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
        View itemView = inflater.inflate(R.layout.public_comment_item, parent, false);

        ImageView ImgPerfil = (ImageView) itemView.findViewById(R.id.userImg);
        new DownloadImageTask(ImgPerfil).execute(objects.get(position).getAuthor().getImageUrl(), "");

        TextView textNombre = (TextView) itemView.findViewById(R.id.name);
        textNombre.setText(objects.get(position).getAuthor().getName());

        TextView textFecha = (TextView) itemView.findViewById(R.id.date);

        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        textFecha.setText( formateador.format(objects.get(position).getCreationDate()));


        ImageButton botonBorrar = (ImageButton) itemView.findViewById(R.id.btnDeleteComments);
        ImageButton botonCompartir = (ImageButton) itemView.findViewById(R.id.btnShare);
        if(perfil.getUserEmail().compareTo(emailGoogle) == 0 )
        {
            //ImageButton botonBorrar = (ImageButton) itemView.findViewById(R.id.btnDelete);
            botonBorrar.setTag(position);
            botonCompartir.setVisibility(View.INVISIBLE);
        }
        else
        {
            // llamada al metodo profilecommentsservice, email, comment, para ver si existe el comentario
            // seria un post
            // devolveria true o false

            // si la llamada a post es true, muestra el boton

            //ImageButton botonCompartir = (ImageButton) itemView.findViewById(R.id.btnShare);
            botonCompartir.setTag(position);
            botonBorrar.setVisibility(View.INVISIBLE);
            ///
        }

        TextView textDescription = (TextView) itemView.findViewById(R.id.description);
        textDescription.setText(objects.get(position).getText());


        if(objects.get(position).getImageUrl() != null  && !objects.get(position).getImageUrl().isEmpty()) {
            ImageView imgComentario = (ImageView) itemView.findViewById(R.id.imgComment);
            new DownloadImageTask(imgComentario).execute(objects.get(position).getImageUrl(), "");
        }

        if(objects.get(position).getVideoUrl() != null && !objects.get(position).getVideoUrl().isEmpty() ) {
            WebView viedoComentario = (WebView) itemView.findViewById(R.id.videoComment);

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

        return itemView;
    }
}