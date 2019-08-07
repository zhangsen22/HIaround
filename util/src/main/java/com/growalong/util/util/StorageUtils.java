package com.growalong.util.util;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by gangqing on 2017/4/27.
 */

public class StorageUtils {
    /**
     * 获取sd剩余存储空间
     *
     * @return
     */
    public static long getSDAvailableSize() {
        try {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getAbsolutePath());
            long blockSize;
            long availableBlocks;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.getBlockSizeLong();
                availableBlocks = stat.getAvailableBlocksLong();
            } else {
                blockSize = stat.getBlockSize();
                availableBlocks = stat.getAvailableBlocks();
            }
            return availableBlocks * blockSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 计算总空间
     *
     * @return
     */
    public static long getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getAbsolutePath());
        long blockSize;
        long totalBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            totalBlocks = stat.getBlockCountLong();
        } else {
            blockSize = stat.getBlockSize();
            totalBlocks = stat.getBlockCount();
        }
        return blockSize * totalBlocks;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(long size) {
        BigDecimal result;

        long kb = size / 1024;
        if (kb < 1) {
            return size + "B";
        }

        long mb = kb / 1024;
        if (mb < 1) {
            result = new BigDecimal(Double.toString(kb));
            return result.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gb = mb / 1024d;
        if (gb < 1) {
            result = new BigDecimal(Double.toString(mb));
            return result.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        result = new BigDecimal(Double.toString(gb));
        return result.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
    }
}
