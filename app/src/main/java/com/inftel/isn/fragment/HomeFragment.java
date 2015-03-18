package com.inftel.isn.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.inftel.isn.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ListView eventListView = (ListView) v.findViewById(R.id.itemList);

        //Array estatico provisional
        ArrayList objetos = new ArrayList<String>();
        objetos.add("paco");objetos.add("hola");objetos.add("hola");objetos.add("hola");objetos.add("paco");objetos.add("paco");

        SimpleArrayAdapter adapter = new SimpleArrayAdapter(objetos);
        eventListView.setAdapter(adapter);
        return v;
    }

    public class SimpleArrayAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ArrayList<String> objects;

        public SimpleArrayAdapter(ArrayList<String> objetos) {
            inflater = LayoutInflater.from(getActivity());
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
            View itemView = inflater.inflate(R.layout.list_item, parent, false);
            TextView textTitulo = (TextView) itemView.findViewById(R.id.titulo_item);
            TextView textDescription = (TextView) itemView.findViewById(R.id.descripcion_item);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.itemImg);
            TextView textFechaIni = (TextView) itemView.findViewById(R.id.fecha);

            Picasso.with(getActivity()).load(R.drawable.user).into(imageView);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            textFechaIni.setText(dateFormat.format(new Date()));
            textTitulo.setText("Titulo");
            textDescription.setText("Descripcion");
            return itemView;
        }
    }
}
