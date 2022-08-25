package com.example.mycustomeviews;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
   // private ImageView imageView;

    private String[] imagesUrls =  new String[]{
            "https://pbs.twimg.com/profile_images/1510276954938920966/_jPqrcHD_400x400.jpg",
            "https://c.tenor.com/BSamNpi39MIAAAAM/minions.gif"
    };
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //The following block got commented out because now the fallowing logic can be done in a fragment
        /*photoUri = Uri.parse(imagesUrls[1]);
        imageView = ((ImageSelector) findViewById(R.id.image_selector)).getSelectorImage();
        Glide.with(this).load(photoUri).into(imageView);
        */
    }
}