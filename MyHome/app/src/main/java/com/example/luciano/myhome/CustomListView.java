package com.example.luciano.myhome;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Luciano on 15/04/2017.
 */

public class CustomListView extends ArrayAdapter<String> {

    public final Activity context;
    public final String[] title;
    public final String[] subTitle;
    public final Integer[] image;

    public CustomListView(Activity context, Integer[] image, String[] title, String[] subTitle) {
        super(context, R.layout.cutom_listview, title);
        this.context = context;
        this.title = title;
        this.subTitle = subTitle;
        this.image = image;
    }



    public View getView (int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.cutom_listview, null, true);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageCustomListView);
        TextView txtTitle = (TextView)rowView.findViewById(R.id.titleCustomListView);
        TextView txtSubTitle = (TextView)rowView.findViewById(R.id.subTitleCustomListView);
        imageView.setImageResource(image[position]);
        txtTitle.setText(title[position]);
        txtSubTitle.setText(subTitle[position]);
        return rowView;
    }

}
