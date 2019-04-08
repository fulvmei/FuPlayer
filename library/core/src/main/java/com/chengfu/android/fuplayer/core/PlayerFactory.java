package com.chengfu.android.fuplayer.core;

import android.support.annotation.NonNull;

import com.google.android.exoplayer2.ExoPlayer;

public interface PlayerFactory {

    @NonNull
    ExoPlayer create();
}
