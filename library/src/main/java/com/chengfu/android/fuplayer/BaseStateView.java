package com.chengfu.android.fuplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.ExoPlayer;


public abstract class BaseStateView extends FrameLayout implements StateView {

    protected ExoPlayer player;

    private VisibilityChangeListener visibilityChangeListener;
    protected boolean fullScreen;

    public interface VisibilityChangeListener {
        void onVisibilityChange(BaseStateView stateView, boolean visibility);
    }

    public BaseStateView(@NonNull Context context) {
        this(context, null);
    }

    public BaseStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VisibilityChangeListener getVisibilityChangeListener() {
        return visibilityChangeListener;
    }

    public void setVisibilityChangeListener(VisibilityChangeListener l) {
        this.visibilityChangeListener = l;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        if (this.fullScreen == fullScreen) {
            return;
        }
        this.fullScreen = fullScreen;
        onFullScreenChanged(fullScreen);
    }

    @Override
    public void setPlayer(ExoPlayer player) {
        if (this.player == player) {
            return;
        }
        ExoPlayer tempPlayer = this.player;
        if (player == null) {
            this.player = null;
            onDetachedFromPlayer(tempPlayer);
            return;
        }
        this.player = player;
        onAttachedToPlayer(player);
    }

    @Override
    public ExoPlayer getPlayer() {
        return player;
    }

    protected abstract void onFullScreenChanged(boolean fullScreen);

    protected abstract void onAttachedToPlayer(@NonNull ExoPlayer player);

    protected abstract void onDetachedFromPlayer(@NonNull ExoPlayer player);

    /**
     * Dispatch callbacks to {@link #setVisibilityChangeListener} down
     * the view hierarchy.
     */
    protected void dispatchVisibilityChanged(boolean visibility) {
        if (visibilityChangeListener != null) {
            visibilityChangeListener.onVisibilityChange(this, visibility);
        }
    }

}
