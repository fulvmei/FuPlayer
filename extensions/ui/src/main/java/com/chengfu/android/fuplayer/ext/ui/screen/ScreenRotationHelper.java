package com.chengfu.android.fuplayer.ext.ui.screen;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.provider.Settings;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.video.VideoListener;

public final class ScreenRotationHelper implements OrientationEventObserver.OnOrientationChangedListener {

    private static final String TAG = "ScreenRotationHelper";

    private static final float DEFAUT_RATE = 4f / 3f;//开启竖屏全屏模式的阈值

    private Activity activity;
    private Player player;
    private final ComponentListener componentListener;
    private final OrientationEventObserver orientationEventObserver;
    private OnScreenChangedListener onScreenChangedListener;

    private boolean disableInPlayerStateEnd;// 播放完成时是否可用，默认false
    private boolean disableInPlayerStateError;// 播放出错时是否可用，默认false
    private boolean toggleToPortraitInDisable;//不可用时切换到竖屏
    /**
     * 是否开启竖屏全屏模式,此模式下如果视频的高度与宽度比例大于1，则不会请求旋转屏幕
     */
    private boolean enablePortraitFullScreen;

    private int orientation;
    private boolean portraitFullScreen;

    private float videoRate;

    public interface OnScreenChangedListener {
        void onScreenChanged(boolean portraitFullScreen);
    }

    public ScreenRotationHelper(Activity activity) {
        this.activity = activity;

        componentListener = new ComponentListener();

        orientationEventObserver = new OrientationEventObserver(activity);

        orientationEventObserver.setOnOrientationChangedListener(this);
    }

    public void setOnScreenChangedListener(OnScreenChangedListener onScreenChangedListener) {
        this.onScreenChangedListener = onScreenChangedListener;
    }

    public OnScreenChangedListener getOnScreenChangedListener() {
        return onScreenChangedListener;
    }

    public void setPlayer(Player player) {
        if (this.player == player) {
            return;
        }
        if (this.player != null) {
            this.player.removeListener(componentListener);
            if (this.player.getVideoComponent() != null) {
                this.player.getVideoComponent().removeVideoListener(componentListener);
            }
        }
        this.player = player;
        if (player != null) {
            player.addListener(componentListener);
            if (this.player.getVideoComponent() != null) {
                this.player.getVideoComponent().addVideoListener(componentListener);
            }
        }
        videoRate = 0f;
        switchOrientationState();
    }

    private boolean isInPortraitFullScreenState() {
        return enablePortraitFullScreen && (videoRate == 0f || videoRate > DEFAUT_RATE);
    }

    private boolean isInEnableState() {
        if (player == null) {
            return false;
        }
//        if (stopInVideoHightBigger && (videoHeight * 3 > videoWidth * 4)) {
//            return false;
//        }
        if (player.getPlaybackState() == Player.STATE_READY || player.getPlaybackState() == Player.STATE_BUFFERING) {
            return true;
        }
        if (player.getPlaybackState() == Player.STATE_ENDED && !disableInPlayerStateEnd) {
            return true;
        }
        if (player.getPlaybackState() == Player.STATE_IDLE
                && player.getPlaybackError() != null
                && !disableInPlayerStateError) {
            return true;
        }

        return false;
    }

    private void switchOrientationState() {
        if (isInPortraitFullScreenState()) {
            orientationEventObserver.disable();
            if (toggleToPortraitInDisable && !isInEnableState()) {
                maybeToggleToPortrait();
            }
            return;
        }
        if (!isInEnableState()) {
            orientationEventObserver.disable();
            if (toggleToPortraitInDisable) {
                maybeToggleToPortrait();
            }
        } else {
            orientationEventObserver.enable();
        }
    }

    public void manualToggleOrientation() {
        if (isInPortraitFullScreenState()) {
            if (onScreenChangedListener != null) {
                portraitFullScreen = !portraitFullScreen;
                onScreenChangedListener.onScreenChanged(portraitFullScreen);
            }
            return;
        }
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            if (orientation > 0 && orientation <= 180) {

                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            } else {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            if (onScreenChangedListener != null) {
                onScreenChangedListener.onScreenChanged(false);
            }
        }
    }

    public boolean maybeToggleToPortrait() {
        if (isInPortraitFullScreenState()) {
            if (portraitFullScreen) {
                portraitFullScreen = false;
                if (onScreenChangedListener != null) {
                    onScreenChangedListener.onScreenChanged(false);
                }
                return true;
            }
            return false;
        }
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        }
        return false;
    }

    public void setEnablePortraitFullScreen(boolean enablePortraitFullScreen) {
        this.enablePortraitFullScreen = enablePortraitFullScreen;
    }

    public boolean isEnablePortraitFullScreen() {
        return enablePortraitFullScreen;
    }

    public void setDisableInPlayerStateEnd(boolean disableInPlayerStateEnd) {
        this.disableInPlayerStateEnd = disableInPlayerStateEnd;
        switchOrientationState();
    }

    public boolean isDisableInPlayerStateEnd() {
        return disableInPlayerStateEnd;
    }

    public void setDisableInPlayerStateError(boolean disableInPlayerStateError) {
        this.disableInPlayerStateError = disableInPlayerStateError;
        switchOrientationState();
    }

    public boolean isDisableInPlayerStateError() {
        return disableInPlayerStateError;
    }

    public void setToggleToPortraitInDisable(boolean toggleToPortraitInDisable) {
        this.toggleToPortraitInDisable = toggleToPortraitInDisable;
        switchOrientationState();
    }

    public boolean isToggleToPortraitInDisable() {
        return toggleToPortraitInDisable;
    }

    @Override
    public void onOrientationChanged(int orientation) {
        this.orientation = orientation;
    }

    @Override
    public void onScreenOrientationChanged(int screenOrientation) {
        Log.d(TAG, "onScreenOrientationChanged : screenOrientation=" + screenOrientation);
        int accelerometerRotation = Settings.System.getInt(activity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        if (accelerometerRotation != 1) {
            //如果用户设置关闭自动旋转屏幕，则不进行旋转
            return;
        }
        switch (screenOrientation) {
            case OrientationEventObserver.SCREEN_ORIENTATION_PORTRAIT:
                if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    if (onScreenChangedListener != null) {
                        onScreenChangedListener.onScreenChanged(false);
                    }
                }
                break;
            case OrientationEventObserver.SCREEN_ORIENTATION_LANDSCAPE:
                if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if (onScreenChangedListener != null) {
                        onScreenChangedListener.onScreenChanged(true);
                    }
                }
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case OrientationEventObserver.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
                if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if (onScreenChangedListener != null) {
                        onScreenChangedListener.onScreenChanged(true);
                    }
                }
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                break;
        }
    }

    private final class ComponentListener implements Player.EventListener, VideoListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_IDLE) {
                videoRate = 0f;
            }
            switchOrientationState();
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            videoRate = 0f;
            switchOrientationState();
        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            videoRate = width != 0 ? (float) height / width : 0f;
        }
    }
}
