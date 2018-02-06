package com.mulkearn.kevin.sitecolorgrabber;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;

public class AboutActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void onReturnClick(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


}
