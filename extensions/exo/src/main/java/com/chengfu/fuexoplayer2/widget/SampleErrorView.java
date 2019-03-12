package com.chengfu.fuexoplayer2.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chengfu.fuexoplayer2.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;

public class SampleErrorView extends BaseStateView {

    protected OnReTryClickListener onReTryClickListener;

    protected boolean showInDetachPlayer;

    public interface OnReTryClickListener {
        void onReTryClick(View v);
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

        updataVisibility();

        View retry = findViewById(R.id.btn_retry);
        if (retry != null) {
            retry.setOnClickListener(v -> {
                if (onReTryClickListener != null) {
                    onReTryClickListener.onReTryClick(v);
                }
            });
        }
    }

    protected View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.sample_error_view, parent, false);
    }

    protected void updataVisibility() {
        if (isInShowState()) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
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
    protected void onAttachedToPlayer(Player player) {
        updataVisibility();
    }

    @Override
    protected void onDetachedFromPlayer() {
        updataVisibility();
    }

    protected int getLayoutResourcesId() {
        return R.layout.sample_error_view;
    }

    public OnReTryClickListener getOnReTryClickListener() {
        return onReTryClickListener;
    }

    public void setOnReTryClickListener(OnReTryClickListener l) {
        this.onReTryClickListener = l;
    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        updataVisibility();
    }
}
