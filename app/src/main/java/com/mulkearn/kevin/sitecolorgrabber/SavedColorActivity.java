package com.mulkearn.kevin.sitecolorgrabber;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SavedColorActivity extends AppCompatActivity{

    TextView colorsText;
    EditText colorInput;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_color);

        colorsText = (TextView) findViewById(R.id.colorsText);
        colorInput = (EditText) findViewById(R.id.colorInput);
        dbHandler = new DBHandler(this, null, null, 1);//check constructor for more info

        printDatabase();
    }

    public void saveButtonClick(View view){
        Colors color = new Colors(colorInput.getText().toString());
        dbHandler.addColor(color);
        printDatabase();
    }

    public void deleteButtonClick(View view){
        String inputColor = colorInput.getText().toString();
        dbHandler.deleteColor(inputColor);
        printDatabase();
    }

    public void clearAllClicked(View view){
        dbHandler.clearColors();
        printDatabase();
    }

    public void printDatabase(){
        String dbString = dbHandler.databaseToString();
        colorsText.setText(dbString);
        colorInput.setText("");
    }

}
