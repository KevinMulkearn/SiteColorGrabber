package com.mulkearn.kevin.sitecolorgrabber;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SavedColorActivity extends AppCompatActivity{

    TextView colorsText;
    DBHandler dbHandler;
    ListView savedColorsList;
    ListAdapter colorAdapter;

    String dbString;
    String[] savedColorsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_color);

        colorsText = (TextView) findViewById(R.id.colorsText);
        savedColorsList = (ListView) findViewById(R.id.savedColorsList);

        dbHandler = new DBHandler(this, null, null, 1);

        printDatabase();
    }


    public void deleteButtonClick(View view){
//        String inputColor = colorInput.getText().toString();
//        dbHandler.deleteColor(inputColor);
//        printDatabase();
    }

    public void clearAllClicked(View view){
        dbHandler.clearColors();
        //Add black and white list can't be empty
        Colors color1 = new Colors("#FFFFFF");
        dbHandler.addColor(color1);
        Colors color2 = new Colors("#000000");
        dbHandler.addColor(color2);
        printDatabase();
    }

    public void printDatabase(){
        if(dbHandler.databaseToString().length() > 4){ //At least one color
            dbString = dbHandler.databaseToString();
            dbString = dbString.substring(0,dbString.length()-1); //Remove , at end
        } else {
            dbString = "#FFFFFF,#000000";
        }
        colorsText.setText(dbString);//Remove when done
        savedColorsArray = dbString.split(",");
        colorAdapter = new CustomAdapter(SavedColorActivity.this, savedColorsArray); //use my custom adapter
        savedColorsList.setAdapter(colorAdapter); //set list colors
    }

}
