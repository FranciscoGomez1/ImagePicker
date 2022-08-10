package com.example.mycustomeviews;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class MainFrag extends Fragment {
    private ImageView imageView;
    private ImageSelector imageSelector;

    private String[] imagesUrls =  new String[]{
            "https://pbs.twimg.com/profile_images/1510276954938920966/_jPqrcHD_400x400.jpg",
            "https://c.tenor.com/BSamNpi39MIAAAAM/minions.gif"
    };
    private Uri photoUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_layout, container, false);
        imageSelector = layout.findViewById(R.id.image_selector);
        photoUri = Uri.parse(imagesUrls[1]);
        imageView = imageSelector.getSelectorImage();
        Glide.with(this).load(photoUri).into(imageView);

        return layout;
    }
}
