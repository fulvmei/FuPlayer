package com.chengfu.android.fuplayer.demo.audio.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.media.MediaDescriptionCompat;

import com.chengfu.android.fuplayer.demo.audio.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MusicUtil {
    private static final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");

    public static List<MediaDescriptionCompat> getMusicList(Context context) {

        List<MediaDescriptionCompat> musics = new ArrayList<>();
        Cursor c = null;
        try {
            c = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));// 路径
                if (!FileUtils.isExists(path)) {
                    continue;
                }
                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)); // 歌曲名
                String album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)); // 专辑
                int album_id = c.getInt(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));// 专辑ID
                String artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)); // 作者
                long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));// 大小
                int duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));// 时长
                int id = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));// 歌曲的id
                Bitmap bitmap = getArtwork(context, id, album_id,true,false);

                int isMusic = c.getInt(c
                        .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // 是否为音乐
                if (isMusic != 0 && duration > 32000) {

                    MediaDescriptionCompat music = new MediaDescriptionCompat.Builder()
                            .setDescription(album)
                            .setExtras(null)
                            .setIconBitmap(bitmap)
                            .setIconUri(null)
                            .setMediaId(id + "")
                            .setMediaUri(Uri.parse(path))
                            .setSubtitle(artist)
                            .setTitle(name)
                            .build();
                    musics.add(music);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return musics;
    }

    private static Bitmap getAlbumArt(Context context, int album_id) {
        System.out.println("11111 album_id=" + album_id);
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)), projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        Bitmap bm = null;
        if (album_art != null) {
            bm = BitmapFactory.decodeFile(album_art);
        } else {
            bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_art);
        }
        return bm;
    }

    /**
     * 从文件当中获取专辑封面位图
     *
     * @param context
     * @param songid
     * @param albumid
     * @return
     */
    private static Bitmap getArtworkFromFile(Context context, long songid, long albumid) {
        Bitmap bm = null;
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            FileDescriptor fd = null;
            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/"
                        + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            } else {
                Uri uri = ContentUris.withAppendedId(albumArtUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            }
            options.inSampleSize = 1;
            // 只进行大小判断
            options.inJustDecodeBounds = true;
            // 调用此方法得到options得到图片大小
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            // 我们的目标是在800pixel的画面上显示
            // 所以需要调用computeSampleSize得到图片缩放的比例
            options.inSampleSize = 100;
            // 我们得到了缩放的比例，现在开始正式读入Bitmap数据
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            //根据options参数，减少所需要的内存
            bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bm;
    }

    public static Bitmap getArtwork(Context context, long song_id, long album_id, boolean allowdefalut, boolean small){
        if(album_id < 0) {
            if(song_id < 0) {
                Bitmap bm = getArtworkFromFile(context, song_id, -1);
                if(bm != null) {
                    return bm;
                }
            }
            if(allowdefalut) {
                return getDefaultArtwork(context);
            }
            return null;
        }
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(albumArtUri, album_id);
        if(uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                //先制定原始大小
                options.inSampleSize = 1;
                //只进行大小判断
                options.inJustDecodeBounds = true;
                //调用此方法得到options得到图片的大小
                BitmapFactory.decodeStream(in, null, options);
                /** 我们的目标是在你N pixel的画面上显示。 所以需要调用computeSampleSize得到图片缩放的比例 **/
                /** 这里的target为800是根据默认专辑图片大小决定的，800只是测试数字但是试验后发现完美的结合 **/
                if(small){
                    options.inSampleSize = computeSampleSize(options, 40);
                } else{
                    options.inSampleSize = computeSampleSize(options, 600);
                }
                // 我们得到了缩放比例，现在开始正式读入Bitmap数据
                options.inJustDecodeBounds = false;
                options.inDither = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                in = res.openInputStream(uri);
                return BitmapFactory.decodeStream(in, null, options);
            } catch (FileNotFoundException e) {
                Bitmap bm = getArtworkFromFile(context, song_id, album_id);
                if(bm != null) {
                    if(bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        if(bm == null && allowdefalut) {
                            return getDefaultArtwork(context);
                        }
                    }
                } else if(allowdefalut) {
                    bm = getDefaultArtwork(context);
                }
                return bm;
            } finally {
                try {
                    if(in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Bitmap getDefaultArtwork(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.default_art);
    }

    public static int computeSampleSize(BitmapFactory.Options options, int target) {
        int w = options.outWidth;
        int h = options.outHeight;
        int candidateW = w / target;
        int candidateH = h / target;
        int candidate = Math.max(candidateW, candidateH);
        if(candidate == 0) {
            return 1;
        }
        if(candidate > 1) {
            if((w > target) && (w / candidate) < target) {
                candidate -= 1;
            }
        }
        if(candidate > 1) {
            if((h > target) && (h / candidate) < target) {
                candidate -= 1;
            }
        }
        return candidate;
    }


    public static List<MediaDescriptionCompat> getNetMusicList() {
        List<MediaDescriptionCompat> list = new ArrayList<>();

        list.add(new MediaDescriptionCompat.Builder()
                .setMediaId("1")
                .setTitle("3D潮音 - 3D环绕嗨曲")
                .setSubtitle("未知来源")
                .setMediaUri(Uri.parse("http://mvoice.spriteapp.cn/voice/2016/0517/573b1240d0118.mp3"))
                .setIconUri(Uri.parse("http://mpic.spriteapp.cn/crop/566x360/picture/2016/0517/573b1240af3da.jpg"))
                .build());


        list.add(new MediaDescriptionCompat.Builder()
                .setMediaId("2")
                .setTitle("电音House   耳机福利")
                .setSubtitle("未知来源")
                .setMediaUri(Uri.parse("http://mvoice.spriteapp.cn/voice/2016/1108/5821463c8ea94.mp3"))
                .setIconUri(Uri.parse("http://mpic.spriteapp.cn/crop/566x360/picture/2016/1108/5821463c3fad8.jpg"))
                .build());

        list.add(new MediaDescriptionCompat.Builder()
                .setMediaId("3")
                .setSubtitle("未知来源")
                .setTitle("爱过的人我已不再拥有，错过的人是否可回首 . （治愈女声）")
                .setMediaUri(Uri.parse("http://mvoice.spriteapp.cn/voice/2016/1104/581b63392f6cb.mp3"))
                .setIconUri(Uri.parse("http://mpic.spriteapp.cn/crop/566x360/picture/2016/1104/581b633864635.jpg"))
                .build());

        list.add(new MediaDescriptionCompat.Builder()
                .setMediaId("4")
                .setSubtitle("未知来源")
                .setTitle("感觉很放松，我最喜欢在我的兰博基尼上听这首歌，先不说，...")
                .setMediaUri(Uri.parse("http://mvoice.spriteapp.cn/voice/2016/1123/5834c6bc02059.mp3"))
                .setIconUri(Uri.parse("http://mpic.spriteapp.cn/crop/566x360/picture/2016/1123/5834c6bbdcce7.jpg"))
                .build());

        list.add(new MediaDescriptionCompat.Builder()
                .setMediaId("5")
                .setSubtitle("未知来源")
                .setSubtitle("未知")
                .setTitle("一辈子有多少的来不及发现已失去最重要的东西 . （精神节奏）")
                .setMediaUri(Uri.parse("http://mvoice.spriteapp.cn/voice/2016/0703/5778246106dab.mp3"))
                .setIconUri(Uri.parse("http://mpic.spriteapp.cn/crop/566x360/picture/2016/0703/57782460908e4.jpg"))
                .build());

        list.add(new MediaDescriptionCompat.Builder()
                .setMediaId("6")
                .setSubtitle("未知来源")
                .setTitle("陪你度过漫长岁月。（达尔文）")
                .setMediaUri(Uri.parse("http://mvoice.spriteapp.cn/voice/2017/0515/591969966204f.mp3"))
                .setIconUri(Uri.parse("http://mpic.spriteapp.cn/crop/566x360/picture/2017/0515/5919699622800.jpg"))
                .build());

        list.add(new MediaDescriptionCompat.Builder()
                .setMediaId("7")
                .setSubtitle("未知来源")
                .setTitle("应广大百友要求！要我媳妇把整首《天空之城》清唱完毕！在...")
                .setMediaUri(Uri.parse("http://mvoice.spriteapp.cn/voice/2016/0423/571ac24dab840.mp3"))
                .setIconUri(Uri.parse("http://mpic.spriteapp.cn/crop/566x360/picture/2016/0423/571ac24da061b.jpg"))
                .build());

        list.add(new MediaDescriptionCompat.Builder()
                .setMediaId("8")
                .setSubtitle("未知来源")
                .setTitle("我是肌无力患者，由于力气不足唱的不好，我真心送给天下母...")
                .setMediaUri(Uri.parse("http://mvoice.spriteapp.cn/voice/2017/0108/5871ba43667c6.mp3"))
                .setIconUri(Uri.parse("http://mpic.spriteapp.cn/crop/566x360/picture/2017/0108/5871ba41e0681.jpg"))
                .build());

        return list;
    }

}