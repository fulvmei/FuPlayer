package com.chengfu.android.fuplayer.demo.util;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.view.OrientationEventListener;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;

public class ScreenRotationHelper {

    private Activity activity;
    private Player player;
    private final PlayerEventListener playerEventListener;
    private final OrientationEventListener orientationEventListener;
    private boolean enableInPlayerStateBuffering;
    private boolean enableInPlayerStateEnd;
    private boolean enableInPlayerStateError;
    private boolean startSign;
    private boolean clickPSign;// 竖屏模式下点击屏幕切换按钮
    private boolean clickLSign;// 横屏模式下点击屏幕切换按钮
    private int orientation;
    private int accelerometerRotation;//屏幕旋转系统设置（1 开启、0 关闭）
    private RotationObserver rotationObserver;//屏幕旋转系统设置监听


    public ScreenRotationHelper(Activity activity) {
        this.activity = activity;
        accelerometerRotation = Settings.System.getInt(activity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        rotationObserver = new RotationObserver(activity);
        playerEventListener = new PlayerEventListener();
        orientationEventListener = new OrientationEventListener(activity) {
            @Override
            public void onOrientationChanged(int orientation) {
                ScreenRotationHelper.this.orientation = orientation;

                if (clickPSign) {
                    if (getOrientation(orientation) == 1 || getOrientation(orientation) == 2) {
                        clickPSign = false;
                    }
                    return;
                }
                if (clickLSign) {
                    if (getOrientation(orientation) == 0 || getOrientation(orientation) == 3) {
                        clickLSign = false;
                    }
                    return;
                }

                switch (getOrientation(orientation)) {
                    case 0:
                    case 3:
                        if (accelerometerRotation == 0 && activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            return;
                        }
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                    case 1:
                        if (accelerometerRotation == 0 && activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            return;
                        }
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        break;
                    case 2:
                        if (accelerometerRotation == 0 && activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            return;
                        }
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void setPlayer(Player player) {
        if (this.player == player) {
            return;
        }
        if (this.player != null) {
            this.player.removeListener(playerEventListener);

        }
        this.player = player;
        if (player != null) {
            player.addListener(playerEventListener);
        }

        switchOrientationState();
    }

    public boolean isEnableInPlayerStateBuffering() {
        return enableInPlayerStateBuffering;
    }

    public void setEnableInPlayerStateBuffering(boolean enableInPlayerStateBuffering) {
        this.enableInPlayerStateBuffering = enableInPlayerStateBuffering;
    }

    public boolean isEnableInPlayerStateEnd() {
        return enableInPlayerStateEnd;
    }

    public void setEnableInPlayerStateEnd(boolean enableInPlayerStateEnd) {
        if (this.enableInPlayerStateEnd == enableInPlayerStateEnd) {
            return;
        }
        this.enableInPlayerStateEnd = enableInPlayerStateEnd;
    }

    public boolean isEnableInPlayerStateError() {
        return enableInPlayerStateError;
    }

    public void setEnableInPlayerStateError(boolean enableInPlayerStateError) {
        if (this.enableInPlayerStateError == enableInPlayerStateError) {
            return;
        }
        this.enableInPlayerStateError = enableInPlayerStateError;
    }

    public void resume() {
        startSign = true;
        accelerometerRotation = Settings.System.getInt(activity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        rotationObserver.startObserver();
        switchOrientationState();
    }

    public void pause() {
        startSign = false;
        rotationObserver.stopObserver();
        switchOrientationState();
    }


    private int getOrientation(int rotation) {
        if (((rotation >= 0) && (rotation <= 30)) || (rotation >= 330)) {
            return 0;
        } else if ((rotation >= 240) && (rotation <= 300)) {
            return 1;
        } else if ((rotation >= 60) && (rotation <= 120)) {
            return 2;
        } else if ((rotation >= 150) && (rotation <= 210)) {
            return 3;
        } else {
            return -1;
        }
    }

    private void switchOrientationState() {
        if (startSign && isInEnableState()) {
            orientationEventListener.enable();
        } else {
            orientationEventListener.disable();
        }
    }

    public void manualToggleOrientation() {
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            clickPSign = true;
            clickLSign = false;
            if (orientation > 0 && orientation <= 180) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            } else {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            clickLSign = true;
            clickPSign = false;
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    protected boolean isInEnableState() {
        if (player == null) {
            return false;
        }
        if (player.getPlaybackState() == Player.STATE_READY) {
            return true;
        }
        if (player.getPlaybackState() == Player.STATE_BUFFERING && enableInPlayerStateBuffering) {
            return true;
        }
        if (player.getPlaybackState() == Player.STATE_ENDED && enableInPlayerStateEnd) {
            return true;
        }
        if (player.getPlaybackState() == Player.STATE_IDLE
                && player.getPlaybackError() != null
                && enableInPlayerStateError) {
            return true;
        }

        return false;
    }

    private class PlayerEventListener implements Player.EventListener {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switchOrientationState();
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            switchOrientationState();
        }
    }

    private class RotationObserver extends ContentObserver {
        ContentResolver resolver;

        public RotationObserver(Activity activity) {
            super(new Handler());
            resolver = activity.getContentResolver();
        }

        //屏幕旋转设置改变时调用
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            accelerometerRotation = Settings.System.getInt(resolver, Settings.System.ACCELEROMETER_ROTATION, 0);
        }

        public void startObserver() {
            resolver.registerContentObserver(Settings.System
                            .getUriFor(Settings.System.ACCELEROMETER_ROTATION), false,
                    this);
        }

        public void stopObserver() {
            resolver.unregisterContentObserver(this);
        }
    }
}
