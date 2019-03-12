package com.chengfu.fuexoplayer2.demo.ui.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chengfu.android.fuplayer.FuPlaybackException;
import com.chengfu.android.fuplayer.Player;
import com.chengfu.android.fuplayer.ui.BaseStateView;
import com.chengfu.fuexoplayer2.demo.R;

public class ListVideoPlayView extends BaseStateView {

    protected OnPlayerClickListener onPlayerClickListener;

    public interface OnPlayerClickListener {
        void onPlayClick(View v);
    }


    public ListVideoPlayView(@NonNull Context context) {
        this(context, null);
    }

    public ListVideoPlayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListVideoPlayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = onCreateView(LayoutInflater.from(context), this);

        if (view != null) {
            addView(view);
        }

        updateVisibility();

        View play = findViewById(R.id.play);
        if (play != null) {
            play.setOnClickListener(v -> {
                if (onPlayerClickListener != null) {
                    onPlayerClickListener.onPlayClick(v);
                }
            });
        }
    }

    protected View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.view_list_video_play, parent, false);
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
        if ((player.getPlaybackState() == Player.STATE_ENDED)
                || (player.getPlaybackState() == Player.STATE_IDLE && player.getPlaybackError() == null)) {
            return true;
        }
        return false;
    }

    public OnPlayerClickListener getOnPlayerClickListener() {
        return onPlayerClickListener;
    }

    public void setOnPlayerClickListener(OnPlayerClickListener onPlayerClickListener) {
        this.onPlayerClickListener = onPlayerClickListener;
    }

    @Override
    protected void onAttachedToPlayer(Player player) {
        updateVisibility();
    }

    @Override
    protected void onDetachedFromPlayer() {
        updateVisibility();
    }

    @Override
    public void onPlayerError(FuPlaybackException error) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        updateVisibility();
    }
}
