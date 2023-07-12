package com.chengfu.android.fuplayer.ui;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.Player;

public interface PlayerHolder {
    /**
     * Returns the {@link Player} currently being controlled by this view, or null if no player is
     * set.
     */
    @Nullable
    Player getPlayer();

    /**
     * Sets the {@link Player} to control.
     *
     * @param player The {@link Player} to control.
     */
    void setPlayer(@Nullable Player player);

}
