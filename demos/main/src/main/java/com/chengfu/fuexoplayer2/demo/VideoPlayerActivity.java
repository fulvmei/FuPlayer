package com.chengfu.fuexoplayer2.demo;

import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chengfu.fuexoplayer2.demo.immersion.ImmersionBar;
import com.chengfu.fuexoplayer2.demo.immersion.QMUIStatusBarHelper;
import com.chengfu.fuexoplayer2.widget.DefaultControlView;
import com.chengfu.fuexoplayer2.widget.SampleBufferingView;
import com.chengfu.fuexoplayer2.widget.FuPlayerView;
import com.chengfu.fuexoplayer2.widget.SampleEndedView;
import com.chengfu.fuexoplayer2.widget.SampleErrorView;
import com.chengfu.player.extensions.pldroid.PLPlayer;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

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

        setContentView(R.layout.activity_player);

        playerView = findViewById(R.id.playerView);

        controlView = findViewById(R.id.controlView);

//        player = exoPlayer();
        player = plPlayer();

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
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this);
        ConcatenatingMediaSource concatenatedSource =
                new ConcatenatingMediaSource(getMediaSource(Uri.parse(path1)), getMediaSource(Uri.parse(path2)), getMediaSource(Uri.parse(path3)), getMediaSource(Uri.parse(path4)));

        player.prepare(getMediaSource(Uri.parse(path1)));
        return player;
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

    private MediaSource getMediaSource(Uri uri) {
        int contentType = Util.inferContentType(uri);
        DefaultDataSourceFactory dataSourceFactory =
                new DefaultDataSourceFactory(this,
                        Util.getUserAgent(this, this.getPackageName()), new DefaultBandwidthMeter());


//        String scheme = uri.getScheme();
//        if (scheme != null && scheme.contains("rtmp")) {
//            return new ExtractorMediaSource(uri, new RtmpDataSourceFactory(), new DefaultExtractorsFactory(), null, null);
//        }

        switch (contentType) {
            case C.TYPE_DASH:
                DefaultDashChunkSource.Factory factory = new DefaultDashChunkSource.Factory(dataSourceFactory);
                return new DashMediaSource(uri, dataSourceFactory, factory, null, null);
            case C.TYPE_SS:
                DefaultSsChunkSource.Factory ssFactory = new DefaultSsChunkSource.Factory(dataSourceFactory);
                return new SsMediaSource(uri, dataSourceFactory, ssFactory, null, null);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, dataSourceFactory, null, null);

            case C.TYPE_OTHER:
            default:
                // This is the MediaSource representing the media to be played.
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                return new ExtractorMediaSource(uri,
                        dataSourceFactory, extractorsFactory, null, null);
        }
    }
}
