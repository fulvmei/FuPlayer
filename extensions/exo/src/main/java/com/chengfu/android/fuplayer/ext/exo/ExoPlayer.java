package com.chengfu.android.fuplayer.ext.exo;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.chengfu.android.fuplayer.FuPlaybackException;
import com.chengfu.android.fuplayer.PlaybackParameters;
import com.chengfu.android.fuplayer.Player;
import com.chengfu.android.fuplayer.Timeline;
import com.chengfu.android.fuplayer.audio.AudioAttributes;
import com.chengfu.android.fuplayer.audio.AudioListener;
import com.chengfu.android.fuplayer.audio.AuxEffectInfo;
import com.chengfu.android.fuplayer.ext.exo.util.MediaSourceUtil;
import com.chengfu.android.fuplayer.ext.exo.util.TypeConverter;
import com.chengfu.android.fuplayer.metadata.MetadataOutput;
import com.chengfu.android.fuplayer.source.TrackGroupArray;
import com.chengfu.android.fuplayer.text.TextOutput;
import com.chengfu.android.fuplayer.trackselection.TrackSelectionArray;
import com.chengfu.android.fuplayer.video.VideoFrameMetadataListener;
import com.chengfu.android.fuplayer.video.VideoListener;
import com.chengfu.android.fuplayer.video.spherical.CameraMotionListener;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;

import java.util.concurrent.CopyOnWriteArraySet;

public class ExoPlayer implements Player, Player.AudioComponent, Player.VideoComponent, Player.TextComponent, Player.MetadataComponent {

    private Context context;
    public com.google.android.exoplayer2.SimpleExoPlayer player;
    private final CopyOnWriteArraySet<EventListener> listeners;
    private final CopyOnWriteArraySet<VideoListener> videoListeners;
    private final CopyOnWriteArraySet<AudioListener> audioListeners;
    private final CopyOnWriteArraySet<TextOutput> textOutputs;
    private final CopyOnWriteArraySet<MetadataOutput> metadataOutputs;
    private VideoFrameMetadataListener videoFrameMetadataListener;
    private CameraMotionListener cameraMotionListener;


    public ExoPlayer(Context context) {
        this.context = context;
        player = ExoPlayerFactory.newSimpleInstance(context);

        listeners = new CopyOnWriteArraySet<>();
        videoListeners = new CopyOnWriteArraySet<>();
        audioListeners = new CopyOnWriteArraySet<>();
        textOutputs = new CopyOnWriteArraySet<>();
        metadataOutputs = new CopyOnWriteArraySet<>();
    }

    public void setDataSource(String path) {
        player.prepare(MediaSourceUtil.getMediaSource(context,path));
    }

    @Override
    public void addAudioListener(AudioListener listener) {

    }

    @Override
    public void removeAudioListener(AudioListener listener) {

    }

    @Override
    public void setAudioAttributes(AudioAttributes audioAttributes, boolean handleAudioFocus) {
        if (audioAttributes == null) {
            player.setAudioAttributes(null, handleAudioFocus);
            return;
        }
        com.google.android.exoplayer2.audio.AudioAttributes exoAttributes = new com.google.android.exoplayer2.audio.AudioAttributes.Builder()
                .setContentType(audioAttributes.contentType)
                .setFlags(audioAttributes.flags)
                .setUsage(audioAttributes.usage)
                .build();
        player.setAudioAttributes(exoAttributes, handleAudioFocus);

        player.addListener(mEventListener);

    }

    @Override
    public AudioAttributes getAudioAttributes() {
        com.google.android.exoplayer2.audio.AudioAttributes exoAttributes = player.getAudioAttributes();
        if (exoAttributes == null) {
            return null;
        }
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(exoAttributes.contentType)
                .setFlags(exoAttributes.flags)
                .setUsage(exoAttributes.usage)
                .build();
        return audioAttributes;
    }

    @Override
    public int getAudioSessionId() {
        return player.getAudioSessionId();
    }

    @Override
    public void setAuxEffectInfo(AuxEffectInfo auxEffectInfo) {
        if (auxEffectInfo == null) {
            player.setAuxEffectInfo(null);
            return;
        }
        com.google.android.exoplayer2.audio.AuxEffectInfo exoAuxEffectInfo = new com.google.android.exoplayer2.audio.AuxEffectInfo(auxEffectInfo.effectId, auxEffectInfo.sendLevel);
        player.setAuxEffectInfo(exoAuxEffectInfo);
    }

    @Override
    public void clearAuxEffectInfo() {
        player.clearAuxEffectInfo();
    }

    @Override
    public void setVolume(float audioVolume) {
        player.setVolume(audioVolume);
    }

    @Override
    public float getVolume() {
        return player.getVolume();
    }

    @Override
    public void setVideoScalingMode(int videoScalingMode) {
        player.setVideoScalingMode(videoScalingMode);
    }

    @Override
    public int getVideoScalingMode() {
        return player.getVideoScalingMode();
    }

    @Override
    public void addVideoListener(VideoListener listener) {
        videoListeners.add(listener);
    }

    @Override
    public void removeVideoListener(VideoListener listener) {
        videoListeners.remove(listener);
    }

    @Override
    public void setVideoFrameMetadataListener(VideoFrameMetadataListener listener) {
        videoFrameMetadataListener = listener;
        if (listener == null) {
            player.clearVideoFrameMetadataListener(mVideoFrameMetadataListener);
            return;
        }
        player.setVideoFrameMetadataListener(mVideoFrameMetadataListener);
    }

    @Override
    public void clearVideoFrameMetadataListener(VideoFrameMetadataListener listener) {
        if (videoFrameMetadataListener != listener) {
            return;
        }
        player.clearVideoFrameMetadataListener(mVideoFrameMetadataListener);
    }

    @Override
    public void setCameraMotionListener(CameraMotionListener listener) {
        cameraMotionListener = listener;
        if (listener == null) {
            player.clearCameraMotionListener(mCameraMotionListener);
            return;
        }
        player.setCameraMotionListener(mCameraMotionListener);
    }

    @Override
    public void clearCameraMotionListener(CameraMotionListener listener) {
        if (cameraMotionListener != listener) {
            return;
        }
        player.clearCameraMotionListener(mCameraMotionListener);
    }

    @Override
    public void clearVideoSurface() {
        player.clearVideoSurface();
    }

    @Override
    public void clearVideoSurface(Surface surface) {
        player.clearVideoSurface(surface);
    }

    @Override
    public void setVideoSurface(Surface surface) {
        player.setVideoSurface(surface);
    }

    @Override
    public void setVideoSurfaceHolder(SurfaceHolder surfaceHolder) {
        player.setVideoSurfaceHolder(surfaceHolder);
    }

    @Override
    public void clearVideoSurfaceHolder(SurfaceHolder surfaceHolder) {
        player.clearVideoSurfaceHolder(surfaceHolder);
    }

    @Override
    public void setVideoSurfaceView(SurfaceView surfaceView) {
        player.setVideoSurfaceView(surfaceView);
    }

    @Override
    public void clearVideoSurfaceView(SurfaceView surfaceView) {
        player.clearVideoSurfaceView(surfaceView);
    }

    @Override
    public void setVideoTextureView(TextureView textureView) {
        player.setVideoTextureView(textureView);
    }

    @Override
    public void clearVideoTextureView(TextureView textureView) {
        player.clearVideoTextureView(textureView);
    }

    @Override
    public void addTextOutput(TextOutput listener) {
        textOutputs.add(listener);
    }

    @Override
    public void removeTextOutput(TextOutput listener) {
        textOutputs.remove(listener);
    }

    @Override
    public void addMetadataOutput(MetadataOutput output) {
        metadataOutputs.add(output);
    }

    @Override
    public void removeMetadataOutput(MetadataOutput output) {
        metadataOutputs.remove(output);
    }

    @Override
    public AudioComponent getAudioComponent() {
        return this;
    }

    @Override
    public VideoComponent getVideoComponent() {
        return this;
    }

    @Override
    public TextComponent getTextComponent() {
        return this;
    }

    @Override
    public MetadataComponent getMetadataComponent() {
        return this;
    }

    @Override
    public Looper getApplicationLooper() {
        return player.getApplicationLooper();
    }

    @Override
    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public int getPlaybackState() {
        return player.getPlaybackState();
    }

    @Override
    public FuPlaybackException getPlaybackError() {
        if (player.getPlaybackError() == null) {
            return null;
        }
        ExoPlaybackException exoError = player.getPlaybackError();
        switch (exoError.type) {
            case ExoPlaybackException.TYPE_RENDERER:
                return FuPlaybackException.createForRenderer(exoError.getRendererException(), exoError.rendererIndex);
            case ExoPlaybackException.TYPE_SOURCE:
                return FuPlaybackException.createForRenderer(exoError.getSourceException(), exoError.rendererIndex);
            default:
                return FuPlaybackException.createForRenderer(exoError.getUnexpectedException(), exoError.rendererIndex);
        }
    }

    @Override
    public void setPlayWhenReady(boolean playWhenReady) {
        player.setPlayWhenReady(playWhenReady);
    }

    @Override
    public boolean getPlayWhenReady() {
        return player.getPlayWhenReady();
    }

    @Override
    public void setRepeatMode(int repeatMode) {
        player.setRepeatMode(repeatMode);
    }

    @Override
    public int getRepeatMode() {
        return player.getRepeatMode();
    }

    @Override
    public void setShuffleModeEnabled(boolean shuffleModeEnabled) {
        player.setShuffleModeEnabled(shuffleModeEnabled);
    }

    @Override
    public boolean getShuffleModeEnabled() {
        return player.getShuffleModeEnabled();
    }

    @Override
    public boolean isLoading() {
        return player.isLoading();
    }

    @Override
    public void seekToDefaultPosition() {
        player.seekToDefaultPosition();
    }

    @Override
    public void seekToDefaultPosition(int windowIndex) {
        player.seekToDefaultPosition(windowIndex);
    }

    @Override
    public void seekTo(long positionMs) {
        player.seekTo(positionMs);
    }

    @Override
    public void seekTo(int windowIndex, long positionMs) {
        player.seekTo(windowIndex, positionMs);
    }

    @Override
    public boolean hasPrevious() {
        return player.hasPrevious();
    }

    @Override
    public void previous() {
        player.previous();
    }

    @Override
    public boolean hasNext() {
        return player.hasNext();
    }

    @Override
    public void next() {
        player.next();
    }

    @Override
    public void setPlaybackParameters(PlaybackParameters playbackParameters) {
        player.setPlaybackParameters(TypeConverter.PlaybackParametersFuToExo(playbackParameters));

    }

    @Override
    public PlaybackParameters getPlaybackParameters() {
        return TypeConverter.PlaybackParametersExoToFu(player.getPlaybackParameters());
    }

    @Override
    public void stop() {
        player.stop();
    }

    @Override
    public void stop(boolean reset) {
        player.stop(reset);
    }

    @Override
    public void release() {
        player.release();
    }

    @Override
    public int getRendererCount() {
        return player.getRendererCount();
    }

    @Override
    public int getRendererType(int index) {
        return player.getRendererType(index);
    }

    @Override
    public TrackGroupArray getCurrentTrackGroups() {
        return TypeConverter.TrackGroupArrayExoToFu(player.getCurrentTrackGroups());
    }

    @Override
    public TrackSelectionArray getCurrentTrackSelections() {
        return TypeConverter.TrackSelectionArrayExoToFu(player.getCurrentTrackSelections());
    }

    @Override
    public Object getCurrentManifest() {
        return player.getCurrentManifest();
    }

    @Override
    public Timeline getCurrentTimeline() {
        return null;
    }

    @Override
    public int getCurrentPeriodIndex() {
        return player.getCurrentPeriodIndex();
    }

    @Override
    public int getCurrentWindowIndex() {
        return player.getCurrentWindowIndex();
    }

    @Override
    public int getNextWindowIndex() {
        return player.getNextWindowIndex();
    }

    @Override
    public int getPreviousWindowIndex() {
        return player.getPreviousWindowIndex();
    }

    @Override
    public Object getCurrentTag() {
        return player.getCurrentTag();
    }

    @Override
    public long getDuration() {
        return player.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public long getBufferedPosition() {
        return player.getBufferedPosition();
    }

    @Override
    public int getBufferedPercentage() {
        return player.getBufferedPercentage();
    }

    @Override
    public long getTotalBufferedDuration() {
        return player.getTotalBufferedDuration();
    }

    @Override
    public boolean isCurrentWindowDynamic() {
        return player.isCurrentWindowDynamic();
    }

    @Override
    public boolean isCurrentWindowSeekable() {
        return player.isCurrentWindowSeekable();
    }

    @Override
    public boolean isPlayingAd() {
        return player.isPlayingAd();
    }

    @Override
    public int getCurrentAdGroupIndex() {
        return player.getCurrentAdGroupIndex();
    }

    @Override
    public int getCurrentAdIndexInAdGroup() {
        return player.getCurrentAdIndexInAdGroup();
    }

    @Override
    public long getContentDuration() {
        return player.getContentDuration();
    }

    @Override
    public long getContentPosition() {
        return player.getContentPosition();
    }

    @Override
    public long getContentBufferedPosition() {
        return player.getContentBufferedPosition();
    }

    private com.google.android.exoplayer2.video.VideoFrameMetadataListener mVideoFrameMetadataListener = new com.google.android.exoplayer2.video.VideoFrameMetadataListener() {

        @Override
        public void onVideoFrameAboutToBeRendered(long presentationTimeUs, long releaseTimeNs, Format format) {
            if (videoFrameMetadataListener != null) {
                videoFrameMetadataListener.onVideoFrameAboutToBeRendered(presentationTimeUs, releaseTimeNs, format);
            }
        }
    };

    private com.google.android.exoplayer2.video.spherical.CameraMotionListener mCameraMotionListener = new com.google.android.exoplayer2.video.spherical.CameraMotionListener() {

        @Override
        public void onCameraMotion(long timeUs, float[] rotation) {
            if (cameraMotionListener != null) {
                cameraMotionListener.onCameraMotion(timeUs, rotation);
            }
        }

        @Override
        public void onCameraMotionReset() {
            if (cameraMotionListener != null) {
                cameraMotionListener.onCameraMotionReset();
            }
        }
    };

    private com.google.android.exoplayer2.Player.EventListener mEventListener = new com.google.android.exoplayer2.Player.EventListener() {
        @Override
        public void onLoadingChanged(boolean isLoading) {
            for (Player.EventListener listener : listeners) {
                listener.onLoadingChanged(isLoading);
            }
        }

        @Override
        public void onPlaybackParametersChanged(com.google.android.exoplayer2.PlaybackParameters playbackParameters) {
            for (Player.EventListener listener : listeners) {
                listener.onPlaybackParametersChanged(TypeConverter.PlaybackParametersExoToFu(playbackParameters));
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            for (Player.EventListener listener : listeners) {
                listener.onPlayerError(TypeConverter.PlaybackErrorExoToFu(error));
            }
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            for (Player.EventListener listener : listeners) {
                listener.onPlayerStateChanged(playWhenReady, playbackState);
            }
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            for (Player.EventListener listener : listeners) {
                listener.onPositionDiscontinuity(reason);
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            for (Player.EventListener listener : listeners) {
                listener.onRepeatModeChanged(repeatMode);
            }
        }

        @Override
        public void onSeekProcessed() {
            for (Player.EventListener listener : listeners) {
                listener.onSeekProcessed();
            }
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            for (Player.EventListener listener : listeners) {
                listener.onShuffleModeEnabledChanged(shuffleModeEnabled);
            }
        }

        @Override
        public void onTimelineChanged(com.google.android.exoplayer2.Timeline timeline, @Nullable Object manifest, int reason) {
            for (Player.EventListener listener : listeners) {
//                listener
            }
        }

        @Override
        public void onTracksChanged(com.google.android.exoplayer2.source.TrackGroupArray trackGroups, com.google.android.exoplayer2.trackselection.TrackSelectionArray trackSelections) {
            for (Player.EventListener listener : listeners) {
                listener.onTracksChanged(TypeConverter.TrackGroupArrayExoToFu(trackGroups), TypeConverter.TrackSelectionArrayExoToFu(trackSelections));
            }
        }

    };
}
