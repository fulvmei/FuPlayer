package com.chengfu.android.fuplayer.demo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.view.OrientationEventListener;

public class ScreenRotationHelper extends ContentObserver {

    ContentResolver mResolver;
    int mAccelerometerRotation;
    OrientationEventListener orientationEventListener;

    public ScreenRotationHelper(Context context) {
        super(new Handler());
        mResolver = context.getContentResolver();
    }

    private class RotationObserver extends ContentObserver {
        ContentResolver mResolver;

        public RotationObserver(Context context) {
            super(new Handler());
            mResolver = context.getContentResolver();
        }

        //屏幕旋转设置改变时调用
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mAccelerometerRotation = Settings.System.getInt(mResolver, Settings.System.ACCELEROMETER_ROTATION, 0);
        }

        public void startObserver() {
            mResolver.registerContentObserver(Settings.System
                            .getUriFor(Settings.System.ACCELEROMETER_ROTATION), false,
                    this);
        }

        public void stopObserver() {
            mResolver.unregisterContentObserver(this);
        }
    }
}
