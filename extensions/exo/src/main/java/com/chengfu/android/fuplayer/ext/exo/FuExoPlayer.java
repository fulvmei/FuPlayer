package com.chengfu.android.fuplayer.ext.exo;

import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chengfu.android.fuplayer.FuPlayer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.PlayerMessage;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

public class FuExoPlayer implements FuPlayer {

    private ExoPlayer mPlayer;

    FuExoPlayer(@NonNull ExoPlayer player) {
        mPlayer = player;
    }

    @Override
    public Looper getPlaybackLooper() {
        return mPlayer.getPlaybackLooper();
    }

    @Override
    public void retry() {
        mPlayer.retry();
    }

    @Override
    public void prepare(MediaSource mediaSource) {
        mPlayer.prepare(mediaSource);
    }

    @Override
    public void prepare(MediaSource mediaSource, boolean resetPosition, boolean resetState) {
        mPlayer.prepare(mediaSource, resetPosition, resetState);
    }

    @Override
    public PlayerMessage createMessage(PlayerMessage.Target target) {
        return mPlayer.createMessage(target);
    }

    @Override
    public void setSeekParameters(@Nullable SeekParameters seekParameters) {
        mPlayer.setSeekParameters(seekParameters);
    }

    @Override
    public SeekParameters getSeekParameters() {
        return mPlayer.getSeekParameters();
    }

    @Nullable
    @Override
    public AudioComponent getAudioComponent() {
        return mPlayer.getAudioComponent();
    }

    @Nullable
    @Override
    public VideoComponent getVideoComponent() {
        return mPlayer.getVideoComponent();
    }

    @Nullable
    @Override
    public TextComponent getTextComponent() {
        return mPlayer.getTextComponent();
    }

    @Nullable
    @Override
    public MetadataComponent getMetadataComponent() {
        return mPlayer.getMetadataComponent();
    }

    @Override
    public Looper getApplicationLooper() {
        return mPlayer.getApplicationLooper();
    }

    @Override
    public void addListener(Player.EventListener listener) {
        mPlayer.addListener(listener);
    }

    @Override
    public void removeListener(Player.EventListener listener) {
        mPlayer.removeListener(listener);
    }

    @Override
    public int getPlaybackState() {
        return mPlayer.getPlaybackState();
    }

    @Override
    public int getPlaybackSuppressionReason() {
        return mPlayer.getPlaybackSuppressionReason();
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Nullable
    @Override
    public ExoPlaybackException getPlaybackError() {
        return mPlayer.getPlaybackError();
    }

    @Override
    public void setPlayWhenReady(boolean playWhenReady) {
        mPlayer.setPlayWhenReady(playWhenReady);
    }

    @Override
    public boolean getPlayWhenReady() {
        return mPlayer.getPlayWhenReady();
    }

    @Override
    public void setRepeatMode(int repeatMode) {
        mPlayer.setRepeatMode(repeatMode);
    }

    @Override
    public int getRepeatMode() {
        return mPlayer.getRepeatMode();
    }

    @Override
    public void setShuffleModeEnabled(boolean shuffleModeEnabled) {
        mPlayer.setShuffleModeEnabled(shuffleModeEnabled);
    }

    @Override
    public boolean getShuffleModeEnabled() {
        return mPlayer.getShuffleModeEnabled();
    }

    @Override
    public boolean isLoading() {
        return mPlayer.isLoading();
    }

    @Override
    public void seekToDefaultPosition() {
        mPlayer.seekToDefaultPosition();
    }

    @Override
    public void seekToDefaultPosition(int windowIndex) {
        mPlayer.seekToDefaultPosition(windowIndex);
    }

    @Override
    public void seekTo(long positionMs) {
        mPlayer.seekTo(positionMs);
    }

    @Override
    public void seekTo(int windowIndex, long positionMs) {
        mPlayer.seekTo(windowIndex, positionMs);
    }

    @Override
    public boolean hasPrevious() {
        return mPlayer.hasPrevious();
    }

    @Override
    public void previous() {
        mPlayer.previous();
    }

    @Override
    public boolean hasNext() {
        return mPlayer.hasNext();
    }

    @Override
    public void next() {
        mPlayer.next();
    }

    @Override
    public void setPlaybackParameters(@Nullable PlaybackParameters playbackParameters) {
        mPlayer.setPlaybackParameters(playbackParameters);
    }

    @Override
    public PlaybackParameters getPlaybackParameters() {
        return mPlayer.getPlaybackParameters();
    }

    @Override
    public void stop() {
        mPlayer.stop();
    }

    @Override
    public void stop(boolean reset) {
        mPlayer.stop(reset);
    }

    @Override
    public void release() {
        mPlayer.release();
    }

    @Override
    public int getRendererCount() {
        return mPlayer.getRendererCount();
    }

    @Override
    public int getRendererType(int index) {
        return mPlayer.getRendererType(index);
    }

    @Override
    public TrackGroupArray getCurrentTrackGroups() {
        return mPlayer.getCurrentTrackGroups();
    }

    @Override
    public TrackSelectionArray getCurrentTrackSelections() {
        return mPlayer.getCurrentTrackSelections();
    }

    @Nullable
    @Override
    public Object getCurrentManifest() {
        return mPlayer.getCurrentManifest();
    }

    @Override
    public Timeline getCurrentTimeline() {
        return mPlayer.getCurrentTimeline();
    }

    @Override
    public int getCurrentPeriodIndex() {
        return mPlayer.getCurrentPeriodIndex();
    }

    @Override
    public int getCurrentWindowIndex() {
        return mPlayer.getCurrentWindowIndex();
    }

    @Override
    public int getNextWindowIndex() {
        return mPlayer.getNextWindowIndex();
    }

    @Override
    public int getPreviousWindowIndex() {
        return mPlayer.getPreviousWindowIndex();
    }

    @Nullable
    @Override
    public Object getCurrentTag() {
        return mPlayer.getCurrentTag();
    }

    @Override
    public long getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public long getBufferedPosition() {
        return mPlayer.getBufferedPosition();
    }

    @Override
    public int getBufferedPercentage() {
        return mPlayer.getBufferedPercentage();
    }

    @Override
    public long getTotalBufferedDuration() {
        return mPlayer.getTotalBufferedDuration();
    }

    @Override
    public boolean isCurrentWindowDynamic() {
        return mPlayer.isCurrentWindowDynamic();
    }

    @Override
    public boolean isCurrentWindowSeekable() {
        return mPlayer.isCurrentWindowSeekable();
    }

    @Override
    public boolean isPlayingAd() {
        return mPlayer.isPlayingAd();
    }

    @Override
    public int getCurrentAdGroupIndex() {
        return mPlayer.getCurrentAdGroupIndex();
    }

    @Override
    public int getCurrentAdIndexInAdGroup() {
        return mPlayer.getCurrentAdIndexInAdGroup();
    }

    @Override
    public long getContentDuration() {
        return mPlayer.getContentDuration();
    }

    @Override
    public long getContentPosition() {
        return mPlayer.getContentPosition();
    }

    @Override
    public long getContentBufferedPosition() {
        return mPlayer.getContentBufferedPosition();
    }
}
