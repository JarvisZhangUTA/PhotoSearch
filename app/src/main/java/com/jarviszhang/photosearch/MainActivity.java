package com.jarviszhang.photosearch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public final static int KEEP_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                        startActivity(intent);
                    }
                },
                KEEP_TIME
        );
    }

}
