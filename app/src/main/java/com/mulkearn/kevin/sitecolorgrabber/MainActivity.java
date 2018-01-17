package com.mulkearn.kevin.sitecolorgrabber;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements SearchSectionFragment.SearchSectionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //called by search bar fragment when button click
    @Override
    public void createMeme(String siteAddress) {

        final ListView colorListView = (ListView) findViewById(R.id.colorListView); //ListView reference
        String[] defaultColor = {"#FFFFFF","#000000"};
        String[] colorArray; //Default Colours

        ColorListFragment colorListFragment = (ColorListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
        colorListFragment.setSiteText(siteAddress);

        String message;
        if (pingUrl(siteAddress) == true){
            String colors = getWebsite(siteAddress);
            colorArray = colors.split(", ");
            message = "Searching";
        } else {
            colorArray = defaultColor;
            message = "Invalid URL";
        }

        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        ListAdapter colorAdapter = new CustomAdapter(this, colorArray); //Use adapter I created
        colorListView.setAdapter(colorAdapter);
    }


    public String getWebsite(String website){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(website);

        ArrayList<String> allColors = new ArrayList<>();
        allColors.add("#000000");
        allColors.add("#FFFFFF");
        int index;
        int lineLength;
        String hexTest;
        String sorted;

        try{
            HttpResponse response;
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(instream, "windows-1251"),8);
            String inputLine;

            while((inputLine = br.readLine()) != null){
                index = inputLine.indexOf("#");

                while (index >= 0) {
                    lineLength = inputLine.length();
                    if (index < lineLength-7){
                        hexTest = inputLine.substring(index+1, index+7); //***check length validation ***
                        hexTest = hexTest.toUpperCase();

                        if(hexTest.matches("[0-9a-fA-F]+")){
                            allColors.add("#" + hexTest);
                        }
                    }
                    index = inputLine.indexOf("#", index + 1);
                }
            }
            instream.close();

        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
        Set<String> hs = new HashSet<>(); //Create hash set
        hs.addAll(allColors); //add all elements of array to hash set (removes duplicates)
        allColors.clear(); //clear array
        allColors.addAll(hs); //add all elements of hash set back to array

        //Collections.sort(allColors); //sort array

        Collections.sort(allColors, new Comparator<String>() {
            @Override
            public int compare(String c1, String c2) {
                int intColl = Color.parseColor(c1);
                int intCol2 = Color.parseColor(c2);
                if (Color.red(intColl) < Color.red(intCol2)) {
                    return -1;
                }
                if (Color.red(intColl) > Color.red(intCol2)) {
                    return 1;
                }
                if (Color.green(intColl) < Color.green(intCol2)) {
                    return -1;
                }
                if (Color.green(intColl) > Color.green(intCol2)) {
                    return 1;
                }
                if (Color.blue(intColl) < Color.blue(intCol2)) {
                    return -1;
                }
                if (Color.blue(intColl) > Color.blue(intCol2)) {
                    return 1;
                }
                return 0;
            }
        });

        sorted = allColors.toString();
        sorted = sorted.substring(1,sorted.length()-1);

        return sorted;
    }


    public static boolean pingUrl(String testUrl) {
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(testUrl);
            HttpResponse response;
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
