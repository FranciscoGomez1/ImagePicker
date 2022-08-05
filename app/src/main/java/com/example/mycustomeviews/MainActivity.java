package com.example.mycustomeviews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((ImageSelector) findViewById(R.id.image_selector)).setBackground(R.drawable.placeholder);
    }
}