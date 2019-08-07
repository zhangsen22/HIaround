/*
 * Copyright (C) 2016-2016 SJY.JIANGSU Corporation. All rights reserved
 */

package com.growalong.util.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by gangqing on 2016/7/21.
 */
public class BitmapUtils {
    public static File saveBitmapFile(Bitmap bitmap) {
        File cacheFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + System.currentTimeMillis() + ".jpg");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cacheFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cacheFile;
    }

    public static boolean savePic(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取屏幕长和高
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int width = view.getWidth();
        int height = view.getHeight();
        // 去掉标题栏
        Bitmap b = Bitmap.createBitmap(b1, 0, 0, width, height);
        view.destroyDrawingCache();
        String strPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + System.currentTimeMillis() + ".jpg";
        File file = new File(strPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;

        Bitmap bitmap = Bitmap.createBitmap(w,h,config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);


        return bitmap;
    }
    /**
     * 压缩图片
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
//        int degree = readPictureDegree(filePath);
//        bm = rotateBitmap(bm,degree) ;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    public static Bitmap CropImageByPrecent(Bitmap bitmap, int width, int height) {
        Bitmap b = null;
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = (height * w) / width;
        try {
            b = Bitmap.createBitmap(bitmap, 0, h / 2, w, wh, null, false);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
            System.gc();
            try {
                b = Bitmap.createBitmap(bitmap, 0, h / 2, w, wh, null, false);
            } catch (OutOfMemoryError error1) {
                error1.printStackTrace();
            }
        }
        if (b == null) {
            b = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);
        }
        return b;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    public static byte[] bmpToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);//把bitmap100%高质量压缩 到 output对象里
        //微信分享的图标不能超过32kb，这里使用30kb,以优化打开速度
        int option = (30 * 1024 * 100) / output.toByteArray().length;
        if (option < 100) {
            output.reset();// 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, option, output);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        bitmap.recycle();
        byte[] result = output.toByteArray();//转换成功了
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap compressImage(Bitmap bitmap) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);//把bitmap100%高质量压缩 到 output对象里
        //微信分享的图标不能超过32kb，这里使用30kb,以优化打开速度
        int option = (30 * 1024 * 100) / output.toByteArray().length;
        if (option < 100) {
            output.reset();// 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, option, output);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(output.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        bitmap = BitmapFactory.decodeStream(isBm, null, null);
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 25f;

    public static Bitmap GaussianBlur(Context context, Bitmap image) {
        // 计算图片缩小后的长宽
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);
        // 将缩小后的图片做为预渲染的图片。
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // 创建一张渲染后的输出图片。
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(BLUR_RADIUS);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);
        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public static Bitmap rotateBitmapByDegree(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        try {
            int degree = getBitmapDegree(path);
            return rotateBitmapByDegree(bitmap, degree);
        } catch (Exception e) {
            return bitmap;
        }
    }

    public static int[] getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth, options.outHeight};
    }

    /**
     * 谷歌推荐使用方法，从资源中加载图像，并高效压缩，有效降低OOM的概率
     * @param res 资源
     * @param resId 图像资源的资源id
     * @param reqWidth 要求图像压缩后的宽度
     * @param reqHeight 要求图像压缩后的高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // 设置inJustDecodeBounds = true ,表示获取图像信息，但是不将图像的像素加入内存
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // 调用方法计算合适的 inSampleSize
        options.inSampleSize = calculateInSampleSizes(options, reqWidth,reqHeight);

        // inJustDecodeBounds 置为 false 真正开始加载图片
        options.inJustDecodeBounds = false;
        //将options.inPreferredConfig改成Bitmap.Config.RGB_565，
        // 是默认情况Bitmap.Config.ARGB_8888占用内存的一般
        options.inPreferredConfig= Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    // 计算 BitmapFactpry 的 inSimpleSize的值的方法
    public static int calculateInSampleSizes(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }

        // 获取图片原生的宽和高
        final int height = options.outHeight;
        final int width = options.outWidth;
        GALogger.d(TAG, "origin, w= " + width + " h=" + height);
        int inSampleSize = 1;

        // 如果原生的宽高大于请求的宽高,那么将原生的宽和高都置为原来的一半
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 主要计算逻辑
            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        GALogger.d(TAG, "sampleSize:" + inSampleSize);
        return inSampleSize;
    }

    /**
     * bitmap转base64
     *
     * @param @param  bitmap
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: bitmapToBase64
     * @Description: TODO(Bitmap 转换为字符串)
     */
    @SuppressLint("NewApi")
    public static String bitmapToBase64(Bitmap bitmap) {

        // 要返回的字符串
        String reslut = null;

        ByteArrayOutputStream baos = null;

        try {

            if (bitmap != null) {

                baos = new ByteArrayOutputStream();
                /**
                 * 压缩只对保存有效果bitmap还是原来的大小
                 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);

                baos.flush();
                baos.close();
                // 转换为字节数组
                byte[] byteArray = baos.toByteArray();

                // 转换为字符串
                reslut = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return reslut;

    }
    /**
     * base64转bitmap
     *
     * @param @param  base64String
     * @param @return 设定文件
     * @return Bitmap    返回类型
     * @throws
     * @Title: base64ToBitmap
     * @Description: TODO(base64l转换为Bitmap)
     */
    public static Bitmap base64ToBitmap(String base64String) {

        byte[] decode = Base64.decode(base64String, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);

        return bitmap;
    }

    /**
     * 获取一个ImageView上的图片的bitmap
     * @param imageView
     * @return
     */
    public static Bitmap getImageView(ImageView imageView){
        return ((BitmapDrawable) (imageView).getDrawable()).getBitmap();
    }

}
