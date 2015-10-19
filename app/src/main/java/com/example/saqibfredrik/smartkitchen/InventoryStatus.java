package com.example.saqibfredrik.smartkitchen;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Set;


/**
 * Created by Saqib Sarker on 2015-10-16.
 */
public class InventoryStatus extends AppCompatActivity{

    private static final String TAG = InventoryStatus.class.getName();


    private ArrayList<String> stringArrayList;
    private ListView parseItemList;
    private ArrayAdapter<String> listViewAdapter;

    protected ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_status);

        parseItemList = (ListView) findViewById(R.id.listView_itemList);
        parseItemList.setClickable(false);

        stringArrayList = new ArrayList<>();
        listViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        parseItemList.setAdapter(listViewAdapter);

        progress = new ProgressDialog(this);

        getDataFromParse();

    }

    private void updateListView(ArrayList<String> arrayList){
        listViewAdapter.clear();
        listViewAdapter.addAll(arrayList);
        parseItemList.setAdapter(listViewAdapter);

        //Dismiss the loading window
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }


    public void getDataFromParse(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LagerStatus");

        //Show a loading window
        if (!progress.isShowing()) {
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.show();
        }

        query.getInBackground(getString(R.string.ParseObjectID), new GetCallback<ParseObject>() {

            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                    Set<String> items    = object.keySet();
                    for(String s : items){
                        Log.d(TAG, "Items: " + s + " Key: " + object.getInt(s));
                        stringArrayList.add(s + ": " + object.getInt(s));
                    }
                    updateListView(stringArrayList);

                } else {
                    // something went wrong
                    Log.d(TAG, "Something went Wrong");
                    //Dismiss the loading window
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }
                }
            }
        });
    }


    public void makeToast(String msg){
        Toast toast = Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }//End of makeToast
}
