package com.paulniu.media_sum;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Coder: niupuyue
 * Date: 2019/8/26
 * Time: 17:18
 * Desc:
 * Version:
 */
public class FileUtils {

    public static String getVideoSavePath() {
        File sdDir;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }
        return null;
    }

}
