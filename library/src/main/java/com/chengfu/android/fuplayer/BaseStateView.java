package com.chengfu.android.fuplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.video.VideoListener;


public abstract class BaseStateView extends FrameLayout implements Player.EventListener, VideoListener {

    protected Player player;

    public BaseStateView(@NonNull Context context) {
        this(context, null);
    }

    public BaseStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPlayer(Player player) {
        if (this.player == player) {
            return;
        }
        if (this.player != null) {
            this.player.removeListener(this);
            if (this.player.getVideoComponent() != null) {
                this.player.getVideoComponent().removeVideoListener(this);
            }
        }
        this.player = player;
        if (player != null) {
            player.addListener(this);
            if (player.getVideoComponent() != null) {
                player.getVideoComponent().addVideoListener(this);
            }
            onAttachedToPlayer(player);
        } else {
            onDetachedFromPlayer();
        }

    }

    protected abstract void onAttachedToPlayer(Player player);

    protected abstract void onDetachedFromPlayer();

}
