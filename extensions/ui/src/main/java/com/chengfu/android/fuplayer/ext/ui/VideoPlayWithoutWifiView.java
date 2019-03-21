package com.chengfu.android.fuplayer.ext.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class VideoPlayWithoutWifiView extends FrameLayout {

    protected OnPlayClickListener onPlayClickListener;

    public interface OnPlayClickListener {
        void onPlayClick(View v);
    }

    public VideoPlayWithoutWifiView(@NonNull Context context) {
        this(context, null);
    }

    public VideoPlayWithoutWifiView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayWithoutWifiView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.fpu_view_video_state_without_wifi, this, true);

        findViewById(R.id.btnPlayWithoutWifi).setOnClickListener(v -> {
            if (onPlayClickListener != null) {
                onPlayClickListener.onPlayClick(v);
            }
        });

        setVisibility(GONE);
    }

    public OnPlayClickListener getOnPlayClickListener() {
        return onPlayClickListener;
    }

    public void setOnPlayClickListener(OnPlayClickListener onPlayClickListener) {
        this.onPlayClickListener = onPlayClickListener;
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }
}
