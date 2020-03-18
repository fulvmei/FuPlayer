package com.chengfu.android.fuplayer.ui;

public interface ProgressAdapter extends PlayerView{

    boolean isCurrentWindowSeekable();

    boolean isCurrentWindowDynamic();

    long getCurrentPosition();

    long getDuration();

    long getBufferedPosition();

    int getBufferedPercentage();

    CharSequence getPositionText(long position);

}
