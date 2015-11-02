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

import java.util.Observable;

/**
 * Created by Saqib Sarker on 2015-10-23.
 */
public class JsonHandler extends Observable{

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
     *
     * @return JSONObject
     */
    public JSONObject getJsonObject(){
        if( !this.jsonArrayFromParse.isNull(index) ) {
            try {
                return this.jsonArrayFromParse.getJSONObject(index);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        updateDeleteJsonObj();
        return null;
    }//End of getJsonObject

    /**
     *
     * @param jObj
     */
    public void putJsonObject(JSONObject jObj){
        this.jsonArrayToSave.put(jObj);
        this.jsonArrayFromParse.remove(index);

        if( index == (jsonArrayLen - 1) ){
            uploadAndUpdateParse();
        }
    }//End of putJsonObject

    /**
     * update pic information
     */
    public void uploadAndUpdateParse(){
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


    /**----------------PRIVATE----------------**/

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
                                    setChanged();
                                    notifyObservers();

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

    public void updateDeleteJsonObj(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UnknownImage");
        // Retrieve the object by id
        query.getInBackground("QjestMNLzB", new GetCallback<ParseObject>() {
            public void done(final ParseObject object, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data.
                    byte[] data = jsonArrayFromParse.toString().getBytes(); // hardCoded what to save!
                    final ParseFile file = new ParseFile("ImagesURLS.json", data);
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
    }//End of updateDeleteJsonObj
}
