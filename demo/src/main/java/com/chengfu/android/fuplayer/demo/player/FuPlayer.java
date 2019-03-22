package com.chengfu.android.fuplayer.demo.player;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;

import com.chengfu.android.fuplayer.BaseStateView;
import com.chengfu.android.fuplayer.FuPlayerView;
import com.chengfu.android.fuplayer.ext.ui.VideoControlView;
import com.chengfu.android.fuplayer.ext.ui.VideoPlayWithoutWifiView;
import com.chengfu.android.fuplayer.ext.ui.screen.ScreenRotationHelper;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.DefaultPlaybackController;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.source.MediaSource;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

public class FuPlayer extends PlayerAdapter {

    private static final String TAG = "FuPlayer";

    private Activity mActivity;
    private final ComponentListener mComponentListener;
    private MediaSessionCompat mMediaSession;
    private FuPlayerView mPlayerView;
    private VideoControlView mVideoControlView;
    private ScreenRotationHelper mScreenRotation;
    private CopyOnWriteArraySet<StateViewHolder> mStateViewsHolder = new CopyOnWriteArraySet<>();
    private boolean needPlayOnResume;
    private VideoPlayWithoutWifiView noWifiView;

    public VideoPlayWithoutWifiView getNoWifiView() {
        return noWifiView;
    }

    public void setNoWifiView(VideoPlayWithoutWifiView noWifiView) {
        this.noWifiView = noWifiView;
    }

    public FuPlayer(@NonNull Activity activity, @NonNull ExoPlayer player) {
        this(player);
        this.mActivity = activity;

        mMediaSession = new MediaSessionCompat(activity, TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        MediaSessionConnector mediaSessionConnector = new MediaSessionConnector(mMediaSession, new DefaultPlaybackController() {
            @Override
            public void onPlay(Player player) {
                super.onPlay(player);
                showController();
            }

            @Override
            public void onPause(Player player) {
                super.onPause(player);
                showController();

            }
        });
        mediaSessionConnector.setPlayer(player, null);

        player.addListener(mComponentListener);

    }

    protected FuPlayer(@NonNull ExoPlayer player) {
        super(player);
        mComponentListener = new ComponentListener();

    }

    public FuPlayerView getPlayerView() {
        return mPlayerView;
    }

    public void setPlayerView(FuPlayerView playerView) {
        if (this.mPlayerView == playerView) {
            return;
        }
        if (this.mPlayerView != null) {
            this.mPlayerView.setPlayer(null);
        }
        this.mPlayerView = playerView;
        if (playerView != null) {
            playerView.setPlayer(mPlayer);
        }
    }

    public void setVideoControlView(VideoControlView controlView) {
        if (this.mVideoControlView == controlView) {
            return;
        }
        if (this.mVideoControlView != null) {
            this.mVideoControlView.setPlayer(null);
            this.mVideoControlView.setOnScreenClickListener(null);
            this.mVideoControlView.setOnBackClickListener(null);
        }
        this.mVideoControlView = controlView;
        if (controlView != null) {
            mVideoControlView.setPlayer(mPlayer);
            this.mVideoControlView.setOnScreenClickListener(mComponentListener);
            this.mVideoControlView.setOnBackClickListener(mComponentListener);
        }
    }

    public VideoControlView getVideoControlView() {
        return mVideoControlView;
    }

    public void addStateView(BaseStateView stateView) {
        addStateView(stateView, false);
    }

    public void addStateView(BaseStateView stateView, boolean hideController) {
        if (stateView == null) {
            return;
        }
        StateViewHolder stateViewHolder = new StateViewHolder(stateView, hideController);
        if (mStateViewsHolder.contains(stateViewHolder)) {
            return;
        }
        mStateViewsHolder.add(stateViewHolder);
        stateView.setVisibilityChangeListener(mComponentListener);
        stateView.setPlayer(mPlayer);
    }

    public void removeStateView(BaseStateView stateView) {
        if (stateView == null) {
            return;
        }
        StateViewHolder stateViewHolder = new StateViewHolder(stateView, false);
        if (!mStateViewsHolder.contains(stateViewHolder)) {
            return;
        }
        mStateViewsHolder.remove(stateViewHolder);
        stateView.setPlayer(null);
        stateView.setVisibilityChangeListener(null);
    }

    public void clearStateViews() {
        for (StateViewHolder holder : mStateViewsHolder) {
            holder.stateView.setPlayer(null);
            holder.stateView.setVisibilityChangeListener(null);
        }
        mStateViewsHolder.clear();
    }

    public void setScreenRotation(ScreenRotationHelper screenRotation) {
        if (this.mScreenRotation == screenRotation) {
            return;
        }
        if (this.mScreenRotation != null) {
            this.mScreenRotation.setPlayer(null);
        }
        this.mScreenRotation = screenRotation;
        if (screenRotation != null) {
            screenRotation.setPlayer(mPlayer);
        }
    }

    public ScreenRotationHelper getScreenRotation() {
        return mScreenRotation;
    }

    public void showController() {
        if (mVideoControlView == null) {
            return;
        }
        mVideoControlView.show();
    }

    public void maybeHideController() {
        if (mVideoControlView == null) {
            return;
        }
        if (mVideoControlView.isShowing()) {
            mVideoControlView.hide();
        }
    }

    private StateViewHolder getStateViewHolder(BaseStateView stateView) {
        for (StateViewHolder stateViewHolder : mStateViewsHolder) {
            if (stateViewHolder.stateView == stateView) {
                return stateViewHolder;
            }
        }
        return null;
    }


    public void onResume() {
        if (mPlayerView != null) {
            mPlayerView.onResume();
        }
        if (mScreenRotation != null) {
            mScreenRotation.resume();
        }
        if (!mPlayer.getPlayWhenReady() && needPlayOnResume) {
            mPlayer.setPlayWhenReady(true);
            needPlayOnResume = false;
        }
    }


    public void onPause() {
        if (mPlayerView != null) {
            mPlayerView.onPause();
        }
        if (mScreenRotation != null) {
            mScreenRotation.pause();
        }
        if (mPlayer.getPlayWhenReady()) {
            needPlayOnResume = true;
            mPlayer.setPlayWhenReady(false);
        }
    }

    public boolean onBackPressed() {
        if (mScreenRotation != null) {
            return mScreenRotation.maybeToggleToPortrait();
        }
        return false;
    }

    public void onDestroy() {
        if (mPlayerView != null) {
            mPlayerView.onPause();
        }
        if (mScreenRotation != null) {
            mScreenRotation.pause();
        }
        mPlayer.release();
    }

    private final class ComponentListener implements BaseStateView.VisibilityChangeListener, Player.EventListener, VideoControlView.OnScreenClickListener, VideoControlView.OnBackClickListener {

        @Override
        public void onVisibilityChange(BaseStateView stateView, boolean visibility) {
            StateViewHolder holder = getStateViewHolder(stateView);
            if (holder != null && holder.hideController && visibility) {
                maybeHideController();
            }
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_READY) {
                mMediaSession.setActive(true);
            } else {
                mMediaSession.setActive(false);
            }
        }

        @Override
        public void onScreenClick(boolean fullScreen) {
            if (mScreenRotation != null) {
                mScreenRotation.manualToggleOrientation();
            }
        }

        @Override
        public void onBackClick(View v) {
            if (mScreenRotation != null) {
                mScreenRotation.maybeToggleToPortrait();
            }
        }
    }

    private class StateViewHolder {
        BaseStateView stateView;
        boolean hideController;

        public StateViewHolder(BaseStateView stateView) {
            this.stateView = stateView;
        }

        public StateViewHolder(BaseStateView stateView, boolean hideController) {
            this.stateView = stateView;
            this.hideController = hideController;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof StateViewHolder) {
                StateViewHolder stateViewHolder = (StateViewHolder) obj;
                return stateView == stateViewHolder.stateView;
            } else {
                return false;
            }
        }
    }
}
