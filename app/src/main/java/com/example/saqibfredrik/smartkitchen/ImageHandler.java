package com.example.saqibfredrik.smartkitchen;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Saqib Sarker on 2015-10-16.
 */
public class ImageHandler extends AppCompatActivity implements Observer{

    private static final String PIC_NAME        = "picName";
    private static final String PIC_URL         = "picURL";
    private static final String ITEM_NAME       = "itemName";

    private static final String TAG = ImageHandler.class.getName();

    private String picURL = "";

    protected ImageView croppedImgView;
    protected CropImageView cropImgView;

    protected PhotoViewAttacher croppedImgViewAttacher;
    protected PhotoViewAttacher cropImgViewAttacher;

    protected ProgressDialog progress;

    private JsonHandler jsonHandler;
    private JSONObject jsonObject;

    private TextView textToShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_cropper_activity);
        initImageLoader();

        progress                = new ProgressDialog(this);
        cropImgView             = (CropImageView) findViewById(R.id.cropImageView);
        croppedImgView          = (ImageView) findViewById(R.id.croppedImageView);
        croppedImgViewAttacher  = new PhotoViewAttacher(croppedImgView);
        cropImgViewAttacher     = new PhotoViewAttacher(cropImgView);
        textToShow              = (TextView) findViewById(R.id.textView_UnderPhoto);

        jsonHandler = new JsonHandler();
        jsonHandler.addObserver(this);

        jsonObject  = new JSONObject();

        //getAndSetImage();

    }//End of onCreate

    private void getAndSetImage(){
        Log.d(TAG, "Outside try");
        try {
            jsonObject = jsonHandler.getJsonObject();
            Log.d(TAG, "Inside try, outside IF");
            if(jsonObject != null) {
                Log.d(TAG, jsonObject.toString());
                imageLoad(jsonObject.getString(PIC_URL));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }//End of getAndSetImage


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                /**
                 * @TODO Check for images from server
                 */
                getAndSetImage();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable observable, Object data) {
        getAndSetImage();
    }

    /**
     * Check intent for url
     */
    public boolean checkIntentForURL(){
        Bundle extras = getIntent().getExtras();
        if(extras == null){
            Log.d(TAG, "Nothing extra in intent");
            return false;
        }
        else{
            picURL = extras.getString("url");
            return true;
        }
    }//End of checkIntentForURL

    /**
     * Init for imageloader
     */
    public void initImageLoader(){
        //UniversalImageLoader initiation
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * imageLoader
     * @param imageUri
     */
    public void imageLoad(String imageUri){
        // Load image, decode it to Bitmap and return Bitmap to callback
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.loadImage(imageUri, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
                if (!progress.isShowing()) {
                    progress.setTitle("Loading");
                    progress.setMessage("Wait while loading...");
                    progress.show();
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                //SetImage
                cropImgView.setImageBitmap(loadedImage);
                cropImgViewAttacher.update();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                makeToast("Something went wrong downloading from: " + imageUri);
                if (progress.isShowing()) {
                    progress.dismiss();
                }
            }
        });
    }//End of imageLoad

    /**
     * OnClickListener
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_crop_button:
                if(cropImgView.getImageBitmap() != null) {
                    //Crop the original image and show the cropped
                    croppedImgView.setImageBitmap(cropImgView.getCroppedBitmap());
                    croppedImgViewAttacher.update();

                    Log.d(TAG, "FirstX: " + cropImgView.getFirstX() + " FirstY: " + cropImgView.getFirstY() +
                            " Width: " + cropImgView.getCroppedWidth() + " Height: " + cropImgView.getCroppedHeight());
                }
                else{
                    makeToast("There is no image to crop!");
                }
                break;
            case R.id.btn_saveButton:
                //Check the bottom ImageView if there is a image!
                if(croppedImgView.getDrawable() != null){
                    showInputDialog();
                }
                else{
                    makeToast("You need to crop the image first!");
                }

                break;
            default:
                break;
        }//End of switch
    }//End of onClick

    /**
     * show a Dialog for input of object name from user!
     */
    protected void showInputDialog(){
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(ImageHandler.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_save, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageHandler.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.editText_userInput);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /**
                         * TODO set information on right JSONObject!
                         */
                        try {
                            jsonObject.put(ITEM_NAME, editText.getText().toString().trim());
                            jsonObject.put("x", cropImgView.getFirstX());
                            jsonObject.put("y", cropImgView.getFirstY());
                            jsonObject.put("width", cropImgView.getCroppedWidth());
                            jsonObject.put("height", cropImgView.getCroppedHeight());

                            jsonHandler.putJsonObject(jsonObject);

                            getAndSetImage();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }//End of showInputDialog()

    public void makeToast(String msg){
        Toast toast = Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }//End of makeToast


}
