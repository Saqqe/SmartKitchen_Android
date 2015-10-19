package com.example.saqibfredrik.smartkitchen;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by Saqib Sarker on 2015-10-16.
 */
public class ImageHandler extends AppCompatActivity {

    private static final String TAG = ImageHandler.class.getName();

    private String picURL = "";
    protected ImageView croppedImgView;
    protected CropImageView cropImgView;

    protected ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_cropper_activity);
        init();

        progress        = new ProgressDialog(this);
        cropImgView     = (CropImageView) findViewById(R.id.cropImageView);
        croppedImgView  = (ImageView) findViewById(R.id.croppedImageView);

        //picURL = "https://scontent-ams3-1.xx.fbcdn.net/hphotos-xtf1/v/t1.0-9/10382163_465971536889458_6913905472985494652_n.jpg?oh=44daa29efcc9ecbf20661103518a5385&oe=566F0F1C";

        picURL = "http://i.imgur.com/d5F1k0g.jpg";

        

        imageLoad(picURL);
    }

    /**
     * Init for imageloader
     */
    public void init(){
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

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_crop_button:
                if(cropImgView.getImageBitmap() != null) {
                    //Crop the original image and show the cropped
                    croppedImgView.setImageBitmap(cropImgView.getCroppedBitmap());

                    makeToast("FirstX: " + cropImgView.getFirstX() + " FirstY: " + cropImgView.getFirstY() +
                            " Width: " + cropImgView.getCroppedWidth() + " Height: " + cropImgView.getCroppedHeight());

                    Log.d("TEST", "FirstX: " + cropImgView.getFirstX() + " FirstY: " + cropImgView.getFirstY() +
                            " Width: " + cropImgView.getCroppedWidth() + " Height: " + cropImgView.getCroppedHeight());
                }
                else{
                    makeToast("No image to crop");
                }
                break;
        }
    }//End of onClick

    public void makeToast(String msg){
        Toast toast = Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }//End of makeToast
}
