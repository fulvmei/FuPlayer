package com.chengfu.fuexoplayer2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;

/**
 * Helper class to encapsulate code for building notifications.
 */
public class NotificationBuilder {

//    private final Context context;
//    private final NotificationCompat.Action skipToPreviousAction;
//    private final NotificationCompat.Action playAction;
//    private final NotificationCompat.Action pauseAction;
//    private final NotificationCompat.Action skipToNextAction;
//    private final PendingIntent stopPendingIntent;

    public NotificationBuilder(Context context) {
//        this.context = context;
//
//        skipToPreviousAction = new NotificationCompat.Action(
//                R.drawable.exo_controls_previous,
//                context.getString(R.string.notification_skip_to_previous),
//                MediaButtonReceiver.buildMediaButtonPendingIntent(context, ACTION_SKIP_TO_PREVIOUS));
//        playAction = new NotificationCompat.Action(
//                R.drawable.exo_controls_play,
//                context.getString(R.string.notification_play),
//                MediaButtonReceiver.buildMediaButtonPendingIntent(context, ACTION_PLAY));
//        pauseAction = new NotificationCompat.Action(
//                R.drawable.exo_controls_pause,
//                context.getString(R.string.notification_pause),
//                MediaButtonReceiver.buildMediaButtonPendingIntent(context, ACTION_PAUSE));
//        skipToNextAction = new NotificationCompat.Action(
//                R.drawable.exo_controls_next,
//                context.getString(R.string.notification_skip_to_next),
//                MediaButtonReceiver.buildMediaButtonPendingIntent(context, ACTION_SKIP_TO_NEXT));
//        stopPendingIntent = MediaButtonReceiver.buildMediaButtonPendingIntent(context, ACTION_STOP);
    }

    public Notification buildNotification(MediaSessionCompat.Token sessionToken) {
        return null;
    }

    private boolean shouldCreateNowPlayingChannel() {


        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !nowPlayingChannelExists();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private boolean nowPlayingChannelExists() {
//        platformNotificationManager.getNotificationChannel(NOW_PLAYING_CHANNEL) != null;
        return false;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNowPlayingChannel() {
//        NotificationChannel notificationChannel = new NotificationChannel(NOW_PLAYING_CHANNEL,
//                context.getString(R.string.notification_channel),
//                NotificationManager.IMPORTANCE_LOW);
//        notificationChannel.setDescription(context.getString(R.string.notification_channel_description));
//        platformNotificationManager.createNotificationChannel(notificationChannel);
    }
}
