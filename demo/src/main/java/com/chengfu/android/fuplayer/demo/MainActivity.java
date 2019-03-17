package com.chengfu.android.fuplayer.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chengfu.android.fuplayer.demo.ui.local.LocalVideosActivity;
import com.chengfu.android.fuplayer.demo.ui.video.VideoListActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnLocalVideos).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LocalVideosActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnNetVideos).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MediaChooseActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnVideoListPlay).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VideoListActivity.class);
            startActivity(intent);
        });

    }
}