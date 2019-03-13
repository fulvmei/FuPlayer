package com.chengfu.android.fuplayer;

import com.google.android.exoplayer2.Player;

public interface PlayerController {

    /**
     * Returns the {@link Player} currently being controlled by this view, or null if no player is
     * set.
     */
    Player getPlayer();

    /**
     * Sets the {@link Player} to control.
     *
     * @param player The {@link Player} to control.
     */
    void setPlayer(Player player);

    /**
     * Returns the playback controls timeout. The playback controls are automatically hidden after
     * this duration of time has elapsed without user input.
     *
     * @return The duration in milliseconds. A non-positive value indicates that the controls will
     * remain visible indefinitely.
     */
    int getShowTimeoutMs();

    /**
     * Sets the playback controls timeout. The playback controls are automatically hidden after this
     * duration of time has elapsed without user input.
     *
     * @param showTimeoutMs The duration in milliseconds. A non-positive value will cause the controls
     *                      to remain visible indefinitely.
     */
    void setShowTimeoutMs(int showTimeoutMs);

    /**
     * Returns whether the PlayerController is currently showing.
     */
    boolean isShowing();

    /**
     * Shows the playback controls. If {@link #getShowTimeoutMs()} is positive then the controls will
     * be automatically hidden after this duration of time has elapsed without user input.
     */
    void show();

    /**
     * Hides the controller.
     */
    void hide();

}
