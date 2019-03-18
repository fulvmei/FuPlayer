package com.chengfu.android.fuplayer.demo;

import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.chengfu.android.fuplayer.FuPlayerView;
import com.chengfu.android.fuplayer.SampleBufferingView;
import com.chengfu.android.fuplayer.SampleEndedView;
import com.chengfu.android.fuplayer.demo.bean.Video;
import com.chengfu.android.fuplayer.demo.immersion.ImmersionBar;
import com.chengfu.android.fuplayer.demo.immersion.QMUIStatusBarHelper;
import com.chengfu.android.fuplayer.demo.util.MediaSourceUtil;
//import com.chengfu.android.fuplayer.demo.util.ScreenRotationHelper;
import com.chengfu.android.fuplayer.ext.ui.VideoControlView;
import com.chengfu.android.fuplayer.ext.ui.VideoPlayErrorView;
//import com.chengfu.player.extensions.pldroid.PLPlayer;
import com.chengfu.android.fuplayer.ext.ui.gesture.GestureHelper;
import com.chengfu.android.fuplayer.ext.ui.screen.ScreenRotationHelper;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;


public class VideoPlayerActivity extends AppCompatActivity {
    private Video media;
    private View playerRoot;
    private FuPlayerView playerView;
    private VideoControlView controlView;
    private Player player;
    private ScreenRotationHelper screenRotationHelper;


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

        screenRotationHelper = new ScreenRotationHelper(this);
        screenRotationHelper.setPlayer(player);
        screenRotationHelper.setDisableInPlayerStateEnd(true);
        screenRotationHelper.setDisableInPlayerStateError(false);
        screenRotationHelper.setToggleToPortraitInDisable(true);
        screenRotationHelper.setEnablePortraitFullScreen(true);
        screenRotationHelper.setAutoRotationMode(ScreenRotationHelper.AUTO_ROTATION_MODE_SYSTEM);

        screenRotationHelper.setOnScreenChangedListener(portraitFullScreen -> {
            if (portraitFullScreen) {

                controlView.setFullScreen(true);

                ViewGroup.LayoutParams layoutParams = playerRoot.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//            playerRoot.setLayoutParams(layoutParams);

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                controlView.setFullScreen(false);

                ViewGroup.LayoutParams layoutParams = playerRoot.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = 608;

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        });
    }

    private void initPlayer() {
        ExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(this);
        exoPlayer.prepare(MediaSourceUtil.getMediaSource(this, media.getPath()));
        player = exoPlayer;
        player.setPlayWhenReady(true);
    }

    private void initPlayerView() {
        playerView = findViewById(R.id.playerView);

        SampleBufferingView loadingView = new SampleBufferingView(this);
        VideoPlayErrorView errorView = new VideoPlayErrorView(this);
        SampleEndedView endedView = new SampleEndedView(this);

        errorView.setOnReTryClickListener(v -> player.setPlayWhenReady(true));

        endedView.setOnReTryClickListener(v -> {
            if (player.getPlaybackState() == Player.STATE_ENDED) {
                player.seekTo(0);
            }
            player.setPlayWhenReady(true);
        });

        playerView.addStateView(loadingView);
        playerView.addStateView(errorView);
        playerView.addStateView(endedView);

        playerView.setPlayer(player);

    }

    private void initControlView() {
        controlView = findViewById(R.id.controlView);

        controlView.setPlayer(player);

        controlView.setTitle(media.getName());

        controlView.setEnableGestureType(GestureHelper.SHOW_TYPE_BRIGHTNESS | GestureHelper.SHOW_TYPE_PROGRESS | GestureHelper.SHOW_TYPE_VOLUME);
        controlView.setShowBottomProgress(true);
        controlView.setShowTopOnlyFullScreen(true);

        controlView.setOnScreenClickListener(fullScreen -> {
            screenRotationHelper.manualToggleOrientation();
        });

        controlView.setOnBackClickListener(v -> {
            if (!screenRotationHelper.maybeToggleToPortrait()) {
                finish();
            }
        });

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            controlView.setFullScreen(true);

            ViewGroup.LayoutParams layoutParams = playerRoot.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//            playerRoot.setLayoutParams(layoutParams);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            controlView.setFullScreen(false);

            ViewGroup.LayoutParams layoutParams = playerRoot.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = 608;

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onBackPressed() {
        if (!screenRotationHelper.maybeToggleToPortrait()) {
            finish();
        }
    }


    @Override
    protected void onResume() {
        playerView.onResume();
        screenRotationHelper.resume();
        if (player != null) {
            player.setPlayWhenReady(true);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        playerView.onPause();
        screenRotationHelper.pause();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        player.release();
        screenRotationHelper.pause();
        super.onDestroy();
    }
}
