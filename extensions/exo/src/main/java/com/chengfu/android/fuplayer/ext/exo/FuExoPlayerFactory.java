package com.chengfu.android.fuplayer.ext.exo;

import android.content.Context;
import android.support.annotation.NonNull;

import com.chengfu.android.fuplayer.core.PlayerFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;

public class FuExoPlayerFactory implements PlayerFactory {

    private Context mContext;

    public FuExoPlayerFactory(@NonNull Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ExoPlayer create() {
        return new FuExoPlayer(ExoPlayerFactory.newSimpleInstance(mContext));
    }
}
