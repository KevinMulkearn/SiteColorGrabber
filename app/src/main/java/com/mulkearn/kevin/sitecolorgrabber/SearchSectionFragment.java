package com.mulkearn.kevin.sitecolorgrabber;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

public class SearchSectionFragment extends Fragment{
    private static EditText searchBarInput;
    SearchSectionListener activityCommander;

    public interface SearchSectionListener{
        public void createMeme(String siteAddress);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            activityCommander = (SearchSectionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_bar_fragment, container, false);
        searchBarInput = (EditText) view.findViewById(R.id.searchBarInput);
        final Button button = (Button) view.findViewById(R.id.searchButton);

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonClicked(v);
                    }
                }
        );
        return view;
    }


    public void buttonClicked(View view){
        activityCommander.createMeme(searchBarInput.getText().toString());
    }

}
