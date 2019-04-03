package com.chengfu.android.fuplayer.demo.video.ui.video;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.chengfu.android.fuplayer.demo.video.R;
import com.chengfu.android.fuplayer.video.FuPlayerView;
import com.chengfu.android.fuplayer.demo.video.StaticConfig;
import com.chengfu.android.fuplayer.demo.video.bean.Resource;
import com.chengfu.android.fuplayer.demo.video.bean.Video;

import com.chengfu.android.fuplayer.demo.video.immersion.BarHide;
import com.chengfu.android.fuplayer.demo.video.immersion.ImmersionBar;
import com.chengfu.android.fuplayer.demo.video.player.FuPlayer;
import com.chengfu.android.fuplayer.demo.video.util.MediaSourceUtil;
import com.chengfu.android.fuplayer.ext.exo.FuExoPlayerFactory;
import com.chengfu.android.fuplayer.ext.video_plus.VideoBufferingView;
import com.chengfu.android.fuplayer.ext.video_plus.VideoControlView;
import com.chengfu.android.fuplayer.ext.video_plus.VideoEndedView;
import com.chengfu.android.fuplayer.ext.video_plus.VideoPlayErrorView;
import com.chengfu.android.fuplayer.ext.video_plus.VideoPlayWithoutWifiView;
import com.chengfu.android.fuplayer.ext.video_plus.screen.ScreenRotationHelper;


public class VideoPlayerActivity extends AppCompatActivity {
    public static final String TAG = "VideoPlayerActivity";
    private Video video;
    private String id;
    private View playerRoot;
    private FuPlayerView playerView;
    private VideoControlView controlView;
    private FuPlayer player;
    private VideoBufferingView loadingView;
    private VideoPlayErrorView errorView;
    private VideoEndedView endedView;
    private VideoPlayWithoutWifiView noWifiView;

    private VideoDetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        id = getIntent().getStringExtra("id");

        setContentView(R.layout.activity_video_player);

        ImmersionBar.with(this)
                .statusBarColorInt(Color.BLACK)
                .fitsSystemWindows(true)
                .hideBar(BarHide.FLAG_SHOW_BAR)
                .init();

        player = new FuPlayer(this, new FuExoPlayerFactory(this));

        initViews();

        initScreenRotation();

        initFragment();

        viewModel = ViewModelProviders.of(this).get(VideoDetailsViewModel.class);

        viewModel.getVideoResource().observe(this, new Observer<Resource<Video>>() {
            @Override
            public void onChanged(@Nullable Resource<Video> videoResource) {
                video = videoResource.data;
                if (videoResource.status == Resource.Status.LOADING) {
                    loadingView.show();
                    errorView.hide();
                } else if (videoResource.status == Resource.Status.ERROR) {
                    loadingView.hide();
                    errorView.show();
                } else if (videoResource.status == Resource.Status.SUCCESS) {
                    loadingView.hide();
                    errorView.hide();

                    controlView.setTitle(video.getName());

                    player.prepare(MediaSourceUtil.getMediaSource(VideoPlayerActivity.this, video.getPath()));
//                    initializePlayer();
                }
            }
        });

        viewModel.setParams(id);
        viewModel.refreshVideo();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        id = getIntent().getStringExtra("id");

        viewModel.setParams(id);
        viewModel.refreshVideo();
    }

    private void initViews() {
        playerRoot = findViewById(R.id.playerRoot);

        playerView = findViewById(R.id.playerView);

        loadingView = findViewById(R.id.bufferingView);
        errorView = findViewById(R.id.errorView);
        endedView = findViewById(R.id.endedView);
        noWifiView = findViewById(R.id.noWifiView);

        noWifiView.setAllowPlayInNoWifi(StaticConfig.PLAY_VIDEO_NO_WIFI);

        errorView.setOnRetryListener(player -> {
            if (video == null) {
                viewModel.refreshVideo();
                return true;
            }
            return false;
        });


        controlView = findViewById(R.id.controlView);
//        controlView.setTitle(media.getName());

        controlView.setEnableGestureType(VideoControlView.Gesture.SHOW_TYPE_BRIGHTNESS | VideoControlView.Gesture.SHOW_TYPE_PROGRESS | VideoControlView.Gesture.SHOW_TYPE_VOLUME);
        controlView.setShowBottomProgress(true);
        controlView.setShowTopOnlyFullScreen(true);
        controlView.setShowAlwaysInPaused(true);

//        controlView.setOnScreenClickListener(fullScreen -> {
//            player.getScreenRotation().manualToggleOrientation();
//        });
//
//        controlView.setOnBackClickListener(v -> {
//            if (!player.getScreenRotation().maybeToggleToPortrait()) {
//                finish();
//            }
//        });

        player.addStateView(noWifiView, true);
        player.addStateView(loadingView);
        player.addStateView(errorView, true);
        player.addStateView(endedView, true);

        player.setPlayerView(playerView);

        player.setVideoControlView(controlView);
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new VideoDetailsFragment()).commitAllowingStateLoss();
    }

    private void initScreenRotation() {
        ScreenRotationHelper screenRotationHelper = new ScreenRotationHelper(this);
//        screenRotationHelper.setPlayer(player);
        screenRotationHelper.setDisableInPlayerStateEnd(false);
        screenRotationHelper.setDisableInPlayerStateError(false);
        screenRotationHelper.setToggleToPortraitInDisable(true);
        screenRotationHelper.setEnablePortraitFullScreen(true);
        screenRotationHelper.setAutoRotationMode(ScreenRotationHelper.AUTO_ROTATION_MODE_SYSTEM);

        screenRotationHelper.setOnScreenChangedListener(portraitFullScreen -> {
            changedScreen(portraitFullScreen);
        });

        player.setScreenRotation(screenRotationHelper);
    }

    private void changedScreen(boolean fullScreen) {
        player.setFullScreen(fullScreen);
        if (fullScreen) {
            ViewGroup.LayoutParams layoutParams = playerRoot.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ImmersionBar.with(this)
                    .fitsSystemWindows(false)
                    .hideBar(BarHide.FLAG_HIDE_BAR)
                    .init();
        } else {

            ViewGroup.LayoutParams layoutParams = playerRoot.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = 608;

            ImmersionBar.with(this)
                    .statusBarColorInt(Color.BLACK)
                    .fitsSystemWindows(true)
                    .hideBar(BarHide.FLAG_SHOW_BAR)
                    .init();
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            changedScreen(true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            changedScreen(false);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        player.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.onPause();
    }

    @Override
    public void onBackPressed() {
        if (!player.onBackPressed()) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        player.onDestroy();
        super.onDestroy();
    }
}
