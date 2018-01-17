package com.mulkearn.kevin.sitecolorgrabber;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String>{

    CustomAdapter(Context context, String[] foods) {
        super(context,R.layout.custom_row , foods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater colorInflator = LayoutInflater.from(getContext());
        View customView = colorInflator.inflate(R.layout.custom_row, parent, false);

        String singelColor = getItem(position);
        TextView hexText = (TextView) customView.findViewById(R.id.hexText);

        hexText.setText(singelColor);
        hexText.setBackgroundColor(Color.parseColor(singelColor));

        return customView;

    }

}
