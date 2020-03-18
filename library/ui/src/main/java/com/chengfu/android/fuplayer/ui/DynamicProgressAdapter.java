package com.chengfu.android.fuplayer.ui;

import com.chengfu.android.fuplayer.FuPlayer;

import java.util.Formatter;
import java.util.Locale;

public class DynamicProgressAdapter implements ProgressAdapter {

    protected long currentPosition;
    protected long duration;

    protected StringBuilder mFormatBuilder;
    protected Formatter mFormatter;

    public DynamicProgressAdapter() {
        init();
    }

    public DynamicProgressAdapter(long currentPosition, long duration) {
        init();
        this.currentPosition = currentPosition;
        this.duration = duration;
    }

    private void init() {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    public void setCurrentPosition(long currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public boolean isCurrentWindowSeekable() {
        return false;
    }

    @Override
    public boolean isCurrentWindowDynamic() {
        return false;
    }

    @Override
    public long getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public long getBufferedPosition() {
        return currentPosition + 1000;

    }

    @Override
    public int getBufferedPercentage() {
        return (int) (getBufferedPosition() / duration + 0.0001);
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

    @Override
    public FuPlayer getPlayer() {
        return null;
    }

    @Override
    public void setPlayer(FuPlayer player) {

    }
}
