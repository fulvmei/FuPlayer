package com.chengfu.android.fuplayer.ext.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chengfu.android.fuplayer.SampleBufferingView;

public class VideoBufferingView extends SampleBufferingView {

    ProgressBar progressBar;
    boolean fullScreen;

    public VideoBufferingView(@NonNull Context context) {
        this(context, null);
    }

    public VideoBufferingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoBufferingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        progressBar = findViewById(R.id.progressBar);

        updateProgressBarView();
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.fpu_view_video_buffering, parent, false);
    }

    public void setFullScreen(boolean fullScreen) {
        if (this.fullScreen == fullScreen) {
            return;
        }
        this.fullScreen = fullScreen;

        updateProgressBarView();
    }


    protected void updateProgressBarView() {
        if (progressBar == null) {
            return;
        }
        if (fullScreen) {
            progressBar.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
            progressBar.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
        } else {
            progressBar.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
            progressBar.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
        }
    }
}
