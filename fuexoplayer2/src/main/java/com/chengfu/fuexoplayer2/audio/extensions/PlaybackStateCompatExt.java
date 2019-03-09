package com.chengfu.fuexoplayer2.audio.extensions;

import android.support.v4.media.session.PlaybackStateCompat;


public class PlaybackStateCompatExt {

    /**
     * Useful extension methods for [PlaybackStateCompat].
     */
    public static boolean isPrepared(PlaybackStateCompat playbackStateCompat) {
        int state = playbackStateCompat.getState();
        return (state == PlaybackStateCompat.STATE_BUFFERING) ||
                (state == PlaybackStateCompat.STATE_PLAYING) ||
                (state == PlaybackStateCompat.STATE_PAUSED);
    }


    public static boolean isPlaying(PlaybackStateCompat playbackStateCompat) {
        int state = playbackStateCompat.getState();
        return (state == PlaybackStateCompat.STATE_BUFFERING) ||
                (state == PlaybackStateCompat.STATE_PLAYING);
    }

    public static boolean isPlayEnabled(PlaybackStateCompat playbackStateCompat) {
        return true;
    }

    public static boolean isPauseEnabled(PlaybackStateCompat playbackStateCompat) {
        return true;
    }

    public static boolean isSkipToNextEnabled(PlaybackStateCompat playbackStateCompat) {
        return true;
    }

    public static boolean isSkipToPreviousEnabled(PlaybackStateCompat playbackStateCompat) {
        return true;
    }

    public static String stateName(PlaybackStateCompat playbackStateCompat) {
        int state = playbackStateCompat.getState();
        switch (state) {
            case PlaybackStateCompat.STATE_NONE:
                return "STATE_NONE";
            case PlaybackStateCompat.STATE_STOPPED:
                return "STATE_STOPPED";
            case PlaybackStateCompat.STATE_PAUSED:
                return "STATE_PAUSED";
            case PlaybackStateCompat.STATE_PLAYING:
                return "STATE_PLAYING";
            case PlaybackStateCompat.STATE_FAST_FORWARDING:
                return "STATE_FAST_FORWARDING";
            case PlaybackStateCompat.STATE_REWINDING:
                return "STATE_REWINDING";
            case PlaybackStateCompat.STATE_BUFFERING:
                return "STATE_BUFFERING";
            case PlaybackStateCompat.STATE_ERROR:
                return "STATE_ERROR";
            default:
                return "UNKNOWN_STATE";
        }
    }
}
