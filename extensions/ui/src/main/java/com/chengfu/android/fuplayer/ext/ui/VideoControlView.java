package com.chengfu.android.fuplayer.ext.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chengfu.android.fuplayer.DefaultControlView;
import com.chengfu.android.fuplayer.ext.ui.gesture.GestureHelper;

public class VideoControlView extends DefaultControlView implements GestureHelper.OnSlideChangedListener {

    private static final String TAG = "VideoControlView";

    View controllerTop;
    TextView titleView;
    ProgressBar mBottomProgressView;
    ImageView fullScreenView;
    ImageButton backView;
    String title;
    boolean showBottomProgress;
    boolean controlViewShow;
    boolean fullScreen;
    boolean showTopOnlyFullScreen;
    boolean showPlayPauseInBuffering;
    GestureHelper gestureHelper;
    GestureDetector mGestureDetector;

    protected OnScreenClickListener onScreenClickListener;

    public interface OnScreenClickListener {
        void onnScreenClick(boolean fullScreen);
    }

    protected OnBackClickListener onBackClickListener;

    public interface OnBackClickListener {

        void onBackClick(View v);
    }

    public VideoControlView(Context context) {
        this(context, null);
    }

    public VideoControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mGestureDetector = new GestureDetector(mContext, onGestureListener);

        gestureHelper = new GestureHelper(this);

        gestureHelper.setOnSlideChangedListener(this);
    }

    @Override
    protected int getLayoutResourcesId(int layoutId) {
        return R.layout.fpu_view_controller;
    }

    @Override
    protected void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        super.initView(context, attrs, defStyleAttr);

        controllerTop = findViewById(R.id.controller_top);

        updateTopVisibility();

        mBottomProgressView = findViewById(R.id.controller_bottom_progress);
        if (mBottomProgressView != null) {
            mBottomProgressView.setMax(mSeekNumber);
        }

        updateBottomProgressView();

        fullScreenView = findViewById(R.id.controller_screen_switch);
        if (fullScreenView != null) {
            fullScreenView.setOnClickListener(v -> {
                if (onScreenClickListener != null) {
                    onScreenClickListener.onnScreenClick(fullScreen);
                }
            });
            updateFullScreenViewResource(fullScreenView, fullScreen);
            hideAfterTimeout();
        }

        backView = findViewById(R.id.controller_back);

        if (backView != null) {
            backView.setOnClickListener(v -> {
                if (onBackClickListener != null) {
                    onBackClickListener.onBackClick(v);
                }
            });
            updateBackViewResource(backView, fullScreen);
            hideAfterTimeout();
        }

        titleView = findViewById(R.id.controller_title);

        setTitle(title);
    }

    @Override
    protected void updateAll() {
        updateBottomProgressView();
        super.updateAll();
    }

    @Override
    protected void updatePlayPauseViewResource(@NonNull ImageButton imageButton, boolean playWhenReady) {
        if (playWhenReady) {
            if (fullScreen) {
                imageButton.setImageResource(R.drawable.fpu_selector_pause_land);
            } else {
                imageButton.setImageResource(R.drawable.fpu_selector_pause_port);
            }
        } else {
            if (fullScreen) {
                imageButton.setImageResource(R.drawable.fpu_selector_play_land);
            } else {
                imageButton.setImageResource(R.drawable.fpu_selector_play_port);
            }

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

    public boolean isShowTopOnlyFullScreen() {
        return showTopOnlyFullScreen;
    }

    public void setShowTopOnlyFullScreen(boolean showTopOnlyFullScreen) {
        if (this.showTopOnlyFullScreen == showTopOnlyFullScreen) {
            return;
        }
        this.showTopOnlyFullScreen = showTopOnlyFullScreen;

        updateTopVisibility();
    }

    protected void updateTopVisibility() {
        if (controllerTop == null) {
            return;
        }
        if (showTopOnlyFullScreen && !fullScreen) {
            controllerTop.setVisibility(View.GONE);
        } else {
            controllerTop.setVisibility(View.VISIBLE);
        }
    }


    public void setTitle(String title) {
        this.title = title;
        if (titleView != null) {
            titleView.setText(TextUtils.isEmpty(title) ? "" : title);
        }
    }

    public String getTitle() {
        return title;
    }

    public OnScreenClickListener getOnScreenClickListener() {
        return onScreenClickListener;
    }

    public void setOnScreenClickListener(OnScreenClickListener onScreenClickListener) {
        this.onScreenClickListener = onScreenClickListener;
    }

    public OnBackClickListener getOnBackClickListener() {
        return onBackClickListener;
    }

    public void setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        if (this.fullScreen == fullScreen) {
            return;
        }
        this.fullScreen = fullScreen;

        updateFullScreenViewResource(fullScreenView, fullScreen);
        updateBackViewResource(backView, fullScreen);
        updatePlayPauseView();
        updateVolumeView();
        updateTopVisibility();
    }

    protected void updateBackViewResource(@Nullable ImageView imageButton, boolean fullScreen) {
        if (imageButton == null) {
            return;
        }
        if (fullScreen) {
            imageButton.setImageResource(R.drawable.fpu_selector_back_land);
        } else {
            imageButton.setImageResource(R.drawable.fpu_selector_back_port);
        }
    }

    protected void updateFullScreenViewResource(@Nullable ImageView imageButton, boolean fullScreen) {
        if (imageButton == null) {
            return;
        }
        if (fullScreen) {
            imageButton.setImageResource(R.drawable.fpu_ic_exit_full_screen);
        } else {
            imageButton.setImageResource(R.drawable.fpu_ic_full_screen);
        }
    }

    @Override
    protected void updateVolumeViewResource(@NonNull ImageButton imageButton, float volume) {
        if (volume > 0.0f) {
            if (fullScreen) {
                imageButton.setImageResource(R.drawable.fpu_ic_control_volume_on_land);
            } else {
                imageButton.setImageResource(R.drawable.fpu_ic_control_volume_on_port);
            }

        } else {
            if (fullScreen) {
                imageButton.setImageResource(R.drawable.fpu_ic_control_volume_off_land);
            } else {
                imageButton.setImageResource(R.drawable.fpu_ic_control_volume_off_port);
            }
        }
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

    public boolean isShowBottomProgress() {
        return showBottomProgress;
    }

    public void setShowBottomProgress(boolean showBottomProgress) {
        this.showBottomProgress = showBottomProgress;
        updateBottomProgressView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            gestureHelper.onUp(ev);
        }
        if (isInShowState()) {
            return mGestureDetector.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            gestureHelper.onUp(ev);
        }
        if (isInShowState()) {
            return mGestureDetector.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public void onStartSlide(int slideType) {
        if (isShowing()) {
            hide();
        }
        switch (slideType) {
            case GestureHelper.SLIDE_TYPE_HORIZONTAL:
                findViewById(R.id.p).setVisibility(View.VISIBLE);
                break;
            case GestureHelper.SLIDE_TYPE_LEFT_VERTICAL:
                findViewById(R.id.b).setVisibility(View.VISIBLE);
                break;
            case GestureHelper.SLIDE_TYPE_RIGHT_VERTICAL:
                findViewById(R.id.v).setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onProgressChanged(int slideType, float progress) {

    }

    @Override
    public void onStopSlide(int slideType) {
        switch (slideType) {
            case GestureHelper.SLIDE_TYPE_HORIZONTAL:
                findViewById(R.id.p).setVisibility(View.GONE);
                break;
            case GestureHelper.SLIDE_TYPE_LEFT_VERTICAL:
                findViewById(R.id.b).setVisibility(View.GONE);
                break;
            case GestureHelper.SLIDE_TYPE_RIGHT_VERTICAL:
                findViewById(R.id.v).setVisibility(View.GONE);
                break;
        }
    }

    private final GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            gestureHelper.onDown(e);
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (isShowing()) {
                hide();
            } else {
                show();
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            gestureHelper.onScroll(e1, e2, distanceX, distanceY);
            return true;
        }
    };

}
