package com.example.luciano.myhome;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Luciano on 01/04/2017.
 */

public class Menu3 extends Fragment {

    Integer[] img = {R.drawable.ligth, R.mipmap.ic_launcher};

    public static String[] title = {"Android1", "Android2"};
    public static String[] subTitle = {"Android1", "Android2"};

    ListView listView;
    CustomListView adapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Menu 3");

        listView = (ListView)getActivity().findViewById(R.id.listViewMenu3);

        adapter = new CustomListView(getActivity(), img, title, subTitle);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Click on: "+position, Toast.LENGTH_SHORT).show();
                setText(position);
            }
        });
    }

    public void setText(int position){
        date = new Date();
        subTitle[position] = dateFormat.format(date);
        adapter = new CustomListView(getActivity(), img, title, subTitle);
        listView.setAdapter(adapter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu3, container, false);
    }
}
