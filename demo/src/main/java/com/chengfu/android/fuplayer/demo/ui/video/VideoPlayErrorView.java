package com.chengfu.android.fuplayer.demo.ui.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chengfu.android.fuplayer.SampleErrorView;
import com.chengfu.android.fuplayer.demo.R;

public class VideoPlayErrorView extends SampleErrorView {

    public VideoPlayErrorView(@NonNull Context context) {
        super(context);
    }

    public VideoPlayErrorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayErrorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.view_video_play_error, parent, false);
    }
}
