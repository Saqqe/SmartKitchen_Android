package com.example.saqibfredrik.smartkitchen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected Button btn_crop, btn_save;
    protected TextView txt;

    private DrawingView dv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_crop = (Button) findViewById(R.id.btn_crop);
        btn_save = (Button) findViewById(R.id.btn_save);
        txt      = (TextView) findViewById(R.id.textView);

    }//End of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }//End of onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }//End of onOptionItemSelected


    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_crop:

                /**
                 * Call DrawingView for cropping the image
                 */
                break;
            case R.id.btn_save:

                /**
                 * Save the image and sen it to RPi for training !!
                 */
            default:
                break;
        }
    }
}//End of MainActivity
