package hiaround.android.com.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hiaround.android.com.util.CommonFunction;

public class NetworkChangedReceiver extends BroadcastReceiver {
    private final static String TAG = NetworkChangedReceiver.class.getSimpleName();
    public final static String CUSTOM_ANDROID_NET_CHANGE_ACTION = "com.eeepay.cn.api.netstatus.CONNECTIVITY_CHANGE";
    private final static String ANDROID_NET_CHANGE_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;//"android.net.conn.CONNECTIVITY_CHANGE";

    private volatile boolean isNetAvailable = false;
    private  CommonFunction.NetType mNetType;
    private List<NetChangeObserver> mNetChangeObservers;

    private static NetworkChangedReceiver mBroadcastReceiver = null;

    private NetworkChangedReceiver() {

    }

    /**
     * 获取 BroadcastReceiver 实例
     *
     * @return
     */
    public static NetworkChangedReceiver getReceiverInstance() {
        if (null == mBroadcastReceiver) {
            synchronized (NetworkChangedReceiver.class) {
                if (null == mBroadcastReceiver) {
                    mBroadcastReceiver = new NetworkChangedReceiver();
                }
            }
        }
        return mBroadcastReceiver;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        //添加网络监听的变化
        netWorkChangeListenering(context, intent);
    }


    private void netWorkChangeListenering(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION) || intent.getAction().equalsIgnoreCase(CUSTOM_ANDROID_NET_CHANGE_ACTION)) {
            if (mNetType == CommonFunction.getAPNType(context))
                return;// 如果回调回来跟上次的一样；直接就return了； 避免有些情况回调2次网络监听
            boolean networkAvailable = CommonFunction.isNetworkConnected(context);
            Log.e(TAG, "networkAvailable=" + networkAvailable);
            if (!networkAvailable) {
                Log.e(TAG, "<--- network disconnected --->");
                isNetAvailable = networkAvailable;
                mNetType = CommonFunction.getAPNType(context);
            } else {
                Log.e(TAG, "<--- network connected --->");
                isNetAvailable = networkAvailable;
                mNetType = CommonFunction.getAPNType(context);
                //判断wifi是否连接并且网络有效  WIFI环境下下载礼物
            }
            notifyObserver();//发出通知
        }
    }

    private void notifyObserver() {
        if(mNetChangeObservers != null) {
            if (!mNetChangeObservers.isEmpty()) {
                int size = mNetChangeObservers.size();
                for (int i = 0; i < size; i++) {
                    NetChangeObserver observer = mNetChangeObservers.get(i);
                    if (observer != null) {
                        if (isNetworkAvailable()) {
                            observer.onNetConnected(mNetType);
                        } else {
                            observer.onNetDisConnect();
                        }
                    }
                }
            }
        }
    }

    public boolean isNetworkAvailable() {
        return isNetAvailable;
    }

    public CommonFunction.NetType getAPNType() {
        return mNetType;
    }

    /**
     * 添加网络监听
     *
     * @param observer
     */
    public void addObserver(NetChangeObserver observer) {
        if (mNetChangeObservers == null) {
            mNetChangeObservers = new ArrayList<NetChangeObserver>();
        }
        if (mNetChangeObservers.contains(observer)) {
            mNetChangeObservers.remove(observer);
        }
        mNetChangeObservers.add(observer);
    }

    /**
     * 移除网络监听
     *
     * @param observer
     */
    public void removeRegisterObserver(NetChangeObserver observer) {
        if (mNetChangeObservers != null) {
            if (mNetChangeObservers.contains(observer)) {
                mNetChangeObservers.remove(observer);
            }
        }
    }

    /**
     * 注册
     *
     * @param context
     */
    public void registerNetworkStateReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        context.getApplicationContext().registerReceiver(getReceiverInstance(), filter);
    }

    /**
     * 反注册
     *
     * @param context
     */
    public void unRegisterNetworkStateReceiver(Context context) {
        if (mBroadcastReceiver != null) {
            try {
                context.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e) {

            }
        }
    }

}
