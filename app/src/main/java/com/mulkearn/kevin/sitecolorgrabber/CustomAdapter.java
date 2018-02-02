package com.mulkearn.kevin.sitecolorgrabber;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends ArrayAdapter<String>{

    String singelColor;
    TextView hexText;

    CustomAdapter(Context context, String[] cols) {
        super(context,R.layout.custom_row , cols);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater colorInflator = LayoutInflater.from(getContext());
        View customView = colorInflator.inflate(R.layout.custom_row, parent, false);

        singelColor = getItem(position);
        hexText = (TextView) customView.findViewById(R.id.hexText);

        hexText.setText(singelColor);
        hexText.setBackgroundColor(Color.parseColor(singelColor));

        return customView;

    }

}
