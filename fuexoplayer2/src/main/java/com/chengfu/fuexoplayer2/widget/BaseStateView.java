package com.chengfu.fuexoplayer2.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;


public abstract class BaseStateView extends FrameLayout {

    private Player mPlayer;
    private ComponentListener mComponentListener;
    protected ExoPlaybackException mPlayerError;

    public BaseStateView(@NonNull Context context) {
        this(context, null);
    }

    public BaseStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mComponentListener = new ComponentListener();
        mPlayerError = null;
    }

    public void setPlayer(Player player) {
        if (mPlayer == player) {
            return;
        }
        if (mPlayer != null) {
            mPlayer.removeListener(mComponentListener);
            mPlayerError = null;
            detachPlayer();
        }
        mPlayer = player;

        if (player != null) {
            mPlayerError = player.getPlaybackError();
            onStateChanged(player.getPlayWhenReady(), player.getPlaybackState());
            player.addListener(mComponentListener);
        }
    }

    abstract void onStateChanged(boolean playWhenReady, int playbackState);

    abstract void detachPlayer();

    private final class ComponentListener implements Player.EventListener {
        @Override
        public void onPlayerError(ExoPlaybackException error) {
            mPlayerError = error;
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState != Player.STATE_IDLE || mPlayer.getPlaybackError() == null) {
                mPlayerError = null;
            }
            BaseStateView.this.onStateChanged(playWhenReady, playbackState);
        }
    }


}
