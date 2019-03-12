package com.chengfu.player.extensions.pldroid;

import android.graphics.SurfaceTexture;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.chengfu.fuexoplayer2.FuLog;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.video.VideoListener;

import java.util.concurrent.CopyOnWriteArraySet;

public abstract class PLPlayerBase implements Player, Player.VideoComponent {

    private static final String TAG = "PLPlayerBase";

    protected final CopyOnWriteArraySet<VideoListener> mVideoListeners = new CopyOnWriteArraySet<>();
    private final ComponentListener mComponentListener = new ComponentListener();

    private Surface mSurface;
    private SurfaceHolder mSurfaceHolder;
    private TextureView mTextureView;

    protected abstract void setSurface(Surface surface);

    @Nullable
    @Override
    public VideoComponent getVideoComponent() {
        return this;
    }

    @Override
    public void addVideoListener(VideoListener listener) {
        mVideoListeners.add(listener);
    }

    @Override
    public void removeVideoListener(VideoListener listener) {
        mVideoListeners.remove(listener);
    }

    @Override
    public void clearVideoSurface() {
        mSurface = null;
        setSurface(null);
    }

    @Override
    public void clearVideoSurface(Surface surface) {
        if (mSurface == surface) {
            setSurface(null);
        }
    }

    @Override
    public void setVideoSurface(@Nullable Surface surface) {
        mSurface = surface;
        setSurface(surface);
    }

    @Override
    public void setVideoSurfaceHolder(SurfaceHolder surfaceHolder) {
        Log.w(TAG, "setVideoSurfaceHolder  surfaceHolder=" + surfaceHolder);
        if (mSurfaceHolder == surfaceHolder) {
            return;
        }
        removeSurfaceCallbacks();
        mSurfaceHolder = surfaceHolder;
        if (surfaceHolder == null) {
            setVideoSurface(null);
        } else {
            surfaceHolder.addCallback(mComponentListener);
            Surface surface = surfaceHolder.getSurface();
            setVideoSurface(surface != null && surface.isValid() ? surface : null);
        }
    }

    @Override
    public void clearVideoSurfaceHolder(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void setVideoSurfaceView(SurfaceView surfaceView) {
        setVideoSurfaceHolder(surfaceView == null ? null : surfaceView.getHolder());
    }

    @Override
    public void clearVideoSurfaceView(SurfaceView surfaceView) {

    }

    @Override
    public void setVideoTextureView(TextureView textureView) {
        Log.w(TAG, "setVideoTextureView  textureView=" + textureView);
        if (mTextureView == textureView) {
            return;
        }
        removeSurfaceCallbacks();
        mTextureView = textureView;
        if (textureView == null) {
            setVideoSurface(null);
        } else {
            if (textureView.getSurfaceTextureListener() != null) {
                Log.w(TAG, "Replacing existing SurfaceTextureListener.");
            }
            textureView.setSurfaceTextureListener(mComponentListener);
            SurfaceTexture surfaceTexture = textureView.isAvailable() ? textureView.getSurfaceTexture()
                    : null;
            setVideoSurface(surfaceTexture == null ? null : new Surface(surfaceTexture));
        }
    }

    @Override
    public void clearVideoTextureView(TextureView textureView) {

    }

    protected final void submitVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        for (VideoListener listener : mVideoListeners) {
            listener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    protected final void submitSurfaceSizeChanged(int width, int height) {
        for (VideoListener listener : mVideoListeners) {
            listener.onSurfaceSizeChanged(width, height);
        }
    }

    protected final void submitRenderedFirstFrame() {
        for (VideoListener listener : mVideoListeners) {
            listener.onRenderedFirstFrame();
        }
    }

    private void removeSurfaceCallbacks() {
        if (mTextureView != null) {
            if (mTextureView.getSurfaceTextureListener() != mComponentListener) {
                FuLog.w(TAG, "SurfaceTextureListener already unset or replaced.");
            } else {
                mTextureView.setSurfaceTextureListener(null);
            }
            mTextureView = null;
        }
        if (mSurfaceHolder != null) {
            mSurfaceHolder.removeCallback(mComponentListener);
            mSurfaceHolder = null;
        }
    }

    private final class ComponentListener implements SurfaceHolder.Callback,
            TextureView.SurfaceTextureListener {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            FuLog.d(TAG, "surfaceCreated");
            setVideoSurface(holder.getSurface());
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            FuLog.d(TAG, "surfaceDestroyed");
            setVideoSurface(null);
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            FuLog.d(TAG, "onSurfaceTextureAvailable");
            setVideoSurface(new Surface(surfaceTexture));
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            FuLog.d(TAG, "onSurfaceTextureDestroyed");
            setVideoSurface(null);
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    }
}
