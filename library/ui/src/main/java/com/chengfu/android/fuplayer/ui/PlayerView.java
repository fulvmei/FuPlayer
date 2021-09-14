package com.chengfu.android.fuplayer.ui;

import androidx.annotation.Nullable;

import com.chengfu.android.fuplayer.FuPlayer;

public interface PlayerView {
    /**
     * Returns the {@link FuPlayer} currently being controlled by this view, or null if no player is
     * set.
     */
    @Nullable
    FuPlayer getPlayer();

    /**
     * Sets the {@link FuPlayer} to control.
     *
     * @param player The {@link FuPlayer} to control.
     */
    void setPlayer(@Nullable FuPlayer player);

}
