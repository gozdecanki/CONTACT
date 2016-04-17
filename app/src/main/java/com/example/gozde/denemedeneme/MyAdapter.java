package com.example.gozde.denemedeneme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by GOZDE on 14.04.2016.
 */
public class MyAdapter extends ArrayAdapter<PERSON> {

    public MyAdapter(Context context, int resource, List<PERSON> persons) {
        super(context, resource, persons);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater myInflater;
            myInflater = LayoutInflater.from(getContext());
            rowView = myInflater.inflate(R.layout.row_layout, null);

            PERSON p_info = getItem(position);

            if (p_info != null) {
                ImageView image = (ImageView) rowView.findViewById(R.id.imageView);
                TextView p_name = (TextView) rowView.findViewById(R.id.twname);
                TextView p_num1 = (TextView) rowView.findViewById(R.id.twnum1);
                TextView p_num2 = (TextView) rowView.findViewById(R.id.twnum2);
           if (p_name!=null){
               p_name.setText(p_info.getP_name());
               p_num1.setText(p_info.getP_num1());
              image.setImageResource(p_info.getP_imageres());

           }

            }

        }
        return rowView;
    }

}
