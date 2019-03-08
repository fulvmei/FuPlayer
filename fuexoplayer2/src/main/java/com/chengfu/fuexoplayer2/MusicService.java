package com.chengfu.fuexoplayer2;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.chengfu.fuexoplayer2.audio.PackageValidator;
import com.chengfu.fuexoplayer2.audio.library.MusicSource;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends MediaBrowserServiceCompat {

    public static final String TAG = "MusicService";

    private MediaSessionCompat mediaSession;
    private MediaControllerCompat mediaController;
    private BecomingNoisyReceiver becomingNoisyReceiver;
    private NotificationManagerCompat notificationManager;
    private NotificationBuilder notificationBuilder;
    private MusicSource mediaSource;
    private MediaSessionConnector mediaSessionConnector;
    private PackageValidator packageValidator;

    @Override
    public void onCreate() {
        super.onCreate();

        FuLog.d(TAG, "onCreate");

        // Build a PendingIntent that can be used to launch the UI.
        Intent sessionIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        PendingIntent sessionActivityPendingIntent = PendingIntent.getActivity(this, 0, sessionIntent, 0);

        // Create a new MediaSession.
        mediaSession = new MediaSessionCompat(this, TAG);
        mediaSession.setSessionActivity(sessionActivityPendingIntent);
        mediaSession.setActive(true);

        /**
         * In order for [MediaBrowserCompat.ConnectionCallback.onConnected] to be called,
         * a [MediaSessionCompat.Token] needs to be set on the [MediaBrowserServiceCompat].
         *
         * It is possible to wait to set the session token, if required for a specific use-case.
         * However, the token *must* be set by the time [MediaBrowserServiceCompat.onGetRoot]
         * returns, or the connection will fail silently. (The system will not even call
         * [MediaBrowserCompat.ConnectionCallback.onConnectionFailed].)
         */
        setSessionToken(mediaSession.getSessionToken());

        // Because ExoPlayer will manage the MediaSession, add the service as a callback for
        // state changes.
        mediaController = new MediaControllerCompat(this, mediaSession);
        mediaController.registerCallback(new MediaControllerCallback());

        notificationBuilder = new NotificationBuilder(this);
        notificationManager = NotificationManagerCompat.from(this);

        try {
            becomingNoisyReceiver =
                    new BecomingNoisyReceiver(this, mediaSession.getSessionToken());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

//        mediaSource = JsonSource(context = this, source = remoteJsonSource);

        // ExoPlayer will manage the MediaSession for us.
        mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setQueueNavigator(new UampQueueNavigator(mediaSession));

//        // ExoPlayer will manage the MediaSession for us.
//        mediaSessionConnector = new MediaSessionConnector(mediaSession).also {
//            // Produces DataSource instances through which media data is loaded.
//            val dataSourceFactory = DefaultDataSourceFactory(
//                    this, Util.getUserAgent(this, UAMP_USER_AGENT), null)
//
//            // Create the PlaybackPreparer of the media session connector.
//            val playbackPreparer = UampPlaybackPreparer(
//                    mediaSource,
//                    exoPlayer,
//                    dataSourceFactory)
//
//            it.setPlayer(exoPlayer, playbackPreparer)
//            it.setQueueNavigator(UampQueueNavigator(mediaSession))
//        }
//
//        packageValidator = new PackageValidator(this, R.xml.allowed_media_browser_callers);
    }

    /**
     * This is the code that causes UAMP to stop playing when swiping it away from recents.
     * The choice to do this is app specific. Some apps stop playback, while others allow playback
     * to continue and allow uses to stop it with the notification.
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        FuLog.d(TAG, "onTaskRemoved");
        /**
         * By stopping playback, the player will transition to [Player.STATE_IDLE]. This will
         * cause a state change in the MediaSession, and (most importantly) call
         * [MediaControllerCallback.onPlaybackStateChanged]. Because the playback state will
         * be reported as [PlaybackStateCompat.STATE_NONE], the service will first remove
         * itself as a foreground service, and will then call [stopSelf].
         */
//        exoPlayer.stop(true)
    }

    @Override
    public void onDestroy() {
        FuLog.d(TAG, "onDestroy");
//        mediaSession.setActive(false);
//        mediaSession.release();
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        FuLog.d(TAG, "onGetRoot : clientPackageName=" + clientPackageName + ",clientUid=" + clientUid + ",rootHints=" + rootHints.get("321"));

        Bundle bundle = new Bundle();
        bundle.putString("123", "哈哈哈哈哈");
        return new BrowserRoot("adsada", bundle);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        FuLog.d(TAG, "onLoadChildren : parentId=" + parentId + ",result=" + result);

//        MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
//                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, "http://mvoice.spriteapp.cn/voice/2016/0703/5778246106dab.mp3")
//                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "一辈子有多少的来不及发现已失去最重要的东西 . （精神节奏）")
//                .build();
//
//        ArrayList<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
//
//        MediaBrowserCompat.MediaItem item = new MediaBrowserCompat.MediaItem(
//                metadata.getDescription(),
//                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
//        );
//
//        mediaItems.add(item);
//
//        //向Browser发送数据
        result.sendResult(new ArrayList<>());


//        // If the media source is ready, the results will be set synchronously here.
//        val resultsSent = mediaSource.whenReady {
//            successfullyInitialized ->
//            if (successfullyInitialized) {
//                val children = browseTree[parentMediaId] ?.map {
//                    item ->
//                            MediaItem(item.description, item.flag)
//                }
//                result.sendResult(children)
//            } else {
//                result.sendError(null)
//            }
//        }
//
//        // If the results are not ready, the service must "detach" the results before
//        // the method returns. After the source is ready, the lambda above will run,
//        // and the caller will be notified that the results are ready.
//        //
//        // See [MediaItemFragmentViewModel.subscriptionCallback] for how this is passed to the
//        // UI/displayed in the [RecyclerView].
//        if (!resultsSent) {
//            result.detach()
//        }
    }

    /**
     * Removes the [NOW_PLAYING_NOTIFICATION] notification.
     * <p>
     * Since `stopForeground(false)` was already called (see
     * [MediaControllerCallback.onPlaybackStateChanged], it's possible to cancel the notification
     * with `notificationManager.cancel(NOW_PLAYING_NOTIFICATION)` if minSdkVersion is >=
     * [Build.VERSION_CODES.LOLLIPOP].
     * <p>
     * Prior to [Build.VERSION_CODES.LOLLIPOP], notifications associated with a foreground
     * service remained marked as "ongoing" even after calling [Service.stopForeground],
     * and cannot be cancelled normally.
     * <p>
     * Fortunately, it's possible to simply call [Service.stopForeground] a second time, this
     * time with `true`. This won't change anything about the service's state, but will simply
     * remove the notification.
     */
    private void removeNowPlayingNotification() {
        stopForeground(true);
    }


    private class MediaControllerCallback extends MediaControllerCompat.Callback {

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            updateNotification(mediaController.getPlaybackState());
        }

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            updateNotification(state);
        }

        private void updateNotification(PlaybackStateCompat state) {

            int updatedState = state.getState();

            if (mediaController.getMetadata() == null) {
                return;
            }

            // Skip building a notification when state is "none".
            Object notification = null;
            if (updatedState != PlaybackStateCompat.STATE_NONE) {
                notification = notificationBuilder.buildNotification(mediaSession.getSessionToken());
            }

            switch (updatedState) {

            }
        }
    }

    private class UampQueueNavigator extends TimelineQueueNavigator {
        private Timeline.Window window = new Timeline.Window();

        public UampQueueNavigator(MediaSessionCompat mediaSession) {
            super(mediaSession);
        }

        @Override
        public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
            return (MediaDescriptionCompat) player.getCurrentTimeline().getWindow(windowIndex, window, true).tag;
        }
    }

    /**
     * Helper class for listening for when headphones are unplugged (or the audio
     * will otherwise cause playback to become "noisy").
     */
    private class BecomingNoisyReceiver extends BroadcastReceiver {
        private Context context;
        private MediaSessionCompat.Token sessionToken;
        private IntentFilter noisyIntentFilter;
        private MediaControllerCompat controller;
        private boolean registered = false;

        public BecomingNoisyReceiver(Context context,
                                     MediaSessionCompat.Token sessionToken) throws RemoteException {
            this.context = context;
            this.sessionToken = sessionToken;

            noisyIntentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
            controller = new MediaControllerCompat(context, sessionToken);
        }

        public void register() {
            if (!registered) {
                context.registerReceiver(this, noisyIntentFilter);
                registered = true;
            }
        }

        public void unregister() {
            if (registered) {
                context.unregisterReceiver(this);
                registered = false;
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                controller.getTransportControls().pause();
            }
        }
    }
}
