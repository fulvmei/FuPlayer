package com.chengfu.android.fuplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;

public class SampleErrorView extends BaseStateView {

    protected final ComponentListener componentListener;

    protected OnRetryListener onRetryListener;

    protected boolean showInDetachPlayer;

    public interface OnRetryListener {
        boolean onRetry(ExoPlayer player);
    }

    public SampleErrorView(@NonNull Context context) {
        this(context, null);
    }

    public SampleErrorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleErrorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = onCreateView(LayoutInflater.from(context), this);

        if (view != null) {
            addView(view);
        }

        componentListener = new ComponentListener();

        updateVisibility();

        View retry = findViewById(R.id.btn_retry);
        if (retry != null) {
            retry.setOnClickListener(v -> {
                if (onRetryListener != null && onRetryListener.onRetry(player)) {
                    return;
                }
                if (player != null) {
                    player.retry();
                }
            });
        }
    }

    @Override
    protected void onFullScreenChanged(boolean fullScreen) {

    }

    protected View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.sample_error_view, parent, false);
    }

    protected void updateVisibility() {
        if (isInShowState()) {
            if (getVisibility() == GONE) {
                setVisibility(VISIBLE);
                dispatchVisibilityChanged(true);
            }
        } else {
            if (getVisibility() == VISIBLE) {
                setVisibility(GONE);
                dispatchVisibilityChanged(false);
            }
        }
    }

    protected boolean isInShowState() {
        if (player == null) {
            return showInDetachPlayer;
        }
        if (player.getPlaybackState() == Player.STATE_IDLE && player.getPlaybackError() != null) {
            return true;
        }
        return false;
    }

    @Override
    protected void onAttachedToPlayer(@NonNull ExoPlayer player) {
        player.addListener(componentListener);
        updateVisibility();
    }

    @Override
    protected void onDetachedFromPlayer(@NonNull ExoPlayer player) {
        player.removeListener(componentListener);
        updateVisibility();
    }

    protected int getLayoutResourcesId() {
        return R.layout.sample_error_view;
    }

    public OnRetryListener getOnRetryListener() {
        return onRetryListener;
    }

    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.onRetryListener = onRetryListener;
    }

    private final class ComponentListener implements Player.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            updateVisibility();
        }
    }
}
