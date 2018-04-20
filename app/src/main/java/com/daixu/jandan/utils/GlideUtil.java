package com.daixu.jandan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.daixu.jandan.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class GlideUtil {
    private static ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("").build();
    private static ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<Runnable>(1024), nameThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    private static class ThreadFactoryBuilder implements ThreadFactory {

        private String mNameFormat;

        private ThreadFactoryBuilder() {
        }

        private ThreadFactoryBuilder setNameFormat(String nameFormat) {
            this.mNameFormat = nameFormat;
            return this;
        }

        public ThreadFactoryBuilder build() {
            return this;
        }

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, mNameFormat + "-Thread-");
        }
    }

    public static void load(Context context, @Nullable Object url, final ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .placeholder(R.mipmap.icon_default_picture)
                .into(imageView);
    }

    public static void loadBitmap(final Context context, @Nullable Object url, final ImageView imageView) {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .placeholder(R.mipmap.icon_default_picture)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                        imageView.requestLayout();

                        Timber.tag("loadPicture").d("原图 宽 " + resource.getWidth() + ",高" + resource.getHeight() + "图片宽" + imageView.getWidth());
                        if (resource.getHeight() >= 1000) {
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                    }
                });
    }

    public static void loadPicture(Context context, @Nullable Object url, final ImageView imageView) {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .placeholder(R.mipmap.icon_default_picture)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                        imageView.requestLayout();
                        Timber.tag("loadPicture").d("原图 宽 " + resource.getWidth() + ",高" + resource.getHeight() + "图片宽" + imageView.getWidth());
                        int height = resource.getHeight();
                        if (height > 2000) {
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1000));
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        } else {
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        }
                    }
                });
    }

    public static void loadPictureGif(Context context, @Nullable Object url, final ImageView imageView) {
        GlideApp.with(context)
                .asGif()
                //.dontAnimate()
                .load(url)
                .into(imageView);
    }

    public static void saveToFile(final Context context, final @Nullable Object url, final View view) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, R.string.gallery_save_file_not_have_external_storage, Toast.LENGTH_SHORT).show();
            return;
        }

        // In this save max image size is source
        final Future<File> future = GlideApp.with(context)
                .load(url)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File sourceFile = future.get();
                    if (sourceFile == null || !sourceFile.exists()) {
                        return;
                    }
                    String extension = BitmapUtil.getExtension(sourceFile.getAbsolutePath());
                    String extDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Pictures" + File.separator + "Jandan" + File.separator;
                    File extDirFile = new File(extDir);
                    if (!extDirFile.exists()) {
                        if (!extDirFile.mkdirs()) {
                            // If mk dir error
                            callSaveStatus(context, false, null, view);
                            return;
                        }
                    }
                    final File saveFile = new File(extDirFile, String.format("IMG_%s.%s", System.currentTimeMillis(), extension));
                    FileUtils.copyFile(sourceFile, saveFile);
                    callSaveStatus(context, true, saveFile, view);
                } catch (Exception e) {
                    e.printStackTrace();
                    callSaveStatus(context, false, null, view);
                }
            }
        });
    }

    private static void callSaveStatus(final Context context, final boolean success, final File savePath, final View view) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    // notify
                    Uri uri = Uri.fromFile(savePath);
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    Snackbar.make(view, "图片已保存至相册Jandaj", Snackbar.LENGTH_LONG)
                            .setAction("查看", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    System.out.println("-------------查看-------------");
                                }
                            }).show();
                } else {
                    Toast.makeText(context, R.string.gallery_save_file_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //拿到图片缩放后的高度
    private static float getBitmapHeight(Bitmap bitmap, int width) {
        float bitmapWidth = bitmap.getWidth();
        float bitmapHeight = bitmap.getHeight();

        if (bitmapWidth > 0 && bitmapHeight > 0) {
            //拿到宽度的缩放值
            float scaleWidth = width / bitmapWidth;
            //拿到高度根据宽度缩放后的高度
            return bitmapHeight * scaleWidth;
        }
        return 0;
    }

}
