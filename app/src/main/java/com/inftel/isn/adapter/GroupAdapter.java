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
import android.widget.Toast;

import com.inftel.isn.R;
import com.inftel.isn.activity.LoginGoogleActivity;
import com.inftel.isn.model.Group;
import com.inftel.isn.request.DownloadImageTask;
import com.inftel.isn.request.RestServiceExitGroup;

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
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.my_group_item, null);
        }

        TextView textTitulo = (TextView) convertView.findViewById(R.id.titulo_item);
        final Group group= objects.get(position);
        textTitulo.setText(group.getName());
        TextView textDescription = (TextView) convertView.findViewById(R.id.descripcion_item);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.itemImg);
        TextView textFechaIni = (TextView) convertView.findViewById(R.id.fecha);

        SharedPreferences prefs = context.getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        final String email = prefs.getString(LoginGoogleActivity.USER_KEY, "");

        ImageButton buttonDelete = (ImageButton) convertView.findViewById(R.id.deleteImg);
        ImageButton buttonExit = (ImageButton) convertView.findViewById(R.id.exitImg);

        if(group.getAdmin().equals(email)) {
           buttonDelete.setVisibility(View.VISIBLE);
           buttonExit.setVisibility(View.INVISIBLE);
        } else {
            buttonDelete.setVisibility(View.INVISIBLE);
            buttonExit.setVisibility(View.VISIBLE);
        }

        buttonDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               // new RestServiceDeleteGroup(group).execute(email);
              //  Toast.makeText(context, "Has eliminado el grupo " + group.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new RestServiceExitGroup(group).execute(email);
                Toast.makeText(context, "Has dejado el grupo " + group.getName(), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });

        String image =group.getImageUrl();

        new DownloadImageTask(imageView).execute(image);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        textFechaIni.setText(dateFormat.format(new Date()));

        textDescription.setText("Descripcion");
        return convertView;
    }
}
