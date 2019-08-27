package hiaround.android.com.downloads;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.growalong.util.util.AppPublicUtils;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.PackageUtil;
import com.liulishuo.filedownloader.FileDownloader;

import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.observer.NetChangeObserver;
import hiaround.android.com.observer.NetworkChangedReceiver;
import hiaround.android.com.util.CommonFunction;

/**
 * 描述：构建apk 升级类
 * 作者：zhuangzeqin
 * 时间: 2017/9/6-14:24
 * 邮箱：zzq@eeepay.cn
 */
public class UpdateAppBuilder {
    private final String TAG = "UpdateAppBuilder";
    private boolean isForce = false; //是否强制更新 默认不强制升级
    private Activity mActivity;//上下文对象
    private String mServerVersionName = "";//服务器的版本名称
    private String mDownloadUrl = "";//apk 下载链接
    private String mUpdateInfo = "";//apk 升级信息
    private int mDownloadid = 0;//下载id
    private boolean isWifiRequired = false;//是否wifi 请求下载

    private  CustomDialog dialog;

    private UpdateAppBuilder(Activity activity) {
        this.mActivity = activity;
        //开启广播去监听 网络 改变事件
        NetworkChangedReceiver.getReceiverInstance().removeRegisterObserver(mNetChangeObserver);
        NetworkChangedReceiver.getReceiverInstance().addObserver(mNetChangeObserver);
    }

    public static UpdateAppBuilder with(Activity activity) {
        return new UpdateAppBuilder(activity);
    }

    /**
     * 设置下载的url
     *
     * @param downloadUrl
     * @return
     */
    public UpdateAppBuilder apkPath(String downloadUrl) {
        this.mDownloadUrl = downloadUrl;
        return this;
    }

    public UpdateAppBuilder isWifiRequired(boolean isWifiRequired) {
        this.isWifiRequired = isWifiRequired;
        return this;
    }

    /**
     * 设置APK 的升级信息
     *
     * @param updateInfo
     * @return
     */
    public UpdateAppBuilder updateInfo(String updateInfo) {
        this.mUpdateInfo = updateInfo;
        return this;
    }

    /**
     * 设置服务器版本号
     *
     * @param serverVersionName
     * @return
     */
    public UpdateAppBuilder serverVersionName(String serverVersionName) {
        this.mServerVersionName = serverVersionName;
        return this;
    }

    /**
     * 是否强制升级
     *
     * @param isForce true 为强制升级； false 不强制升级
     * @return
     */
    public UpdateAppBuilder isForce(boolean isForce) {
        this.isForce = isForce;
        return this;
    }

    public void start() {
        // 先判断是否要升级；
        // 在判断如果是； 在判断是否强制升级
        // 显示下载进度条
        try {
            MyApplication.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    int i = AppPublicUtils.compareVersion(PackageUtil.getAppVersionName(MyApplication.appContext), mServerVersionName);
                    GALogger.d(TAG,"   i   "+i);
                    if(i == -1){
                        Log.d(TAG, "服务器上的版本比本地版本号大；需要升级");
                        doNewVersionUpdate(mActivity);
                    } else {
                        //否则 服务器没有放最新版本；无需升级
                        Log.d(TAG, " 服务器没有放最新版本；无需升级");
                    }
                }
            }, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 网络观察者
     */
    protected final NetChangeObserver mNetChangeObserver = new NetChangeObserver() {
        @Override
        public void onNetConnected(CommonFunction.NetType type) {//网络连接
            if (type == CommonFunction.NetType.WIFI) {
                closeDialog();
                //wifi网络已连接
                mDownloadid = DownloadManager.
                        with(mActivity).
                        downloadUrl(mDownloadUrl).isWifiRequired(isWifiRequired).
                        isForce(isForce).startDownload();
            } else if (type == CommonFunction.NetType.TYPE_MOBILE) {
                //移动网络已连接
                showTiPDialog(mActivity);
            }
        }

        @Override
        public void onNetDisConnect() {//网络已经断开
            FileDownloader.getImpl().pause(mDownloadid);//暂停一下
            closeDialog();
        }
    };

    private void closeDialog() {
        if (dialog!=null)
        {
            dialog.cancel();
        }
    }


    /**
     * 有更新
     *
     * @param mContext postion 1选择更新，2强制更新（一个按钮）
     */
    private void doNewVersionUpdate(final Activity mContext) {
        CustomDialog dialog = new CustomDialog(mContext);
        dialog.setTitles("版本更新").setMessage(mUpdateInfo);
        dialog.setPositiveButton("马上更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开始下载
                mDownloadid = DownloadManager.
                        with(mActivity).
                        downloadUrl(mDownloadUrl).isWifiRequired(isWifiRequired).
                        isForce(isForce).startDownload();
            }
        });
        if (isForce) {//强制升级
            dialog.setCancelable_(false);
        } else {//无需强制升级
            dialog.setNegativeButton("稍后再说", null);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 温馨提示语
     * @param mContext
     */
    private void showTiPDialog(final Activity mContext) {
       //不知道为什么；网络监听会回调2次？
        closeDialog();
        dialog = new CustomDialog(mContext);
        dialog.setTitles("版本更新").setMessage(mContext.getString(R.string.tipmsg));
        dialog.setPositiveButton(mContext.getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开始下载
                mDownloadid = DownloadManager.
                        with(mActivity).
                        downloadUrl(mDownloadUrl).isWifiRequired(isWifiRequired).
                        isForce(isForce).startDownload();
            }
        });
        if (isForce) {//强制升级
            dialog.setCancelable(false);
        } else {//无需强制升级
            dialog.setNegativeButton(mContext.getString(R.string.cancel), null);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
