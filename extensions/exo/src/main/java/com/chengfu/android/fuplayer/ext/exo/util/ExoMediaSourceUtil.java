package com.chengfu.android.fuplayer.ext.exo.util;

import android.net.Uri;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.Util;

public class ExoMediaSourceUtil {

    public static MediaSourceFactory buildMediaSourceFactory(Uri uri, @Nullable String overrideExtension, DataSource.Factory dataSourceFactory) {
        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
        switch (type) {
            case C. CONTENT_TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory);
            case C. CONTENT_TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory);
            case C. CONTENT_TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory);
            case C. CONTENT_TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(dataSourceFactory);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    public static MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension, DataSource.Factory dataSourceFactory) {
        return buildMediaSource(uri, overrideExtension, dataSourceFactory, null);
    }

    public static MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension, DataSource.Factory dataSourceFactory, Object tag) {
        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
        switch (type) {
            case C. CONTENT_TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(uri));
            case C. CONTENT_TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(uri));
            case C. CONTENT_TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(uri));
            case C. CONTENT_TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(uri));
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }
}
