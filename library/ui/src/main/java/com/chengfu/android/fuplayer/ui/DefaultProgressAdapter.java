package com.chengfu.android.fuplayer.ui;

import com.chengfu.android.fuplayer.FuPlayer;

import java.util.Formatter;
import java.util.Locale;

public class DefaultProgressAdapter implements ProgressAdapter {

    private FuPlayer player;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;


    public DefaultProgressAdapter() {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    @Override
    public FuPlayer getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(FuPlayer player) {
        this.player = player;
    }

    @Override
    public boolean isCurrentWindowSeekable() {
        return player != null && player.isCurrentWindowSeekable();
    }

    @Override
    public boolean isCurrentWindowDynamic() {
        return player != null && player.isCurrentWindowDynamic();
    }

    @Override
    public long getCurrentPosition() {
        return player != null ? player.getCurrentPosition() : 0;
    }

    @Override
    public long getDuration() {
        return player != null ? player.getDuration() : 0;
    }


    @Override
    public long getBufferedPosition() {
        return player != null ? player.getBufferedPosition() : 0;
    }

    @Override
    public int getBufferedPercentage() {
        return player != null ? player.getBufferedPercentage() : 0;
    }

    @Override
    public CharSequence getPositionText(long position) {
        if (position <= 0) {
            position = 0;
        }

        long totalSeconds = (position + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
