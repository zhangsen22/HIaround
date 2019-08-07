package hiaround.android.com.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import com.growalong.util.util.GALogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import hiaround.android.com.MyApplication;

public class FileUtils {
    private static final String APKDir = "apkcache/";
    private static final String TAG = FileUtils.class.getSimpleName();
    private static final String FLITERIMAGE = "filterimage/";
    private static final String CRASH = "crash/";

    /**
     * 获取sd卡路径
     *
     * @return "/mnt/sdcard/lucky/" 或者异常时 "/data/data/com.zone.lucky/"
     */
    public static String getSDPath() {
        String sdDir = "";
        try {
            boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
            if (sdCardExist) {
                sdDir = Environment.getExternalStorageDirectory() + "/hbuilder/";// SD卡根目录
            } else {
                sdDir = "/data/data/" + MyApplication.appContext.getPackageName() + "/"; //"/data/data/hbuilder.android.com/"
            }
            File file = new File(sdDir);
            if (!file.exists()) {
                file.mkdirs();
                if (!file.exists()) {
                    sdDir = MyApplication.appContext.getFilesDir().getPath();
                    File tryfile = new File(sdDir);
                    if (!tryfile.exists()) {
                        tryfile.mkdirs();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        GALogger.d("FileUtils", "sdDir " + sdDir);
        return sdDir;
    }

    /**
     * 获取本地文件目录路径
     *
     * @param appContext
     * @return /data/data/包名/
     */
    public static String getDataPath(Context appContext) {
        String sdDir = "";
        try {
            try {
                sdDir = "/data/data/" + appContext.getPackageName() + "/";
            } catch (Exception e) {
                e.printStackTrace();
                sdDir = appContext.getFilesDir().getAbsolutePath() + "/";
            }
            File file = new File(sdDir);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sdDir;
    }

    /**
     * 获取临时目录
     *
     * @param appContext
     * @return /mnt/sdcard/lucky/temp 或 /data/data/com.zone.lucky/temp/
     */
    public static String getTempPath(Context appContext) {
        String temp = "";
        try {
            temp = getDataPath(appContext) + "temp/";
            File file = new File(temp);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        GALogger.d("FileUtils", "path " + temp);
        return temp;
    }

    /**
     * 返回APK的存放目录
     *
     * @return /lucky/apkcache/
     */
    public static String getAPKCacheDir() {

        return createDir(getSDPath() + APKDir);
    }

    /**
     * 返回二维码的存放目录
     *
     * @return /lucky/apkcache/filterimage/
     */
    public static String getFilterImageDir() {

        return createDir(getSDPath() + APKDir + FLITERIMAGE);
    }

    /**
     * 返回crash文件的存放目录
     *
     * @return /lucky/apkcache/crash/
     */
    public static String getCrashDir() {

        return createDir(getSDPath() + APKDir + CRASH);
    }

    private static String createDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dirPath;
    }

    /**
     * 配置 APP 保存图片/语音/文件/log等数据的目录
     * 这里示例用SD卡的应用扩展存储目录
     */
    public static String getAppCacheDir(Context context) {
        String storageRootPath = null;
        try {
            // SD卡应用扩展存储区(APP卸载后，该目录下被清除，用户也可以在设置界面中手动清除)，请根据APP对数据缓存的重要性及生命周期来决定是否采用此缓存目录.
            // 该存储区在API 19以上不需要写权限，即可配置 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="18"/>
            if (context.getExternalCacheDir() != null) {
                storageRootPath = context.getExternalCacheDir().getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            // SD卡应用公共存储区(APP卸载后，该目录不会被清除，下载安装APP后，缓存数据依然可以被加载。SDK默认使用此目录)，该存储区域需要写权限!
            storageRootPath = Environment.getExternalStorageDirectory() + "/" + MyApplication.appContext.getPackageName();
        }

        return storageRootPath;
    }

    //由于okhttp header 中的 value 不支持 null, \n 和 中文这样的特殊字符,所以这里
//会首先替换 \n ,然后使用 okhttp 的校验方式,校验不通过的话,就返回 encode 后的字符串
    public static String getValueEncoded(String value) {
        if (value == null) return "null";
        String newValue = value.replace("\n", "");
        for (int i = 0, length = newValue.length(); i < length; i++) {
            char c = newValue.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                try {
                    return URLEncoder.encode(newValue, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return newValue;
    }

    /**
     * 将图片转换成Base64编码的字符串
     *
     * @param path
     * @return base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    /**
     * base64编码字符集转化成图片文件。
     *
     * @param base64Str
     * @param path      文件存储路径
     * @return 是否成功
     */
    public static boolean base64ToFile(String base64Str, String path) {
        byte[] data = Base64.decode(base64Str, Base64.DEFAULT);
        for (int i = 0; i < data.length; i++) {
            if (data[i] < 0) {
                //调整异常数据
                data[i] += 256;
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            os.write(data);
            os.flush();
            os.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将bitmap保存到本地
     */
    public static boolean saveBitmap(Bitmap bitmap, String imagePath) {
        GALogger.d(TAG, "保存图片");
        File f = new File(imagePath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                out.flush();
                out.close();
                GALogger.d(TAG, "已经保存");
                return true;
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存图片到本地  并通知相册
     * @param bitmap
     * @param imagePath
     */
    public static void saveBitmapToGallery(Bitmap bitmap, String imagePath){
        boolean b = saveBitmap(bitmap, imagePath);
        if(b){
            File file = new File(imagePath);
            //通知相册更新
            MediaStore.Images.Media.insertImage(MyApplication.appContext.getContentResolver(),
                    bitmap, file.toString(), null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            MyApplication.appContext.sendBroadcast(intent);
            MyApplication.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.shortShow("已保存到本地相册");
                }
            });
        }else {
            MyApplication.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.shortShow("保存到本地相册失败");
                }
            });
        }
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param delFile 要删除的文件夹或文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String delFile) {
        File file = new File(delFile);
        if (!file.exists()) {
            GALogger.e(TAG,"删除文件失败:" + delFile + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteSingleFile(delFile);
            else
                return deleteDirectory(delFile);
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteSingleFile( String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                GALogger.e(TAG, "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                return true;
            } else {
                GALogger.e(TAG,"删除单个文件" + filePath$Name + "失败！");
                return false;
            }
        } else {
            GALogger.e(TAG,"删除单个文件失败：" + filePath$Name + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param filePath 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            GALogger.e(TAG,"删除目录失败：" + filePath + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteSingleFile(file.getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (file.isDirectory()) {
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            GALogger.e(TAG,"删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            GALogger.e(TAG, "Copy_Delete.deleteDirectory: 删除目录" + filePath + "成功！");
            return true;
        } else {
            GALogger.e(TAG,"删除目录：" + filePath + "失败！");
            return false;
        }
    }

    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

}
