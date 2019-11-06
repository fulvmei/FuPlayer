package com.chengfu.android.fuplayer.ext.exo;

import android.content.Context;
import androidx.annotation.NonNull;

import com.chengfu.android.fuplayer.FuPlayer;
import com.chengfu.android.fuplayer.PlayerFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;

public class FuExoPlayerFactory implements PlayerFactory {

    private Context mContext;

    public FuExoPlayerFactory(@NonNull Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public FuPlayer create() {
        return new FuExoPlayer(ExoPlayerFactory.newSimpleInstance(mContext));
    }
}
