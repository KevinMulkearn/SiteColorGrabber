package com.mulkearn.kevin.sitecolorgrabber;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WebPageActivity extends AppCompatActivity {

    RelativeLayout mainView;
    WebView websiteView;
    TextView hexText, rgbText, hsvText, colorDisplay;
    DBHandler dbHandler;

    int pixel, height, width;
    String address;
    Bitmap bm;
    Toast t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        websiteView = (WebView) findViewById(R.id.websiteView);
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        hexText = (TextView) findViewById(R.id.hexText);
        rgbText = (TextView) findViewById(R.id.rgbText);
        hsvText = (TextView) findViewById(R.id.hsvText);
        colorDisplay = (TextView) findViewById(R.id.colorDisplay);

        dbHandler = new DBHandler(this, null, null, 1);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        address = getIntent().getStringExtra("url");
        websiteView.getSettings().setJavaScriptEnabled(true);
        websiteView.loadUrl(address);

        t1 = Toast.makeText(WebPageActivity.this, "Loading...", Toast.LENGTH_LONG);
        t1.show();


        websiteView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                //Do something
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                websiteView.measure(View.MeasureSpec.makeMeasureSpec(
                        View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                websiteView.layout(0, 0, websiteView.getMeasuredWidth(),
                        websiteView.getMeasuredHeight());

                websiteView.setDrawingCacheEnabled(true);
                websiteView.buildDrawingCache();

                bm = Bitmap.createBitmap(websiteView.getMeasuredWidth(),
                        websiteView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

                Canvas bigcanvas = new Canvas(bm);
                Paint paint = new Paint();
                int iHeight = bm.getHeight();
                bigcanvas.drawBitmap(bm, 0, iHeight, paint);
                websiteView.draw(bigcanvas);

                if (t1 != null){
                    t1.cancel();
                }
                Toast.makeText(WebPageActivity.this, "Finished Loading", Toast.LENGTH_SHORT).show();
                websiteView.setVisibility(View.VISIBLE);
                getColor();
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    public void getColor(){
        findViewById(R.id.loadingCircle).setVisibility(View.GONE);
        websiteView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int[] viewCoords = new int[2];
                    websiteView.getLocationOnScreen(viewCoords);

                    int touchX = (int) event.getRawX();
                    if (touchX < 0) {
                        touchX = 0;
                    } else if (touchX >= width) {
                        touchX = width - 1;
                    }

                    int touchY = (int) event.getRawY();
                    if (touchY < 0) {
                        touchY = 0;
                    } else if (touchY >= height) {
                        touchY = height - 1;
                    }

                    int imageX = touchX - viewCoords[0];
                    if (imageX >= bm.getWidth()){
                        imageX = bm.getWidth() - 1;
                    }
                    int imageY = touchY - viewCoords[1];
                    if (imageY >= bm.getHeight()){
                        imageY = bm.getHeight() - 1;
                    }

                    pixel = bm.getPixel(imageX, imageY);
                    int redValue = Color.red(pixel);
                    int blueValue = Color.blue(pixel);
                    int greenValue = Color.green(pixel);

                    hexText.setText("Hex: " + rgbToHex(redValue, greenValue, blueValue));
                    rgbText.setText("rgb(" + redValue + ", " + greenValue + ", " + blueValue + ")");
                    hsvText.setText(getHSVValue(redValue, greenValue, blueValue));
                    colorDisplay.setBackgroundColor(pixel);
                }
                return true;
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_web_page, menu);
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
