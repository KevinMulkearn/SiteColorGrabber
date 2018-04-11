package com.mulkearn.kevin.sitecolorgrabber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startSearchActivity(View view) {
        Intent i_search = new Intent(this, SearchActivity.class);
        startActivity(i_search);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void startSavedActivity(View view) {
        Intent i_saved = new Intent(this, SavedColorActivity.class);
        startActivity(i_saved);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void startAboutActivity(View view) {
        Intent i_about = new Intent(this, AboutActivity.class);
        startActivity(i_about);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void startTestActivity(View view) {
        Intent i_test = new Intent(this, TestActivity.class);
        startActivity(i_test);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}