package com.chengfu.android.fuplayer;

import android.os.Looper;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.PlayerMessage;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;

public interface FuPlayer extends Player {

    /**
     * Returns the {@link Looper} associated with the playback thread.
     */
    Looper getPlaybackLooper();

    /**
     * Retries a failed or stopped playback. Does nothing if the player has been reset, or if playback
     * has not failed or been stopped.
     */
    void retry();

    /**
     * Prepares the player to play the provided {@link MediaSource}. Equivalent to
     * {@code prepare(mediaSource, true, true)}.
     * <p>
     * Note: {@link MediaSource} instances are not designed to be re-used. If you want to prepare a
     * player more than once with the same piece of media, use a new instance each time.
     */
    void prepare(MediaSource mediaSource);

    /**
     * Prepares the player to play the provided {@link MediaSource}, optionally resetting the playback
     * position the default position in the first {@link Timeline.Window}.
     * <p>
     * Note: {@link MediaSource} instances are not designed to be re-used. If you want to prepare a
     * player more than once with the same piece of media, use a new instance each time.
     *
     * @param mediaSource   The {@link MediaSource} to play.
     * @param resetPosition Whether the playback position should be reset to the default position in
     *                      the first {@link Timeline.Window}. If false, playback will start from the position defined
     *                      by {@link #getCurrentWindowIndex()} and {@link #getCurrentPosition()}.
     * @param resetState    Whether the timeline, manifest, tracks and track selections should be reset.
     *                      Should be true unless the player is being prepared to play the same media as it was playing
     *                      previously (e.g. if playback failed and is being retried).
     */
    void prepare(MediaSource mediaSource, boolean resetPosition, boolean resetState);

    /**
     * Creates a message that can be sent to a {@link PlayerMessage.Target}. By default, the message
     * will be delivered immediately without blocking on the playback thread. The default {@link
     * PlayerMessage#getType()} is 0 and the default {@link PlayerMessage#getPayload()} is null. If a
     * position is specified with {@link PlayerMessage#setPosition(long)}, the message will be
     * delivered at this position in the current window defined by {@link #getCurrentWindowIndex()}.
     * Alternatively, the message can be sent at a specific window using {@link
     * PlayerMessage#setPosition(int, long)}.
     */
    PlayerMessage createMessage(PlayerMessage.Target target);

    /**
     * Sets the parameters that control how seek operations are performed.
     *
     * @param seekParameters The seek parameters, or {@code null} to use the defaults.
     */
    void setSeekParameters(@Nullable SeekParameters seekParameters);

    /**
     * Returns the currently active {@link SeekParameters} of the player.
     */
    SeekParameters getSeekParameters();
}
