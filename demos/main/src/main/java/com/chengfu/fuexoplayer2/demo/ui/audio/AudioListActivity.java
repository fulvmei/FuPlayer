package com.chengfu.fuexoplayer2.demo.ui.audio;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chengfu.fuexoplayer2.demo.R;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;

import java.util.Arrays;
import java.util.List;

public class AudioListActivity extends AppCompatActivity {

    private final static String TAG = "MediaSessionTest";

    private final static String TABS[] = {"推荐", "古典", "DJ", "情歌"};

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_list);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), Arrays.asList(TABS));

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        test();
    }

    private void test() {
        MediaSessionCompat mMediaSession = new MediaSessionCompat(this, TAG);
        //指明支持的按键信息类型
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );
        mMediaSession.setActive(true);

        mMediaSession.setPlaybackState(
                new PlaybackStateCompat.Builder()
                        .setActions(PlaybackStateCompat.ACTION_STOP)
                        .setState(PlaybackStateCompat.STATE_NONE, 0, 1)
                        .build());

        MediaControllerCompat controller = mMediaSession.getController();

        controller.getTransportControls().play();

        MediaMetadataCompat.Builder metaData = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "Title")
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Artist")
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "Album")
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, "Artist")
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0);

        mMediaSession.setMetadata(metaData.build());

        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onCommand(String command, Bundle extras, ResultReceiver cb) {
                super.onCommand(command, extras, cb);
                Log.d(TAG, "onCommand");
            }

            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
                Log.d(TAG, "onMediaButtonEvent");
                return super.onMediaButtonEvent(mediaButtonEvent);
            }

            @Override
            public void onPrepare() {
                super.onPrepare();
                Log.d(TAG, "onPrepare");
            }

            @Override
            public void onPrepareFromMediaId(String mediaId, Bundle extras) {
                super.onPrepareFromMediaId(mediaId, extras);
                Log.d(TAG, "onPrepareFromMediaId");
            }

            @Override
            public void onPrepareFromSearch(String query, Bundle extras) {
                super.onPrepareFromSearch(query, extras);
                Log.d(TAG, "onPrepareFromSearch");
            }

            @Override
            public void onPrepareFromUri(Uri uri, Bundle extras) {
                super.onPrepareFromUri(uri, extras);
                Log.d(TAG, "onPrepareFromUri");
            }

            @Override
            public void onPlay() {
                super.onPlay();
                Log.d(TAG, "onPlay");
            }

            @Override
            public void onPlayFromMediaId(String mediaId, Bundle extras) {
                super.onPlayFromMediaId(mediaId, extras);
                Log.d(TAG, "onPlayFromMediaId");
            }

            @Override
            public void onPlayFromSearch(String query, Bundle extras) {
                super.onPlayFromSearch(query, extras);
                Log.d(TAG, "onPlayFromSearch");
            }

            @Override
            public void onPlayFromUri(Uri uri, Bundle extras) {
                super.onPlayFromUri(uri, extras);
                Log.d(TAG, "onPlayFromUri");
            }

            @Override
            public void onSkipToQueueItem(long id) {
                super.onSkipToQueueItem(id);
                Log.d(TAG, "onSkipToQueueItem");
            }

            @Override
            public void onPause() {
                super.onPause();
                Log.d(TAG, "onPause");
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                Log.d(TAG, "onSkipToNext");
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                Log.d(TAG, "onSkipToPrevious");
            }

            @Override
            public void onFastForward() {
                super.onFastForward();
                Log.d(TAG, "onFastForward");
            }

            @Override
            public void onRewind() {
                super.onRewind();
                Log.d(TAG, "onRewind");
            }

            @Override
            public void onStop() {
                super.onStop();
                Log.d(TAG, "onStop");
            }

            @Override
            public void onSeekTo(long pos) {
                super.onSeekTo(pos);
                Log.d(TAG, "onSeekTo");
            }

            @Override
            public void onSetRating(RatingCompat rating) {
                super.onSetRating(rating);
                Log.d(TAG, "onSetRating");
            }

            @Override
            public void onSetRating(RatingCompat rating, Bundle extras) {
                super.onSetRating(rating, extras);
                Log.d(TAG, "onSetRating");
            }

            @Override
            public void onSetCaptioningEnabled(boolean enabled) {
                super.onSetCaptioningEnabled(enabled);
                Log.d(TAG, "onSetCaptioningEnabled");
            }

            @Override
            public void onSetRepeatMode(int repeatMode) {
                super.onSetRepeatMode(repeatMode);
                Log.d(TAG, "onSetRepeatMode");
            }

            @Override
            public void onSetShuffleMode(int shuffleMode) {
                super.onSetShuffleMode(shuffleMode);
                Log.d(TAG, "onSetShuffleMode");
            }

            @Override
            public void onCustomAction(String action, Bundle extras) {
                super.onCustomAction(action, extras);
                Log.d(TAG, "onCustomAction");
            }

            @Override
            public void onAddQueueItem(MediaDescriptionCompat description) {
                super.onAddQueueItem(description);
                Log.d(TAG, "onAddQueueItem");
            }

            @Override
            public void onAddQueueItem(MediaDescriptionCompat description, int index) {
                super.onAddQueueItem(description, index);
                Log.d(TAG, "onAddQueueItem");
            }

            @Override
            public void onRemoveQueueItem(MediaDescriptionCompat description) {
                super.onRemoveQueueItem(description);
                Log.d(TAG, "onRemoveQueueItem");
            }
        });
        mMediaSession.setActive(true);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        List<String> tabs;

        public SectionsPagerAdapter(FragmentManager fm, List<String> tabs) {
            super(fm);
            this.tabs = tabs;
        }

        @Override
        public Fragment getItem(int position) {
            String tab = tabs.get(position);
            return AudioListFragment.newInstance(tab);
        }

        @Override
        public int getCount() {
            if (tabs != null) {
                return tabs.size();
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }
    }
}
