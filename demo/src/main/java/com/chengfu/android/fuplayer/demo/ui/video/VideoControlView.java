package com.chengfu.android.fuplayer.demo.ui.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.chengfu.android.fuplayer.DefaultControlView;
import com.chengfu.android.fuplayer.demo.R;

public class VideoControlView extends DefaultControlView {

    ProgressBar mBottomProgressView;
    boolean showBottomProgress;
    boolean controlViewShow;

    public VideoControlView(Context context) {
        super(context);
    }

    public VideoControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected int getLayoutResourcesId(int layoutId) {
        return R.layout.layout_controller_view;
    }

    @Override
    protected void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        super.initView(context, attrs, defStyleAttr);

        mBottomProgressView = findViewById(R.id.controller_bottom_progress);
        if (mBottomProgressView != null) {
            mBottomProgressView.setMax(mSeekNumber);
        }
        updateBottomProgressView();
    }

    @Override
    protected void updateAll() {
        updateBottomProgressView();
        super.updateAll();
    }

    protected void updateBottomProgressView() {
        if (mBottomProgressView == null) {
            return;
        }
        if (isInShowState() && showBottomProgress && !controlViewShow) {
            mBottomProgressView.setVisibility(VISIBLE);
        } else {
            mBottomProgressView.setVisibility(GONE);
        }
    }

    @Override
    protected void updatePlayPauseViewResource(@NonNull ImageButton imageButton, boolean playWhenReady) {
        if (playWhenReady) {
            imageButton.setImageResource(R.mipmap.ic_player_pause);
        } else {
            imageButton.setImageResource(R.mipmap.ic_player_play);
        }
    }

    @Override
    protected boolean onProgressUpdate(long position, long duration, int bufferedPercent) {
        if (showBottomProgress && mBottomProgressView != null) {
            if (duration > 0) {
                long pos = mSeekNumber * position / duration;
                mBottomProgressView.setProgress((int) pos);
            }
            mBottomProgressView.setSecondaryProgress(bufferedPercent * 10);
        }
        return true;
    }

    @Override
    protected void onHideChanged(boolean hide) {
        super.onHideChanged(hide);
        controlViewShow = !hide;

        updateBottomProgressView();
    }

    public boolean isShowBottomProgress() {
        return showBottomProgress;
    }

    public void setShowBottomProgress(boolean showBottomProgress) {
        this.showBottomProgress = showBottomProgress;
        updateBottomProgressView();
    }
}
