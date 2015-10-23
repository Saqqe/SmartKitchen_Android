package com.example.saqibfredrik.smartkitchen;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Saqib Sarker on 2015-10-23.
 */
public class JsonHandler {

    private static final String TAG = JsonHandler.class.getName();

    private JSONArray jsonArrayFromParse;
    private JSONArray jsonArrayToSave;
    private JSONObject jsonObject;
    private int jsonArrayLen;
    private int index;

    /**
     * Constructor
     */
    public JsonHandler() {
        this.jsonArrayFromParse     = new JSONArray();
        this.jsonArrayLen           = this.jsonArrayFromParse.length();
        this.jsonArrayToSave        = new JSONArray();
        this.index                  = 0;

        init();

    }//End of constructor

    /**
     * download data from parse.com
     */
    private void init(){
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("UnknownImage");
        query.getInBackground("QjestMNLzB", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ParseFile file = (ParseFile) object.get("json");
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            if (e == null) {
                                //Done
                                try {
                                    String str = new String(bytes);
                                    jsonArrayFromParse  = new JSONArray(str);
                                    jsonArrayLen        = jsonArrayFromParse.length();


                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                //Went Wrong
                                Log.d(TAG, "Went wrong in file.getDataInBackground");
                            }
                        }
                    });
                } else {
                    // something went wrong
                    Log.d(TAG, "Went wrong in querry.getInBackground");
                }
            }
        });
    }//End of getJson

    /**
     *
     * @return JSONObject
     */
    public JSONObject getJsonObject(){
        if( index < jsonArrayLen ) {
            try {
                return this.jsonArrayFromParse.getJSONObject(index++);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }//End of getJsonObject

    /**
     *
     * @param jObj
     */
    public void putJsonObject(JSONObject jObj){
        this.jsonArrayToSave.put(jObj);

        if( index == (jsonArrayLen - 1) ){
            uploadAndUpdateParse();
        }
    }//End of putJsonObject

    /**
     * update pic information
     */
    private void uploadAndUpdateParse(){
        if( this.jsonArrayToSave.length() >  0 ) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ConfirmedImages");
            // Retrieve the object by id
            query.getInBackground("uXFlSqSVMu", new GetCallback<ParseObject>() {
                public void done(final ParseObject object, ParseException e) {
                    if (e == null) {
                        // Now let's update it with some new data.
                        byte[] data = jsonArrayToSave.toString().getBytes(); // hardCoded what to save!
                        final ParseFile file = new ParseFile("ConfirmedImages.json", data);
                        file.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    //Done
                                    object.put("json", file);
                                    object.saveInBackground();
                                }
                            }//End of callback file save
                        });//End of file save
                    }
                }//End of callback query
            });//End of query
        }//End of if( this.jsonArrayToSAve.length() >  0 )
    }//End of updateParseIWthNewInfo


}
