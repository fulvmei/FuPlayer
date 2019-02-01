package com.chengfu.fuexoplayer2.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.chengfu.fuexoplayer2.R;
import com.google.android.exoplayer2.Player;

public class SampleEndedView extends BaseStateView {

    public SampleEndedView(@NonNull Context context) {
        this(context, null);
    }

    public SampleEndedView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleEndedView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.sample_ended_view, this);

        findViewById(R.id.btn_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"重播点击",Toast.LENGTH_SHORT).show();
            }
        });

        setVisibility(View.GONE);
    }


    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_ENDED) {
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
