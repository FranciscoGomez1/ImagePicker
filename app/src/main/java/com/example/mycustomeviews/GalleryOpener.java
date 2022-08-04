package com.example.mycustomeviews;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;


import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class GalleryOpener extends AppCompatActivity{

    //need the uri
    public static Uri galleryUri;
    private ComponentActivity activity;
    private ActivityResultLauncher<String> mGetContent;
    private ImageHasbeenSelected imageHasbeenSelected;

    public GalleryOpener(ComponentActivity activity) {
        this.activity = activity;
        this.mGetContent = activity.registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                        Log.d("URI", uri.toString());
                        galleryUri = uri;
                        //Set Listener
                        imageHasbeenSelected.imageUpdate();

                    }
                });
    }
    public void launchGallery(){
        Log.d("URI", "ButtonPress");
       mGetContent.launch("image/*");
    }

    public void galleryOpenerListener( ImageHasbeenSelected imageHasbeenSelected){
        this.imageHasbeenSelected = imageHasbeenSelected;
    }

    public interface ImageHasbeenSelected{
        public void imageUpdate();
    }

}
