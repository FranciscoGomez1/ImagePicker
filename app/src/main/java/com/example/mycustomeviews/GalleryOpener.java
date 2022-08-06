package com.example.mycustomeviews;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;


import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GalleryOpener extends AppCompatActivity{
    //need the uri
    public static Uri galleryUri = null;
    private Uri cam_uri;

    private static ComponentActivity activity;
    private ActivityResultLauncher<String> mGetContent;

    private ActivityResultLauncher<Intent> startCamera;

    private ImageHasbeenSelected imageHasbeenSelected;

    private int ALL_PERMISSIONS = 101;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};



    public GalleryOpener(ComponentActivity activity) {
        this.activity = activity;
        this.mGetContent = activity.registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Handle the returned Uri
                    //This is used in case the user hits the back button without selecting an image
                    if( uri != null) {
                        galleryUri = uri;
                        //Call Listener
                        imageHasbeenSelected.imageUpdate();
                    }

                });

        this.startCamera = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // There are no request codes
                        galleryUri = cam_uri;
                        imageHasbeenSelected.imageUpdate();

                    }
                });
    }
    public void launchGallery(){
        Log.d("URI", "ButtonPress");
        mGetContent.launch("image/*");

    }

    public void openPhotoApp(){
        requestCameraApp();
    }

    public void requestCameraApp(){
        if (hasPermissions(permissions)) {
            pickCamera();

        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                activity.requestPermissions(permissions, ALL_PERMISSIONS);
            }
        }
    }

    public static boolean hasPermissions(String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public void pickCamera() {
        Log.d("OPENCAMERA", "openCamera");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        cam_uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_uri);
        startCamera.launch(cameraIntent);

    }


    public void galleryOpenerListener( ImageHasbeenSelected imageHasbeenSelected){
        this.imageHasbeenSelected = imageHasbeenSelected;
    }

    public interface ImageHasbeenSelected{
        void imageUpdate();
    }

}
