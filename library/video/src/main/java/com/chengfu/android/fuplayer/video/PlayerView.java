package com.chengfu.android.fuplayer.video;

import com.google.android.exoplayer2.ExoPlayer;

public interface PlayerView {
    /**
     * Returns the {@link ExoPlayer} currently being controlled by this view, or null if no player is
     * set.
     */
    ExoPlayer getPlayer();

    /**
     * Sets the {@link ExoPlayer} to control.
     *
     * @param player The {@link ExoPlayer} to control.
     */
    void setPlayer(ExoPlayer player);

}
