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

public class NotaFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nota, container, false);
        ListView eventListView = (ListView) v.findViewById(R.id.itemList);

        //Array estatico provisional
        ArrayList objetos = new ArrayList<String>();


        SimpleArrayAdapter adapter = new SimpleArrayAdapter(objetos, getActivity());
        eventListView.setAdapter(adapter);
        return v;
    }
}
