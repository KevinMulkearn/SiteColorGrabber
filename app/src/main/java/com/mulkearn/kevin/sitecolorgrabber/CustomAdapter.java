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
    TextView hexText, rgbText, hsvText;

    CustomAdapter(Context context, String[] cols) {
        super(context,R.layout.custom_row , cols);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater colorInflator = LayoutInflater.from(getContext());
        View customView = colorInflator.inflate(R.layout.custom_row, parent, false);

        singelColor = getItem(position);
        hexText = (TextView) customView.findViewById(R.id.hexText);
        rgbText = (TextView) customView.findViewById(R.id.rgbText);
        hsvText = (TextView) customView.findViewById(R.id.hsvText);

        int color = Color.parseColor(singelColor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        String rgb = "RGB (" + red + ", " + green + ", " + blue + ")";

        float[] hsv = new float[3];
        Color.RGBToHSV(red, green, blue, hsv);
        float hue = hsv[0];
        float sat = hsv[1] * 100;
        float val = hsv[2] * 100;
        String hsvString = "HSV (" + Math.round(hue) + "\u00b0, " + Math.round(sat) + "%, " + Math.round(val) + "%)";

        hexText.setText(singelColor);
        rgbText.setText(rgb);
        hsvText.setText(hsvString);

        hexText.setBackgroundColor(Color.parseColor(singelColor));
        rgbText.setBackgroundColor(Color.parseColor(singelColor));
        hsvText.setBackgroundColor(Color.parseColor(singelColor));

        return customView;

    }

}
