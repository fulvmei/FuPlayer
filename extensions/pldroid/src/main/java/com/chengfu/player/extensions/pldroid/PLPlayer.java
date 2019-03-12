package com.chengfu.player.extensions.pldroid;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.PLOnVideoFrameListener;
import com.pili.pldroid.player.PLOnVideoSizeChangedListener;

import java.io.IOException;
import java.util.Map;

public class PLPlayer extends PLPlayerBase {

    public static final String TAG = "PLPlayer";

    private Context context;

    private PLMediaPlayer plMediaPlayer;

    public PLPlayer(Context context) {
        this(context, null);
        this.context = context;
    }

    public PLPlayer(Context context, AVOptions options) {
        Log.d(TAG, "create PLMediaPlayer");
        plMediaPlayer = new PLMediaPlayer(context, options);

        plMediaPlayer.setOnVideoFrameListener(new PLOnVideoFrameListener() {
            @Override
            public void onVideoFrameAvailable(byte[] bytes, int i, int i1, int i2, int i3, long l) {
                Log.d(TAG, "onVideoFrameAvailable");
            }
        });

        plMediaPlayer.setOnPreparedListener(new PLOnPreparedListener() {
            @Override
            public void onPrepared(int i) {
                Log.d(TAG, "setOnPreparedListener");
                submitRenderedFirstFrame();
            }
        });

        plMediaPlayer.setOnVideoSizeChangedListener(new PLOnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(int width, int height) {
                Log.d(TAG, "onVideoSizeChanged : width=" + width + ",height=" + height);
                submitVideoSizeChanged(width, height, 0, 0);
            }
        });

        plMediaPlayer.setOnInfoListener(new PLOnInfoListener() {
            @Override
            public void onInfo(int i, int i1) {
//                FuLog.d(TAG, "onInfo");
            }
        });
    }

    public void setDataSource(String path) {
        setDataSource(path, null);
    }

    public void setDataSource(String path, Map<String, String> headers) {
        try {
            Log.d(TAG, "setDataSource : path=" + path);
            plMediaPlayer.setDataSource(path, headers);
            Log.d(TAG, "prepareAsync : ");
            plMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        plMediaPlayer.start();
    }

    @Nullable
    @Override
    public AudioComponent getAudioComponent() {
        return null;
    }

    @Nullable
    @Override
    public TextComponent getTextComponent() {
        return null;
    }

    @Nullable
    @Override
    public MetadataComponent getMetadataComponent() {
        return null;
    }

    @Override
    public Looper getApplicationLooper() {
        return context.getMainLooper();
    }

    @Override
    public void addListener(EventListener listener) {

    }

    @Override
    public void removeListener(EventListener listener) {

    }

    @Override
    public int getPlaybackState() {
        return 0;
    }

    @Nullable
    @Override
    public ExoPlaybackException getPlaybackError() {
        return null;
    }

    @Override
    public void setPlayWhenReady(boolean playWhenReady) {
        if (playWhenReady) {
            plMediaPlayer.start();
        } else {
            plMediaPlayer.pause();
        }
    }

    @Override
    public boolean getPlayWhenReady() {
        return false;
    }

    @Override
    public void setRepeatMode(int repeatMode) {

    }

    @Override
    public int getRepeatMode() {
        return Player.REPEAT_MODE_OFF;
    }

    @Override
    public void setShuffleModeEnabled(boolean shuffleModeEnabled) {

    }

    @Override
    public boolean getShuffleModeEnabled() {
        return false;
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void seekToDefaultPosition() {

    }

    @Override
    public void seekToDefaultPosition(int windowIndex) {

    }

    @Override
    public void seekTo(long positionMs) {

    }

    @Override
    public void seekTo(int windowIndex, long positionMs) {

    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public void previous() {

    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public void next() {

    }

    @Override
    public void setPlaybackParameters(@Nullable PlaybackParameters playbackParameters) {

    }

    @Override
    public PlaybackParameters getPlaybackParameters() {
        return null;
    }

    @Override
    public void stop() {
        plMediaPlayer.stop();
    }

    @Override
    public void stop(boolean reset) {
        plMediaPlayer.stop();
    }

    @Override
    public void release() {
        plMediaPlayer.release();
    }

    @Override
    public int getRendererCount() {
        return 0;
    }

    @Override
    public int getRendererType(int index) {
        return 0;
    }

    @Override
    public TrackGroupArray getCurrentTrackGroups() {
        return null;
    }

    @Override
    public TrackSelectionArray getCurrentTrackSelections() {
        return null;
    }

    @Nullable
    @Override
    public Object getCurrentManifest() {
        return null;
    }

    @Override
    public Timeline getCurrentTimeline() {
        return null;
    }

    @Override
    public int getCurrentPeriodIndex() {
        return 0;
    }

    @Override
    public int getCurrentWindowIndex() {
        return 0;
    }

    @Override
    public int getNextWindowIndex() {
        return 0;
    }

    @Override
    public int getPreviousWindowIndex() {
        return 0;
    }

    @Nullable
    @Override
    public Object getCurrentTag() {
        return null;
    }

    @Override
    public long getDuration() {
        return plMediaPlayer.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return plMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getBufferedPosition() {
        return 0;
    }

    @Override
    public int getBufferedPercentage() {
        return 0;
    }

    @Override
    public long getTotalBufferedDuration() {
        return 0;
    }

    @Override
    public boolean isCurrentWindowDynamic() {
        return false;
    }

    @Override
    public boolean isCurrentWindowSeekable() {
        return false;
    }

    @Override
    public boolean isPlayingAd() {
        return false;
    }

    @Override
    public int getCurrentAdGroupIndex() {
        return 0;
    }

    @Override
    public int getCurrentAdIndexInAdGroup() {
        return 0;
    }

    @Override
    public long getContentDuration() {
        return 0;
    }

    @Override
    public long getContentPosition() {
        return 0;
    }

    @Override
    public long getContentBufferedPosition() {
        return 0;
    }

    @Override
    protected void setSurface(Surface surface) {
        FuLog.d(TAG, "setSurface : surface=" + surface);
        plMediaPlayer.setSurface(surface);
    }

    @Override
    public void setVideoScalingMode(int videoScalingMode) {

    }

    @Override
    public int getVideoScalingMode() {
        return C.VIDEO_SCALING_MODE_SCALE_TO_FIT;
    }

    @Override
    public void setVideoFrameMetadataListener(VideoFrameMetadataListener listener) {

    }

    @Override
    public void clearVideoFrameMetadataListener(VideoFrameMetadataListener listener) {

    }

    @Override
    public void setCameraMotionListener(CameraMotionListener listener) {

    }

    @Override
    public void clearCameraMotionListener(CameraMotionListener listener) {

    }
}
