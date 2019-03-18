package com.chengfu.android.fuplayer.demo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chengfu.android.fuplayer.demo.ui.local.LocalVideosActivity;
import com.chengfu.android.fuplayer.demo.ui.video.VideoListActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        findViewById(R.id.screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });
    }
}
