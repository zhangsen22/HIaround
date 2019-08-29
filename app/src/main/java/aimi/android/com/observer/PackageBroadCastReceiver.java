package aimi.android.com.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.growalong.util.util.AppPublicUtils;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

import aimi.android.com.util.FileUtils;

public class PackageBroadCastReceiver extends BroadcastReceiver {
    private final String TAG  = PackageBroadCastReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
//        PackageManager manager = context.getPackageManager();
        final String DEFAULTSAVEPATH  = FileUtils.getAPKCacheDir();//默认的保存路径;
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d(TAG,"安装成功"+packageName);
            //安装完成之后；删除下载文件； 和清空db里的数据
            AppPublicUtils.deleteFile(new File(DEFAULTSAVEPATH));
            FileDownloader.getImpl().clearAllTaskData();
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d(TAG,"卸载成功"+packageName);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d(TAG,"替换成功"+packageName);
        }
    }
}
