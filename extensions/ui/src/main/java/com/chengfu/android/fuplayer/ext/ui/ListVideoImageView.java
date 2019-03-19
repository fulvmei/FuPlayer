package com.chengfu.android.fuplayer.ext.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chengfu.android.fuplayer.BaseStateView;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;

public class ListVideoImageView extends BaseStateView {

    private ImageView image;
    private boolean canHidden;

    public ListVideoImageView(@NonNull Context context) {
        this(context, null);
    }

    public ListVideoImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListVideoImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = onCreateView(LayoutInflater.from(context), this);

        if (view != null) {
            addView(view);
        }

        image = findViewById(R.id.image);
    }

    @Override
    protected void onAttachedToPlayer(ExoPlayer player) {
        if (player.getPlaybackState() == Player.STATE_READY && player.getPlayWhenReady()) {
            canHidden = true;
        } else {
            canHidden = false;
        }
        updateVisibility();
    }

    @Override
    protected void onDetachedFromPlayer() {
        canHidden = false;
        updateVisibility();
    }

    protected View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.fpu_view_list_video_image, parent, false);
    }

    public ImageView getImage() {
        return image;
    }

    protected void updateVisibility() {
        if (isInShowState()) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }

    protected boolean isInShowState() {
        if (player == null) {
            return true;
        }
        if (player.getPlaybackState() == Player.STATE_READY) {
            return false;
        } else if (player.getPlaybackState() == Player.STATE_BUFFERING && canHidden) {
            return false;
        }
        return true;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        updateVisibility();
    }

    @Override
    public void onRenderedFirstFrame() {
        canHidden = true;
        updateVisibility();
    }
}
