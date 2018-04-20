package com.daixu.jandan.utils;

import android.graphics.BitmapFactory;

/**
 * Created by 32422 on 2018/4/20.
 */

public class BitmapUtil {

    /**
     * 创建一个图片处理Options
     *
     * @return {@link android.graphics.BitmapFactory.Options}
     */
    static BitmapFactory.Options createOptions() {
        return new BitmapFactory.Options();
    }

    /**
     * 获取图片的真实后缀
     *
     * @param filePath 图片存储地址
     * @return 图片类型后缀
     */
    public static String getExtension(String filePath) {
        BitmapFactory.Options options = createOptions();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        String mimeType = options.outMimeType;
        return mimeType.substring(mimeType.lastIndexOf("/") + 1);
    }
}
