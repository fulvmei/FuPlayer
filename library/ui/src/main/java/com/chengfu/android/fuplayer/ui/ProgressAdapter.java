package com.chengfu.android.fuplayer.ui;

public interface ProgressAdapter extends PlayerView{

    boolean isCurrentWindowSeekable();

    boolean isCurrentWindowDynamic();

    boolean isCurrentWindowLive();

    long getCurrentPosition();

    long getDuration();

    long getBufferedPosition();

    int getBufferedPercentage();

    boolean showSeekView();

    boolean showPositionViewView();

    boolean showDurationView();

    CharSequence getPositionText(long position);

}
