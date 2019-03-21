package com.chengfu.android.fuplayer.demo.ui.video;

import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.chengfu.android.fuplayer.FuPlayerView;
import com.chengfu.android.fuplayer.demo.R;
import com.chengfu.android.fuplayer.demo.StaticConfig;
import com.chengfu.android.fuplayer.demo.bean.Video;
import com.chengfu.android.fuplayer.demo.immersion.ImmersionBar;
import com.chengfu.android.fuplayer.demo.immersion.QMUIStatusBarHelper;

import com.chengfu.android.fuplayer.demo.player.FuPlayer;
import com.chengfu.android.fuplayer.demo.util.MediaSourceUtil;
import com.chengfu.android.fuplayer.ext.ui.VideoBufferingView;
import com.chengfu.android.fuplayer.ext.ui.VideoControlView;
import com.chengfu.android.fuplayer.ext.ui.VideoEndedView;
import com.chengfu.android.fuplayer.ext.ui.VideoPlayErrorView;
import com.chengfu.android.fuplayer.ext.ui.VideoPlayWithoutWifiView;
import com.chengfu.android.fuplayer.ext.ui.screen.ScreenRotationHelper;
import com.google.android.exoplayer2.ExoPlayerFactory;


public class VideoPlayerActivity extends AppCompatActivity {
    public static final String TAG = "VideoPlayerActivity";
    private Video media;
    private View playerRoot;
    private FuPlayerView playerView;
    private VideoControlView controlView;
    private FuPlayer player;
    private VideoBufferingView loadingView;
    private VideoPlayErrorView errorView;
    private VideoEndedView endedView;
    private VideoPlayWithoutWifiView noWifiView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImmersionBar.with(this).statusBarColorInt(Color.BLACK).init();
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);


        setContentView(R.layout.activity_video_player);

        playerRoot = findViewById(R.id.playerRoot);

        media = (Video) getIntent().getSerializableExtra("media");

        initPlayer();

        initPlayerView();

        initControlView();

        initScreenRotation();

        initFragment();

    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new VideoDetailsFragment()).commitAllowingStateLoss();
    }

    private void initScreenRotation() {
        ScreenRotationHelper screenRotationHelper = new ScreenRotationHelper(this);
//        screenRotationHelper.setPlayer(player);
        screenRotationHelper.setDisableInPlayerStateEnd(true);
        screenRotationHelper.setDisableInPlayerStateError(false);
        screenRotationHelper.setToggleToPortraitInDisable(true);
        screenRotationHelper.setEnablePortraitFullScreen(true);
        screenRotationHelper.setAutoRotationMode(ScreenRotationHelper.AUTO_ROTATION_MODE_SYSTEM);

        screenRotationHelper.setOnScreenChangedListener(portraitFullScreen -> {
            changedScreen(portraitFullScreen);
        });

        player.setScreenRotation(screenRotationHelper);
    }

    private void initPlayer() {
        player = new FuPlayer(this, ExoPlayerFactory.newSimpleInstance(this));

    }

    private void initPlayerView() {
        playerView = findViewById(R.id.playerView);

        loadingView = findViewById(R.id.bufferingView);
        errorView = findViewById(R.id.errorView);
        endedView = findViewById(R.id.endedView);
        noWifiView = findViewById(R.id.noWifiView);

        noWifiView.show();
        noWifiView.setOnPlayClickListener(v -> {
            StaticConfig.PLAY_VIDEO_NO_WIFI = true;
            player.prepare(MediaSourceUtil.getMediaSource(this, media.getPath()));
            player.setPlayWhenReady(true);
            noWifiView.hide();
        });

//        errorView.setOnReTryClickListener(v -> player.setPlayWhenReady(true));

//        endedView.setOnReTryClickListener(v -> {
//            if (player.getPlaybackState() == Player.STATE_ENDED) {
//                player.seekTo(0);
//            }
//            player.setPlayWhenReady(true);
//        });

//        player.addStateView(loadingView);


//        loadingView.setPlayer(player);
//        errorView.setPlayer(player);
//        endedView.setPlayer(player);

//        errorView.setVisibilityChangeListener((stateView, visibility) -> maybeHideController());
//
//        endedView.setVisibilityChangeListener((stateView, visibility) -> maybeHideController());

        player.addStateView(loadingView);
        player.addStateView(errorView, true);
        player.addStateView(endedView, true);


        player.setPlayerView(playerView);
    }

    private void initControlView() {
        controlView = findViewById(R.id.controlView);
        controlView.setTitle(media.getName());

        controlView.setEnableGestureType(VideoControlView.Gesture.SHOW_TYPE_BRIGHTNESS | VideoControlView.Gesture.SHOW_TYPE_PROGRESS | VideoControlView.Gesture.SHOW_TYPE_VOLUME);
        controlView.setShowBottomProgress(true);
        controlView.setShowTopOnlyFullScreen(true);
        controlView.setShowAlwaysInPaused(true);

        controlView.setOnScreenClickListener(fullScreen -> {
            player.getScreenRotation().manualToggleOrientation();
        });

        controlView.setOnBackClickListener(v -> {
            if (!player.getScreenRotation().maybeToggleToPortrait()) {
                finish();
            }
        });

        player.setVideoControlView(controlView);
    }

    private void changedScreen(boolean fullScreen) {
        if (fullScreen) {
            controlView.setFullScreen(true);
            loadingView.setFullScreen(true);
            ViewGroup.LayoutParams layoutParams = playerRoot.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            controlView.setFullScreen(false);
            loadingView.setFullScreen(false);
            ViewGroup.LayoutParams layoutParams = playerRoot.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = 608;

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
//        player.onResume();
        super.onResume();

        player.retry();
    }

    @Override
    protected void onPause() {
//        player.onPause();
        super.onPause();

        player.stop();
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
