package com.chengfu.android.fuplayer.demo;

import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chengfu.android.fuplayer.DefaultControlView;
import com.chengfu.android.fuplayer.FuPlayerView;
import com.chengfu.android.fuplayer.SampleBufferingView;
import com.chengfu.android.fuplayer.SampleEndedView;
import com.chengfu.android.fuplayer.SampleErrorView;
import com.chengfu.android.fuplayer.demo.immersion.ImmersionBar;
import com.chengfu.android.fuplayer.demo.immersion.QMUIStatusBarHelper;
import com.chengfu.android.fuplayer.demo.util.MediaSourceUtil;
import com.chengfu.player.extensions.pldroid.PLPlayer;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
//import com.chengfu.player.extensions.pldroid.PLPlayer;


public class VideoPlayerActivity extends AppCompatActivity {

    private static final String path1 = "https://mov.bn.netease.com/open-movie/nos/mp4/2015/08/27/SB13F5AGJ_sd.mp4";
    private static final String path2 = " https://mov.bn.netease.com/open-movie/nos/mp4/2018/01/12/SD70VQJ74_sd.mp4";
    private static final String path3 = "http://221.228.226.23/11/t/j/v/b/tjvbwspwhqdmgouolposcsfafpedmb/sh.yinyuetai.com/691201536EE4912BF7E4F1E2C67B8119.mp4";
    private static final String path4 = "http://storage.gzstv.net/uploads/media/WZZ/xjm-zhaopian.mp4";

    private FuPlayerView playerView;
    private DefaultControlView controlView;
    private Player player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImmersionBar.with(this).statusBarColorInt(Color.BLACK).init();
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setContentView(R.layout.activity_video_player);

        playerView = findViewById(R.id.playerView);

        controlView = findViewById(R.id.controlView);

        player =exoPlayer();
//        player.prepare(MediaSourceUtil.getMediaSource(this, path1));

        playerView.setPlayer(player);

        player.setPlayWhenReady(true);
        playerView.onResume();


        controlView.setPlayer(player);

        SampleBufferingView loadingView = new SampleBufferingView(this);
        SampleErrorView errorView = new SampleErrorView(this);
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


        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerView.setPlayer(player);
            }
        });

//        MediaController

    }

    private Player exoPlayer() {
        ExoPlayer exoPlayer =ExoPlayerFactory.newSimpleInstance(this);
        exoPlayer.prepare(MediaSourceUtil.getMediaSource(this, path1));
        return exoPlayer;
    }

    private Player plPlayer() {
        PLPlayer player = new PLPlayer(this);

        player.setDataSource(path1);
        return player;
    }

    @Override
    protected void onResume() {
        playerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        playerView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        player.release();
        super.onDestroy();
    }
}
