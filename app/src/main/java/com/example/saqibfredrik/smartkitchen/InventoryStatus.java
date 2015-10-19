package com.example.saqibfredrik.smartkitchen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Saqib Sarker on 2015-10-16.
 */
public class InventoryStatus extends AppCompatActivity{

    private TextView inventoryTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_status);

        inventoryTextView = (TextView) findViewById(R.id.textView_inventory);
        inventoryTextView.setText("yolo swag!");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.getInBackground("Qvd9aQNSO0", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                } else {
                    // something went wrong
                }
            }
        });

        makeToast("Hello, we are in Inventory!");

    }


    public void makeToast(String msg){
        Toast toast = Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }//End of makeToast
}
