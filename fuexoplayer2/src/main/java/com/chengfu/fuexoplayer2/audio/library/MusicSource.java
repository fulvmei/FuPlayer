package com.chengfu.fuexoplayer2.audio.library;

import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;

import java.util.List;


public interface MusicSource extends Iterable<MediaMetadataCompat> {

    /**
     * State indicating the source was created, but no initialization has performed.
     */
    int STATE_CREATED = 1;

    /**
     * State indicating initialization of the source is in progress.
     */
    int STATE_INITIALIZING = 2;

    /**
     * State indicating the source has been initialized and is ready to be used.
     */
    int STATE_INITIALIZED = 3;

    /**
     * State indicating an error has occurred.
     */
    int STATE_ERROR = 4;

    Boolean whenReady();

    List<MediaMetadataCompat> search(String query, Bundle extras);

}
