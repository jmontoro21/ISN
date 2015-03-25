package com.inftel.isn.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.inftel.isn.R;
import com.inftel.isn.adapter.SimpleArrayAdapter;

import java.util.ArrayList;

public class GroupFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group, container, false);
        ListView eventListView = (ListView) v.findViewById(R.id.itemList);

        //Array estatico provisional
        ArrayList objetos = new ArrayList<String>();
        objetos.add("grupo1");objetos.add("grupo2");objetos.add("grupo3");objetos.add("grupo4");
        objetos.add("grupo1");objetos.add("grupo2");objetos.add("grupo3");objetos.add("grupo4");
        objetos.add("grupo1");objetos.add("grupo2");objetos.add("grupo3");objetos.add("grupo4");
        objetos.add("grupo1");objetos.add("grupo2");objetos.add("grupo3");objetos.add("grupo4");

        SimpleArrayAdapter adapter = new SimpleArrayAdapter(objetos, getActivity());
        eventListView.setAdapter(adapter);
        return v;
    }
}
