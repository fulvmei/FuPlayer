package com.chengfu.android.fuplayer.demo;

import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.chengfu.android.fuplayer.FuPlayer;
import com.chengfu.android.fuplayer.demo.bean.Media;
import com.chengfu.android.fuplayer.demo.util.MediaSourceUtil;
import com.chengfu.android.fuplayer.demo.util.ScreenTools;
import com.chengfu.android.fuplayer.ui.DefaultControlView;
import com.chengfu.android.fuplayer.ui.FuPlayerView;
import com.chengfu.android.fuplayer.ext.exo.FuExoPlayerFactory;
import com.chengfu.android.fuplayer.ui.SampleBufferingView;
import com.chengfu.android.fuplayer.ui.SampleEndedView;
import com.chengfu.android.fuplayer.ui.SampleErrorView;
import com.google.android.exoplayer2.Player;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;


public class PlayerActivity extends AppCompatActivity {
    public static final String TAG = "VideoPlayerActivity";
    private Media media;

    private View playerRoot;
    private FuPlayer player;
    private SampleBufferingView loadingView;
    private SampleErrorView errorView;
    private SampleEndedView endedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        media = (Media) getIntent().getSerializableExtra("media");

        setContentView(R.layout.activity_player);

        ImmersionBar.with(this)
                .statusBarColorInt(Color.BLACK)
                .fitsSystemWindows(true)
                .hideBar(BarHide.FLAG_SHOW_BAR)
                .init();

        playerRoot = findViewById(R.id.playerRoot);

        FuPlayerView playerView = findViewById(R.id.playerView);
        loadingView = findViewById(R.id.bufferingView);
        errorView = findViewById(R.id.errorView);
        endedView = findViewById(R.id.endedView);
        DefaultControlView controlView = findViewById(R.id.controlView);

        controlView.setShowAlwaysInPaused(true);

        player = new FuExoPlayerFactory(this).create();
        player.prepare(MediaSourceUtil.getMediaSource(this, media.getPath()));
        player.setPlayWhenReady(true);

        loadingView.setPlayer(player);
        errorView.setPlayer(player);
        endedView.setPlayer(player);
        playerView.setPlayer(player);
        controlView.setPlayer(player);

        onOrientationChanged(getResources().getConfiguration().orientation);
    }

    private void onOrientationChanged(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ViewGroup.LayoutParams layoutParams = playerRoot.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

            ImmersionBar.with(this)
                    .fitsSystemWindows(false)
                    .hideBar(BarHide.FLAG_HIDE_BAR)
                    .init();
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ViewGroup.LayoutParams layoutParams = playerRoot.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = (int) (ScreenTools.getScreenWidth(this) * 9 * 1.0f / 16);

            ImmersionBar.with(this)
                    .statusBarColorInt(Color.BLACK)
                    .fitsSystemWindows(true)
                    .hideBar(BarHide.FLAG_SHOW_BAR)
                    .init();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onOrientationChanged(newConfig.orientation);
    }


    @Override
    protected void onResume() {
        super.onResume();
        player.retry();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(player.getPlaybackState()!= Player.STATE_IDLE){
            player.stop();
        }
    }

    @Override
    protected void onDestroy() {
        player.release();
        super.onDestroy();
    }
}
