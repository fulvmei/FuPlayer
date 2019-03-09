package com.chengfu.fuexoplayer2.audio;

import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.chengfu.fuexoplayer2.audio.library.MusicSource;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.upstream.DataSource;

public class UampPlaybackPreparer implements MediaSessionConnector.PlaybackPreparer {

    private MusicSource musicSource;
    private ExoPlayer exoPlayer;
    private DataSource.Factory dataSourceFactory;

    public UampPlaybackPreparer(ExoPlayer exoPlayer, DataSource.Factory dataSourceFactory) {
        this.musicSource = musicSource;
        this.exoPlayer = exoPlayer;
        this.dataSourceFactory = dataSourceFactory;
        Log.d("UampPlaybackPreparer", "UampPlaybackPreparer");
    }

    @Override
    public long getSupportedPrepareActions() {
        Log.d("UampPlaybackPreparer", "getSupportedPrepareActions");
        return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID |
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID |
                PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH |
                PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH;
    }

    @Override
    public void onPrepare() {
        Log.d("UampPlaybackPreparer", "onPrepare");
    }

    @Override
    public void onPrepareFromMediaId(String mediaId, Bundle extras) {
        Log.d("UampPlaybackPreparer", "onPrepareFromMediaId");

//        exoPlayer.prepare();
    }

    @Override
    public void onPrepareFromSearch(String query, Bundle extras) {
        Log.d("UampPlaybackPreparer", "onPrepareFromSearch");
    }

    @Override
    public void onPrepareFromUri(Uri uri, Bundle extras) {
        Log.d("UampPlaybackPreparer", "onPrepareFromUri");
    }

    @Override
    public String[] getCommands() {
        Log.d("UampPlaybackPreparer", "getCommands");
        return new String[0];
    }

    @Override
    public void onCommand(Player player, String command, Bundle extras, ResultReceiver cb) {
        Log.d("UampPlaybackPreparer", "onCommand command="+command);
    }
}
