package hiaround.android.com.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.growalong.util.util.GALogger;

public class PackageBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = PackageBroadCastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_ADDED)) {
            // 应用安装
            // 获取应用包名，和要监听的应用包名做对比
            String packName = intent.getData().getSchemeSpecificPart();
            GALogger.d(TAG,"应用安装");
        }else if (TextUtils.equals(intent.getAction(),Intent.ACTION_PACKAGE_REMOVED)){
            // 应用卸载
            // 获取应用包名
            String packName = intent.getData().getSchemeSpecificPart();
            GALogger.d(TAG,"应用卸载");
        }else if (TextUtils.equals(intent.getAction(),Intent.ACTION_PACKAGE_REPLACED)){
            // 应用覆盖
            // 获取应用包名
            String packName = intent.getData().getSchemeSpecificPart();
            GALogger.d(TAG,"应用覆盖");
        }
    }
}
