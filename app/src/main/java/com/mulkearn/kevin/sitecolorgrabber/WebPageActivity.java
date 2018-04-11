package com.mulkearn.kevin.sitecolorgrabber;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class WebPageActivity extends AppCompatActivity {

    ToggleButton toggleScrollView;
    ScrollView pageScrollView;
    WebView websiteView;
    ImageView imageView;
    TextView hexText, rgbText, hsvText, colorDisplay;
    Toast t1;

    DBHandler dbHandler;

    String address;
    Bitmap bitmap;
    boolean loaded = false;

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        pageScrollView = (ScrollView) findViewById(R.id.pageScrollView);
        websiteView = (WebView) findViewById(R.id.websiteView);
        imageView = (ImageView) findViewById(R.id.imageView);
        hexText = (TextView) findViewById(R.id.hexText);
        rgbText = (TextView) findViewById(R.id.rgbText);
        hsvText = (TextView) findViewById(R.id.hsvText);
        colorDisplay = (TextView) findViewById(R.id.colorDisplay);
        toggleScrollView = (ToggleButton) findViewById(R.id.toggleScrollView);

        //Hide toggle button text
        toggleScrollView.setText(null);
        toggleScrollView.setTextOn(null);
        toggleScrollView.setTextOff(null);
        toggleScrollView.setVisibility(View.INVISIBLE);

        dbHandler = new DBHandler(this, null, null, 1);

        t1 = Toast.makeText(WebPageActivity.this, "Loading...", Toast.LENGTH_LONG);
        t1.show();

        address = getIntent().getStringExtra("url");;
        websiteView.getSettings().setJavaScriptEnabled(true);
        websiteView.loadUrl(address);

        websiteView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                pageScrollView.setVisibility(View.INVISIBLE);
                websiteView.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                websiteView.measure(View.MeasureSpec.makeMeasureSpec(
                        View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                websiteView.layout(0, 0, websiteView.getMeasuredWidth(),
                        websiteView.getMeasuredHeight());

                pageScrollView.setVisibility(View.VISIBLE);
                websiteView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);

                findViewById(R.id.loadingCircle).setVisibility(View.GONE);
                if (t1 != null){
                    t1.cancel();
                }
                Toast.makeText(WebPageActivity.this, "Finished Loading", Toast.LENGTH_SHORT).show();
                loaded = true;
                invalidateOptionsMenu();//Recall menu create function
                toggleScrollView.setVisibility(View.VISIBLE);
            }
        });

        websiteView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true; //True if the listener has consumed the event, false otherwise.
            }
        });

        toggleScrollView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pageScrollView.setDrawingCacheEnabled(true);
                    pageScrollView.buildDrawingCache();
                    bitmap = pageScrollView.getDrawingCache();
                    imageView.setImageBitmap(bitmap);

                    imageView.setVisibility(View.VISIBLE);
                    pageScrollView.setVisibility(View.INVISIBLE);
                    websiteView.setVisibility(View.INVISIBLE);
                } else {
                    pageScrollView.setDrawingCacheEnabled(false);
                    bitmap = null;

                    pageScrollView.setVisibility(View.VISIBLE);
                    websiteView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        });


        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                int pixel = 12247244;
                if(bitmap != null){
                    pixel = bitmap.getPixel((int) x, (int) y);
                }

                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);

                hexText.setText("Hex: " + rgbToHex(redValue, greenValue, blueValue));
                rgbText.setText("rgb(" + redValue + ", " + greenValue + ", " + blueValue + ")");
                hsvText.setText(getHSVValue(redValue, greenValue, blueValue));
                colorDisplay.setBackgroundColor(pixel);

                return false;
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_web_page, menu);
        if (loaded == false){
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        } else {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i_search = new Intent(this, SavedColorActivity.class);
                NavUtils.navigateUpTo(this, i_search);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.save:
                String saveColor = hexText.getText().toString();
                saveColor = saveColor.substring(saveColor.indexOf("#"),saveColor.length());
                Colors color = new Colors(saveColor);
                dbHandler.addColor(color);
                Toast.makeText(WebPageActivity.this, saveColor + " Saved", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.reload:
                Intent i_reload = new Intent(this, WebPageActivity.class);
                i_reload.putExtra("url", address);
                startActivity(i_reload);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String rgbToHex(int r, int g, int b){
        return String.format("#%02X%02X%02X",r, g, b);
    }

    public String getHSVValue(int r, int g, int b){
        float[] hsv = new float[3];
        Color.RGBToHSV(r, g, b, hsv);
        float h = hsv[0];
        float s = hsv[1] * 100;
        float v = hsv[2] * 100;
        String hue = Integer.toString((int) h) + "\u00b0";
        String sat = Integer.toString((int) s) + "%";
        String val = Integer.toString((int) v) + "%";

        return "hsv(" + hue + ", " + sat + ", " + val + ")";
    }
}
