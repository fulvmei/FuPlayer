package com.chengfu.android.fuplayer;

import android.support.annotation.NonNull;

public interface PlayerFactory {

    @NonNull
    FuPlayer create();
}
