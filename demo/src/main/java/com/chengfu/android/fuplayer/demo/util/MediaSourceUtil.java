package com.chengfu.android.fuplayer.demo.util;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
//import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MediaSourceUtil {

    public static MediaSource getMediaSource(Context context, String path, int contentType) {




        Uri uri = Uri.parse(path);

//        int contentType = Util.inferContentType(uri);
        DefaultDataSourceFactory dataSourceFactory =
                new DefaultDataSourceFactory(context,
                        Util.getUserAgent(context, context.getPackageName()), new DefaultBandwidthMeter());


//        String scheme = uri.getScheme();
//        if (scheme != null && scheme.contains("rtmp")) {
//            return new ExtractorMediaSource(uri, new RtmpDataSourceFactory(), new DefaultExtractorsFactory(), null, null);
//        }

        switch (contentType) {
//            case C.TYPE_DASH:
//                DefaultDashChunkSource.Factory factory = new DefaultDashChunkSource.Factory(dataSourceFactory);
//                return new DashMediaSource(uri, dataSourceFactory, factory, null, null);
            case C.TYPE_SS:
                DefaultSsChunkSource.Factory ssFactory = new DefaultSsChunkSource.Factory(dataSourceFactory);
                return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
//                return new HlsMediaSource(uri, dataSourceFactory, null, null);
            case C.TYPE_OTHER:
            default:
                // This is the MediaSource representing the media to be played.
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                return null;
//                return new ExtractorMediaSource(uri,
//                        dataSourceFactory, extractorsFactory, null, null);
        }
    }
}
