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

import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.inftel.isn.R;
import com.inftel.isn.activity.LoginGoogleActivity;
import com.inftel.isn.model.Comment;
import com.inftel.isn.model.PrivateComments;
import com.inftel.isn.model.ProfileComments;
import com.inftel.isn.model.User;
import com.inftel.isn.request.DownloadImageTask;

import com.inftel.isn.request.RestServiceGet;
import com.inftel.isn.request.RestServicePost;
//import com.inftel.isn.request.RestServicePostWithResp;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PublicsUsersCommentsListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<Comment> objects;
    Activity activity;
    ProfileComments perfil;
    public static final String IP = "192.168.1.123";
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
        if(objects!=null && objects.get(position) != null && objects.get(position).getAuthor() != null
                && objects.get(position).getAuthor().getImageUrl()!=null) {
            new DownloadImageTask(ImgPerfil).execute(objects.get(position).getAuthor().getImageUrl(), "");
        }


        TextView textNombre = (TextView) itemView.findViewById(R.id.name);
        textNombre.setText(objects.get(position).getAuthor().getName());

        TextView textFecha = (TextView) itemView.findViewById(R.id.date);

        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        textFecha.setText( formateador.format(objects.get(position).getCreationDate()));


        ImageButton botonBorrar = (ImageButton) itemView.findViewById(R.id.btnDeleteComments);
        ImageButton botonCompartir = (ImageButton) itemView.findViewById(R.id.btnShare);
        if(perfil.getUserEmail().equals(emailGoogle) )
        {
            //ImageButton botonBorrar = (ImageButton) itemView.findViewById(R.id.btnDelete);
            botonBorrar.setTag(position);

            //botonCompartir.setTag(position);
            botonCompartir.setVisibility(View.INVISIBLE);
        }
        else
        {
            // llamada al metodo profilecommentsservice, email, comment, para ver si existe el comentario
            // seria un post
            // devolveria true o false

            // si la llamada a post es true, muestra el boton



            GsonBuilder builder = new GsonBuilder();

            // Register an adapter to manage the date types as long values
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });





            Gson gson = builder.create();

            String json = gson.toJson(objects.get(position), Comment.class);

            json = json.replaceAll("creationDate\":\".*?\"","creationDate\":1426701282429");

            JSONObject coment = null;

            try {

                coment = new JSONObject(json);

            //    System.out.println("comentario " + coment);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            String formatEmail = emailGoogle.replaceAll("\\.", "___");
         //   new RestServicePostWithResp(coment,botonCompartir,position).execute("http://" + IP + ":8080/InftelSocialNetwork-web/webresources/profilecomments/isShare?userEmail=" +formatEmail);



            //ImageButton botonCompartir = (ImageButton) itemView.findViewById(R.id.btnShare);
            //botonCompartir.setTag(position);
            botonBorrar.setVisibility(View.INVISIBLE);

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