package com.chengfu.android.fuplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.Player;

public class SampleEndedView extends BaseStateView {

    protected OnReTryClickListener onReTryClickListener;

    protected boolean showInDetachPlayer;

    public interface OnReTryClickListener {
        void onReTryClick(View v);
    }

    public SampleEndedView(@NonNull Context context) {
        this(context, null);
    }

    public SampleEndedView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleEndedView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    @Override
    protected void onAttachedToPlayer(Player player) {
        updataVisibility();
    }

    @Override
    protected void onDetachedFromPlayer() {
        updataVisibility();
    }

    protected View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.sample_ended_view, parent, false);
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
        if (player.getPlaybackState() == Player.STATE_ENDED) {
            return true;
        }
        return false;
    }

    public boolean isShowInDetachPlayer() {
        return showInDetachPlayer;
    }

    public void setShowInDetachPlayer(boolean showInDetachPlayer) {
        this.showInDetachPlayer = showInDetachPlayer;
        updataVisibility();
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
