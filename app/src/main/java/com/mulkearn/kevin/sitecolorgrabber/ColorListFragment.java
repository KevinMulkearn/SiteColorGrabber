package com.mulkearn.kevin.sitecolorgrabber;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ColorListFragment extends Fragment{

    public static TextView siteTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.color_list_fragment, container, false);
        siteTextView = (TextView) view.findViewById(R.id.topTextView);
        return view;
    }

    public void setSiteText(String siteText){
        siteTextView.setText(siteText);
    }

}
