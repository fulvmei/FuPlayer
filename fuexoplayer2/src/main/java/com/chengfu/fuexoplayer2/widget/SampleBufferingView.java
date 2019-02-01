package com.chengfu.fuexoplayer2.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.chengfu.fuexoplayer2.R;
import com.google.android.exoplayer2.Player;

public class SampleBufferingView extends BaseStateView {

    public SampleBufferingView(@NonNull Context context) {
        this(context, null);
    }

    public SampleBufferingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleBufferingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.sample_buffering_view, this);

        setVisibility(View.GONE);
    }


    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        if (playWhenReady == true && playbackState == Player.STATE_BUFFERING) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
    }


    @Override
    public void detachPlayer() {
        setVisibility(View.GONE);
    }
}
