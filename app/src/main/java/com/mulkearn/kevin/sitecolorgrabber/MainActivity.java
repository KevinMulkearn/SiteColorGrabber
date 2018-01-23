package com.mulkearn.kevin.sitecolorgrabber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.os.StrictMode;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

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
import java.util.HashSet;
import java.util.Set;

import android.os.Handler;
import android.os.Message;

public class MainActivity extends AppCompatActivity {

    EditText addressBar;
    Button searchButton;
    ListView colorList;
    ListAdapter colorAdapter;
    TextView addressName;

    String[] defaultColor  = {"#FFFFFF","#000000"};
    String[] colorArray; //Default Colours
    String siteAddress;
    String message;

    Toast t;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            addressName.setText(siteAddress);
            colorAdapter = new CustomAdapter(MainActivity.this, colorArray); //use my custom adapter
            colorList.setAdapter(colorAdapter);
            t.cancel();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressBar = (EditText) findViewById(R.id.addressBar);
        searchButton = (Button) findViewById(R.id.searchButton);
        colorList = (ListView) findViewById(R.id.listView);
        addressName = (TextView) findViewById(R.id.addressName);

        //On Keyboard Enter Click
        addressBar.setOnKeyListener(
                new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_DPAD_CENTER:
                                case KeyEvent.KEYCODE_ENTER:
                                    onSearch(v);
                                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                    return true;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                }
        );
    }

    public void onSearch(View view){

        Runnable r = new Runnable() {
            @Override
            public void run() {

                siteAddress = addressBar.getText().toString();

                if (pingUrl(siteAddress)){
                    String colors = getWebsite(siteAddress);
                    colorArray = colors.split(", ");
                    message = "Found";
                } else {
                    colorArray = defaultColor;
                    message = "Invalid URL";
                }
                handler.sendEmptyMessage(0);
            }
        };

        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        t = Toast.makeText(MainActivity.this, "Please wait...", Toast.LENGTH_LONG);
        t.show();

        Thread myThread = new Thread(r);
        myThread.start();
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

        Collections.sort(allColors); //sort array

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