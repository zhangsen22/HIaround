package aimi.android.com;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.PackageUtil;
import aimi.android.com.crash.CrashHandler;
import aimi.android.com.net.retrofit.ApiConstants;
import aimi.android.com.observer.NetworkChangedReceiver;

public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();
    public static volatile Handler applicationHandler;
    public static volatile Context appContext;
    public static volatile String hostAddress;
    public static volatile String h5_down_Address;//h5域名和apk下载地址域名

    @Override
    public void onCreate() {
        super.onCreate();
        GALogger.d(TAG,"onCreate");
        setHostAddress(ApiConstants.getGetDomainNameBase);
        appContext = getApplicationContext();
        // 获取当前进程名
        final String processName = PackageUtil.getProcessName(android.os.Process.myPid());
        // 获取当前包名
        final String packageName = appContext.getPackageName();
        applicationHandler = new Handler(appContext.getMainLooper());
        if (processName != null && !processName.equals(packageName)) {
            return;
        }

        /*开启网络广播监听*/
        NetworkChangedReceiver.getReceiverInstance().registerNetworkStateReceiver(appContext);
        //crash崩溃本地捕捉
        CrashHandler.getInstance().init(appContext);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //低内存的时候回收掉
        NetworkChangedReceiver.getReceiverInstance().unRegisterNetworkStateReceiver(appContext);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            applicationHandler.post(runnable);
        } else {
            applicationHandler.postDelayed(runnable, delay);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static String getHostAddress() {
        return hostAddress;
    }

    public static void setHostAddress(String hostAddress) {
        MyApplication.hostAddress = hostAddress;
    }

    public static String getH5_down_Address() {
        return h5_down_Address;
    }

    public static void setH5_down_Address(String h5_down_Address) {
        MyApplication.h5_down_Address = h5_down_Address;
    }
}
