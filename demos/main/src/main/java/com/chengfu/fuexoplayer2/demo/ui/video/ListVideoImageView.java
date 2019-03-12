package com.chengfu.fuexoplayer2.demo.ui.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chengfu.android.fuplayer.Player;
import com.chengfu.android.fuplayer.ui.BaseStateView;
import com.chengfu.fuexoplayer2.demo.R;

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
    protected void onAttachedToPlayer(Player player) {
        if (player.getPlaybackState() == Player.STATE_READY && player.getPlayWhenReady()) {
            canHidden = true;
        } else {
            canHidden = false;
        }
        System.out.println("onAttachedToPlayer  " + player.getPlaybackState() + ",canHidden=" + canHidden);
        updataVisibility();
    }

    @Override
    protected void onDetachedFromPlayer() {
        canHidden = false;
        updataVisibility();
    }

    protected View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.view_list_video_image, parent, false);
    }

    public ImageView getImage() {
        return image;
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

        updataVisibility();
    }

    @Override
    public void onRenderedFirstFrame() {
        canHidden = true;
        updataVisibility();
    }
}
