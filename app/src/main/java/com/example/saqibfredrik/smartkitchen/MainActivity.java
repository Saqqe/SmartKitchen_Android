package com.example.saqibfredrik.smartkitchen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;


public class MainActivity extends AppCompatActivity {

    private MorphingButton btnUnknown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUnknown = (MorphingButton) findViewById(R.id.btn_unknown);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
    }



    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_unknown:
                Intent myIntent = new Intent(MainActivity.this, ImageHandler.class);
                MainActivity.this.startActivity(myIntent);
                break;
            default:
                break;

        }
    }

    public void makeToast(String msg){
        Toast toast = Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }//End of makeToast
}//End of MainActivity



