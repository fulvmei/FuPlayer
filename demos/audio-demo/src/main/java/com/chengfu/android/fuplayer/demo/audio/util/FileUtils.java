package com.chengfu.android.fuplayer.demo.audio.util;

import java.io.File;

public class FileUtils {
    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }
}
