package com.example.saqibfredrik.smartkitchen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.isseiaoki.simplecropview.CropImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    protected CropImageView cropImageView;
    protected ImageView croppedImageView;
    protected ProgressDialog progress;

    protected String picURL = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        cropImageView    = (CropImageView) findViewById(R.id.cropImageView);
        croppedImageView = (ImageView) findViewById(R.id.croppedImageView);
        progress         = new ProgressDialog(this);

        picURL = "https://scontent-ams3-1.xx.fbcdn.net/hphotos-xtf1/v/t1.0-9/10382163_465971536889458_6913905472985494652_n.jpg?oh=44daa29efcc9ecbf20661103518a5385&oe=566F0F1C";

        imageLoad(picURL);

    }//End of onCreate

    public void init(){
        //UniversalImageLoader initiation
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
    }

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
            case R.id.crop_button:
                croppedImageView.setImageBitmap(cropImageView.getCroppedBitmap());
                break;
            case R.id.saveButton:
                /*View view = (LayoutInflater.from(MainActivity.this)).inflate(R.layout.dialog_save, null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setView(view);

                final EditText userInput = (EditText) view.findViewById(R.id.userInput);

                alertBuilder.setCancelable(true).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParseObject imageObject = new ParseObject("CroppedImage");
                        String picName = userInput.getText().toString();
                        imageObject.put(picName, cropImageView.getCroppedBitmap());
                        imageObject.saveInBackground();
                        makeToast("Saved iamge with name: " + picName);
                    }
                });
                Dialog dialog = alertBuilder.create();
                dialog.show();*/
                if(cropImageView.getCroppedBitmap() != null) {
                    View view = (LayoutInflater.from(MainActivity.this)).inflate(R.layout.dialog_save, null);
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertBuilder.setView(view);

                    final EditText userInput = (EditText) view.findViewById(R.id.userInput);

                    alertBuilder.setCancelable(true).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                ParseObject parseObject = new ParseObject("CroppedImage");

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                                Bitmap bitmap = ((BitmapDrawable)croppedImageView.getDrawable()).getBitmap();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                                String picName = userInput.getText().toString();
                                picName = picName.trim();
                                picName += ".jpg";

                                ParseFile parseFile = new ParseFile(picName, stream.toByteArray());

                                parseFile.save();
                                parseObject.put("FileName", parseFile);
                                parseObject.saveInBackground();
                                makeToast("Saved!");

                            } catch (android.net.ParseException ignored) {

                            } catch (com.parse.ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    Dialog dialog = alertBuilder.create();
                    dialog.show();
                }
                break;
            default:
                break;
        }
    }

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
                cropImageView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                makeToast("Something went wrong downloading from: " + imageUri);
            }
        });
    }//End of imageLoad

    public void makeToast(String msg){
        Toast toast = Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }//End of makeToast

}//End of MainActivity
