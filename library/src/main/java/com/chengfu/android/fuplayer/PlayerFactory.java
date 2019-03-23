package com.chengfu.android.fuplayer;

import android.support.annotation.NonNull;

import com.google.android.exoplayer2.ExoPlayer;

public interface PlayerFactory {

    @NonNull
    ExoPlayer create();
}
