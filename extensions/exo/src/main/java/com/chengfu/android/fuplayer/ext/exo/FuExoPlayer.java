package com.chengfu.android.fuplayer.ext.exo;

import android.os.Looper;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chengfu.android.fuplayer.FuPlayer;
import com.google.android.exoplayer2.DeviceInfo;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.PlayerMessage;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Tracks;
import com.google.android.exoplayer2.analytics.AnalyticsCollector;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.audio.AuxEffectInfo;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ShuffleOrder;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.CueGroup;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import com.google.android.exoplayer2.video.VideoFrameMetadataListener;
import com.google.android.exoplayer2.video.VideoSize;
import com.google.android.exoplayer2.video.spherical.CameraMotionListener;

import java.util.List;

public class FuExoPlayer implements FuPlayer {

    private ExoPlayer mPlayer;

    public FuExoPlayer(@NonNull ExoPlayer player) {
        mPlayer = player;
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
    public DeviceComponent getDeviceComponent() {
        return mPlayer.getDeviceComponent();
    }

    @Override
    public void addAudioOffloadListener(AudioOffloadListener listener) {
        mPlayer.addAudioOffloadListener(listener);
    }

    @Override
    public void removeAudioOffloadListener(AudioOffloadListener listener) {
        mPlayer.removeAudioOffloadListener(listener);
    }

    @Override
    public AnalyticsCollector getAnalyticsCollector() {
        return mPlayer.getAnalyticsCollector();
    }

    @Override
    public void addAnalyticsListener(AnalyticsListener listener) {
        mPlayer.addAnalyticsListener(listener);
    }

    @Override
    public void removeAnalyticsListener(AnalyticsListener listener) {
        mPlayer.removeAnalyticsListener(listener);
    }

    @Override
    public Looper getApplicationLooper() {
        return mPlayer.getApplicationLooper();
    }

    @Override
    public void addListener(Listener listener) {
        mPlayer.addListener(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        mPlayer.removeListener(listener);
    }

    @Override
    public void setMediaItems(List<MediaItem> mediaItems) {
        mPlayer.setMediaItems(mediaItems);
    }

    @Override
    public void setMediaItems(List<MediaItem> mediaItems, boolean resetPosition) {
        mPlayer.setMediaItems(mediaItems, resetPosition);
    }

    @Override
    public void setMediaItems(List<MediaItem> mediaItems, int startWindowIndex, long startPositionMs) {
        mPlayer.setMediaItems(mediaItems, startWindowIndex, startPositionMs);
    }

    @Override
    public void setMediaItem(MediaItem mediaItem) {
        mPlayer.setMediaItem(mediaItem);
    }

    @Override
    public void setMediaItem(MediaItem mediaItem, long startPositionMs) {
        mPlayer.setMediaItem(mediaItem, startPositionMs);
    }

    @Override
    public void setMediaItem(MediaItem mediaItem, boolean resetPosition) {
        mPlayer.setMediaItem(mediaItem, resetPosition);
    }

    @Override
    public void addMediaItem(MediaItem mediaItem) {
        mPlayer.addMediaItem(mediaItem);
    }

    @Override
    public void addMediaItem(int index, MediaItem mediaItem) {
        mPlayer.addMediaItem(index, mediaItem);
    }

    @Override
    public void addMediaItems(List<MediaItem> mediaItems) {
        mPlayer.addMediaItems(mediaItems);
    }

    @Override
    public void addMediaItems(int index, List<MediaItem> mediaItems) {
        mPlayer.addMediaItems(index, mediaItems);
    }

    @Override
    public void moveMediaItem(int currentIndex, int newIndex) {
        mPlayer.moveMediaItem(currentIndex, newIndex);
    }

    @Override
    public void moveMediaItems(int fromIndex, int toIndex, int newIndex) {
        mPlayer.moveMediaItems(fromIndex, toIndex, newIndex);
    }

    @Override
    public void removeMediaItem(int index) {
        mPlayer.removeMediaItem(index);
    }

    @Override
    public void removeMediaItems(int fromIndex, int toIndex) {
        mPlayer.removeMediaItems(fromIndex, toIndex);
    }

    @Override
    public void clearMediaItems() {
        mPlayer.clearMediaItems();
    }

    @Override
    public boolean isCommandAvailable(int command) {
        return mPlayer.isCommandAvailable(command);
    }

    @Override
    public boolean canAdvertiseSession() {
        return mPlayer.canAdvertiseSession();
    }

    @Override
    public Commands getAvailableCommands() {
        return mPlayer.getAvailableCommands();
    }

    @Override
    public void prepare() {
        mPlayer.prepare();
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
    public ExoPlaybackException getPlayerError() {
        return mPlayer.getPlayerError();
    }

    @Override
    public void play() {
        mPlayer.play();
    }

    @Override
    public void pause() {
        mPlayer.pause();
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
    public long getSeekBackIncrement() {
        return mPlayer.getSeekBackIncrement();
    }

    @Override
    public void seekBack() {
        mPlayer.seekBack();
    }

    @Override
    public long getSeekForwardIncrement() {
        return mPlayer.getSeekForwardIncrement();
    }

    @Override
    public void seekForward() {
        mPlayer.seekForward();
    }

    @Override
    public boolean hasPrevious() {
        return mPlayer.hasPrevious();
    }

    @Override
    public boolean hasPreviousWindow() {
        return mPlayer.hasPreviousWindow();
    }

    @Override
    public boolean hasPreviousMediaItem() {
        return mPlayer.hasPreviousMediaItem();
    }

    @Override
    public void previous() {
        mPlayer.previous();
    }

    @Override
    public void seekToPreviousWindow() {
        mPlayer.seekToPreviousWindow();
    }

    @Override
    public void seekToPreviousMediaItem() {
        mPlayer.seekToPreviousMediaItem();
    }

    @Override
    public long getMaxSeekToPreviousPosition() {
        return mPlayer.getMaxSeekToPreviousPosition();
    }

    @Override
    public void seekToPrevious() {
        mPlayer.seekToPrevious();
    }

    @Override
    public boolean hasNext() {
        return mPlayer.hasNext();
    }

    @Override
    public boolean hasNextWindow() {
        return mPlayer.hasNextWindow();
    }

    @Override
    public boolean hasNextMediaItem() {
        return mPlayer.hasNextMediaItem();
    }

    @Override
    public void next() {
        mPlayer.next();
    }

    @Override
    public void seekToNextWindow() {
        mPlayer.seekToNextWindow();
    }

    @Override
    public void seekToNextMediaItem() {
        mPlayer.seekToNextMediaItem();
    }

    @Override
    public void seekToNext() {
        mPlayer.seekToNext();
    }

    @Override
    public void setPlaybackParameters(@Nullable PlaybackParameters playbackParameters) {
        mPlayer.setPlaybackParameters(playbackParameters);
    }

    @Override
    public void setPlaybackSpeed(float speed) {
        mPlayer.setPlaybackSpeed(speed);
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
    public Tracks getCurrentTracks() {
        return mPlayer.getCurrentTracks();
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
    public Renderer getRenderer(int index) {
        return mPlayer.getRenderer(index);
    }

    @Override
    public TrackGroupArray getCurrentTrackGroups() {
        return mPlayer.getCurrentTrackGroups();
    }

    @Override
    public TrackSelectionArray getCurrentTrackSelections() {
        return mPlayer.getCurrentTrackSelections();
    }

    @Override
    public TrackSelectionParameters getTrackSelectionParameters() {
        return mPlayer.getTrackSelectionParameters();
    }

    @Override
    public void setTrackSelectionParameters(TrackSelectionParameters parameters) {
        mPlayer.setTrackSelectionParameters(parameters);
    }

    @Override
    public MediaMetadata getMediaMetadata() {
        return mPlayer.getMediaMetadata();
    }

    @Override
    public MediaMetadata getPlaylistMetadata() {
        return mPlayer.getPlaylistMetadata();
    }

    @Override
    public void setPlaylistMetadata(MediaMetadata mediaMetadata) {
        mPlayer.setPlaylistMetadata(mediaMetadata);
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
    public int getCurrentMediaItemIndex() {
        return mPlayer.getCurrentMediaItemIndex();
    }

    @Override
    public int getNextWindowIndex() {
        return mPlayer.getNextWindowIndex();
    }

    @Override
    public int getNextMediaItemIndex() {
        return mPlayer.getNextMediaItemIndex();
    }

    @Override
    public int getPreviousWindowIndex() {
        return mPlayer.getPreviousWindowIndex();
    }

    @Override
    public int getPreviousMediaItemIndex() {
        return mPlayer.getPreviousMediaItemIndex();
    }

    @Nullable
    @Override
    public MediaItem getCurrentMediaItem() {
        return mPlayer.getCurrentMediaItem();
    }

    @Override
    public int getMediaItemCount() {
        return mPlayer.getMediaItemCount();
    }

    @Override
    public MediaItem getMediaItemAt(int index) {
        return mPlayer.getMediaItemAt(index);
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
    public boolean isCurrentMediaItemDynamic() {
        return mPlayer.isCurrentMediaItemDynamic();
    }

    @Override
    public boolean isCurrentWindowLive() {
        return mPlayer.isCurrentWindowLive();
    }

    @Override
    public boolean isCurrentMediaItemLive() {
        return mPlayer.isCurrentMediaItemLive();
    }

    @Override
    public long getCurrentLiveOffset() {
        return mPlayer.getCurrentLiveOffset();
    }

    @Override
    public boolean isCurrentWindowSeekable() {
        return mPlayer.isCurrentWindowSeekable();
    }

    @Override
    public boolean isCurrentMediaItemSeekable() {
        return mPlayer.isCurrentMediaItemSeekable();
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

    @Override
    public AudioAttributes getAudioAttributes() {
        return mPlayer.getAudioAttributes();
    }

    @Override
    public void setVolume(float audioVolume) {
        mPlayer.setVolume(audioVolume);
    }

    @Override
    public float getVolume() {
        return mPlayer.getVolume();
    }

    @Override
    public void clearVideoSurface() {
        mPlayer.clearVideoSurface();
    }

    @Override
    public void clearVideoSurface(@Nullable Surface surface) {
        mPlayer.clearVideoSurface(surface);
    }

    @Override
    public void setVideoSurface(@Nullable Surface surface) {
        mPlayer.setVideoSurface(surface);
    }

    @Override
    public void setVideoSurfaceHolder(@Nullable SurfaceHolder surfaceHolder) {
        mPlayer.setVideoSurfaceHolder(surfaceHolder);
    }

    @Override
    public void clearVideoSurfaceHolder(@Nullable SurfaceHolder surfaceHolder) {
        mPlayer.clearVideoSurfaceHolder(surfaceHolder);
    }

    @Override
    public void setVideoSurfaceView(@Nullable SurfaceView surfaceView) {
        mPlayer.setVideoSurfaceView(surfaceView);
    }

    @Override
    public void clearVideoSurfaceView(@Nullable SurfaceView surfaceView) {
        mPlayer.clearVideoSurfaceView(surfaceView);
    }

    @Override
    public void setVideoTextureView(@Nullable TextureView textureView) {
        mPlayer.setVideoTextureView(textureView);
    }

    @Override
    public void clearVideoTextureView(@Nullable TextureView textureView) {
        mPlayer.clearVideoTextureView(textureView);
    }

    @Override
    public VideoSize getVideoSize() {
        return mPlayer.getVideoSize();
    }

    @Override
    public CueGroup getCurrentCues() {
        return mPlayer.getCurrentCues();
    }

    @Override
    public DeviceInfo getDeviceInfo() {
        return mPlayer.getDeviceInfo();
    }

    @Override
    public int getDeviceVolume() {
        return mPlayer.getDeviceVolume();
    }

    @Override
    public boolean isDeviceMuted() {
        return mPlayer.isDeviceMuted();
    }

    @Override
    public void setDeviceVolume(int volume) {
        mPlayer.setDeviceVolume(volume);
    }

    @Override
    public void increaseDeviceVolume() {
        mPlayer.increaseDeviceVolume();
    }

    @Override
    public void decreaseDeviceVolume() {
        mPlayer.decreaseDeviceVolume();
    }

    @Override
    public void setDeviceMuted(boolean muted) {
        mPlayer.setDeviceMuted(muted);
    }

    @Nullable
    @Override
    public TrackSelector getTrackSelector() {
        return mPlayer.getTrackSelector();
    }

    @Override
    public Looper getPlaybackLooper() {
        return mPlayer.getPlaybackLooper();
    }

    @Override
    public Clock getClock() {
        return mPlayer.getClock();
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
    public void setMediaSources(List<MediaSource> mediaSources) {
        mPlayer.setMediaSources(mediaSources);
    }

    @Override
    public void setMediaSources(List<MediaSource> mediaSources, boolean resetPosition) {
        mPlayer.setMediaSources(mediaSources, resetPosition);
    }

    @Override
    public void setMediaSources(List<MediaSource> mediaSources, int startWindowIndex, long startPositionMs) {
        mPlayer.setMediaSources(mediaSources, startWindowIndex, startPositionMs);
    }

    @Override
    public void setMediaSource(MediaSource mediaSource) {
        mPlayer.setMediaSource(mediaSource);
    }

    @Override
    public void setMediaSource(MediaSource mediaSource, long startPositionMs) {
        mPlayer.setMediaSource(mediaSource, startPositionMs);
    }

    @Override
    public void setMediaSource(MediaSource mediaSource, boolean resetPosition) {
        mPlayer.setMediaSource(mediaSource, resetPosition);
    }

    @Override
    public void addMediaSource(MediaSource mediaSource) {
        mPlayer.addMediaSource(mediaSource);
    }

    @Override
    public void addMediaSource(int index, MediaSource mediaSource) {
        mPlayer.addMediaSource(index, mediaSource);
    }

    @Override
    public void addMediaSources(List<MediaSource> mediaSources) {
        mPlayer.addMediaSources(mediaSources);
    }

    @Override
    public void addMediaSources(int index, List<MediaSource> mediaSources) {
        mPlayer.addMediaSources(index, mediaSources);
    }

    @Override
    public void setShuffleOrder(ShuffleOrder shuffleOrder) {
        mPlayer.setShuffleOrder(shuffleOrder);
    }

    @Override
    public void setAudioAttributes(AudioAttributes audioAttributes, boolean handleAudioFocus) {
        mPlayer.setAudioAttributes(audioAttributes, handleAudioFocus);
    }

    @Override
    public void setAudioSessionId(int audioSessionId) {
        mPlayer.setAudioSessionId(audioSessionId);
    }

    @Override
    public int getAudioSessionId() {
        return mPlayer.getAudioSessionId();
    }

    @Override
    public void setAuxEffectInfo(AuxEffectInfo auxEffectInfo) {
        mPlayer.setAuxEffectInfo(auxEffectInfo);
    }

    @Override
    public void clearAuxEffectInfo() {
        mPlayer.clearAuxEffectInfo();
    }

    @Override
    public void setSkipSilenceEnabled(boolean skipSilenceEnabled) {
        mPlayer.setSkipSilenceEnabled(skipSilenceEnabled);
    }

    @Override
    public boolean getSkipSilenceEnabled() {
        return mPlayer.getSkipSilenceEnabled();
    }

    @Override
    public void setVideoScalingMode(int videoScalingMode) {
        mPlayer.setVideoScalingMode(videoScalingMode);
    }

    @Override
    public int getVideoScalingMode() {
        return mPlayer.getVideoScalingMode();
    }

    @Override
    public void setVideoChangeFrameRateStrategy(int videoChangeFrameRateStrategy) {
        mPlayer.setVideoChangeFrameRateStrategy(videoChangeFrameRateStrategy);
    }

    @Override
    public int getVideoChangeFrameRateStrategy() {
        return mPlayer.getVideoChangeFrameRateStrategy();
    }

    @Override
    public void setVideoFrameMetadataListener(VideoFrameMetadataListener listener) {
        mPlayer.setVideoFrameMetadataListener(listener);
    }

    @Override
    public void clearVideoFrameMetadataListener(VideoFrameMetadataListener listener) {
        mPlayer.clearVideoFrameMetadataListener(listener);
    }

    @Override
    public void setCameraMotionListener(CameraMotionListener listener) {
        mPlayer.setCameraMotionListener(listener);
    }

    @Override
    public void clearCameraMotionListener(CameraMotionListener listener) {
        mPlayer.clearCameraMotionListener(listener);
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

    @Override
    public void setForegroundMode(boolean foregroundMode) {
        mPlayer.setForegroundMode(foregroundMode);
    }

    @Override
    public void setPauseAtEndOfMediaItems(boolean pauseAtEndOfMediaItems) {
        mPlayer.setPauseAtEndOfMediaItems(pauseAtEndOfMediaItems);
    }

    @Override
    public boolean getPauseAtEndOfMediaItems() {
        return mPlayer.getPauseAtEndOfMediaItems();
    }

    @Nullable
    @Override
    public Format getAudioFormat() {
        return mPlayer.getAudioFormat();
    }

    @Nullable
    @Override
    public Format getVideoFormat() {
        return mPlayer.getVideoFormat();
    }

    @Nullable
    @Override
    public DecoderCounters getAudioDecoderCounters() {
        return mPlayer.getAudioDecoderCounters();
    }

    @Nullable
    @Override
    public DecoderCounters getVideoDecoderCounters() {
        return mPlayer.getVideoDecoderCounters();
    }

    @Override
    public void setHandleAudioBecomingNoisy(boolean handleAudioBecomingNoisy) {
        mPlayer.setHandleAudioBecomingNoisy(handleAudioBecomingNoisy);
    }

    @Override
    public void setHandleWakeLock(boolean handleWakeLock) {
        mPlayer.setHandleWakeLock(handleWakeLock);
    }

    @Override
    public void setWakeMode(int wakeMode) {
        mPlayer.setWakeMode(wakeMode);
    }

    @Override
    public void setPriorityTaskManager(@Nullable PriorityTaskManager priorityTaskManager) {
        mPlayer.setPriorityTaskManager(priorityTaskManager);
    }

    @Override
    public void experimentalSetOffloadSchedulingEnabled(boolean offloadSchedulingEnabled) {
        mPlayer.experimentalSetOffloadSchedulingEnabled(offloadSchedulingEnabled);
    }

    @Override
    public boolean experimentalIsSleepingForOffload() {
        return mPlayer.experimentalIsSleepingForOffload();
    }
}
