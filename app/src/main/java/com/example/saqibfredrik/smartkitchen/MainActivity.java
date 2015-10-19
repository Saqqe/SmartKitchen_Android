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

    private static final String TAG = MainActivity.class.getName();

    private MorphingButton btnShowUnknown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowUnknown = (MorphingButton) findViewById(R.id.btn_showUnknown);
    }


    /**
     * onClick event handler
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_showUnknown:
                Intent myIntent = new Intent(MainActivity.this, ImageHandler.class);
                MainActivity.this.startActivity(myIntent);
                break;
            case R.id.btn_showInventory:
                Intent inventoryIntent = new Intent(MainActivity.this, InventoryStatus.class);
                MainActivity.this.startActivity(inventoryIntent);
                break;
            default:
                break;

        }
    }//End of onclick

    public void makeToast(String msg){
        Toast toast = Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }//End of makeToast
}//End of MainActivity



