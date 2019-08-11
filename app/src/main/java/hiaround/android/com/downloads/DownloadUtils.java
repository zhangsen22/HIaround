package hiaround.android.com.downloads;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import com.growalong.util.util.GALogger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.AppManager;
import hiaround.android.com.app.Constants;
import hiaround.android.com.net.retrofit.ModelResultObserver;
import hiaround.android.com.net.retrofit.service.ApiServices;
import hiaround.android.com.util.FileUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by yz on 2019/2/14 5:25 PM
 * Describe: 下载管理类
 */
public class DownloadUtils {
    private static final String TAG = "DownloadUtils";
    private static DownloadUtils sInstance;
    private static final int MSG_START = 0;
    private static final int MSG_PROGRESS = 1;
    private static final int MSG_FAIL = 2;

    private static final int DEFAULT_TIMEOUT = 15;
    private Retrofit retrofit;
    private DownloadListener listener;
    private boolean mIsDownloading = false;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private long fileSize;
    private final int NotificationProgressID = 111123;
    private PendingIntent pendingIntent;

    private ProgressDialog progressDialog;
    private boolean isFocusUpdate = false;//是否强制升级  true是  false否

    private DownloadUtils() {

    }

    public static DownloadUtils getInstance() {
        if (sInstance == null) {
            synchronized (DownloadUtils.class) {
                if (sInstance == null) {
                    sInstance = new DownloadUtils();
                }
            }
        }
        return sInstance;
    }

    public void initDownload(DownloadListener downloadListener, boolean isFocusUpdate) {
        listener = downloadListener;
        this.isFocusUpdate = isFocusUpdate;
        if (listener == null) {
            setListener();
        }
        MyDownloadInterceptor mInterceptor = new MyDownloadInterceptor(listener);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.getH5_down_Address())
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    /**
     * 判断是否正在下载中
     *
     * @return
     */
    public boolean getIsDownloading() {
        return mIsDownloading;
    }

    /**
     * 开始下载
     *
     */
    public void download() {
        final String filePath = FileUtils.getAPKCacheDir()+ "Gmix.apk";
        mIsDownloading = true;
        retrofit.create(ApiServices.class)
                .download()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, InputStream>() {

                    @Override
                    public InputStream apply(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation()) // 用于计算任务
                .doOnNext(new Consumer<InputStream>() {
                    @Override
                    public void accept(InputStream inputStream) {
                        writeFile(inputStream, filePath);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<InputStream>() {
                    @Override
                    public void onSuccess(InputStream result) {


                        GALogger.d(TAG, "下载完后，立刻安装-------------------onPostExecute" + "  result:" + filePath);
                        if (listener != null) {
                            listener.onSuccess(filePath);
                        }
                        mIsDownloading = false;

                        Intent i = new Intent();
                        i.setAction("android.intent.action.VIEW");
                        i.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判读版本是否在7.0以上
                            Uri apkUri = FileProvider.getUriForFile(MyApplication.appContext, MyApplication.appContext.getPackageName()
                                    + ".fileprovider", new File(filePath));
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            i.setDataAndType(apkUri, "application/vnd.android.package-archive");

                        } else {
                            i.setDataAndType(Uri.parse("file://" + filePath),
                                    "application/vnd.android.package-archive");
                        }
                        MyApplication.appContext.startActivity(i);

                    }
                });

    }

    /**
     * 将输入流写入文件
     *
     * @param inputString
     * @param filePath
     */
    private void writeFile(InputStream inputString, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        BufferedInputStream in=null;
        BufferedOutputStream out=null;
        try {
            in=new BufferedInputStream(inputString);
            out=new BufferedOutputStream(new FileOutputStream(file));
            int len=-1;
            byte[] b=new byte[1024];
            while((len=in.read(b))!=-1){
                out.write(b,0,len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            mIsDownloading = false;
            if (listener != null) {
                listener.onFail("FileNotFoundException");
            }
        } catch (IOException e) {
            mIsDownloading = false;
            if (listener != null) {
                listener.onFail("IOException");
            }
        }
    }

    private void setListener() {
        listener = new DownloadListener() {
            @Override
            public void onStartDownload(long length) {

                Message message = Message.obtain();
                message.what = MSG_START;
                mHandler.sendMessage(message);


                GALogger.d(TAG, "length: " + length);
                fileSize = length;

            }

            @Override
            public void onProgress(int progress) {
                GALogger.d(TAG,"progress   "+progress);
                int pro = (int) ((double) progress / fileSize * 100);

                Message message = Message.obtain();
                message.what = MSG_PROGRESS;
                message.arg1 = pro;
                mHandler.sendMessage(message);

                if(notificationBuilder == null || notificationManager == null){
                    showNotification();

                }
                notificationBuilder.setContentText(pro + " %");
                notificationBuilder.setProgress(100, pro, false);
                Notification notification = notificationBuilder.build();
                notification.contentIntent = pendingIntent;
                notificationManager.notify(NotificationProgressID, notification);

            }

            @Override
            public void onFail(String errorInfo) {
                GALogger.d(TAG, "errorInfo: " + errorInfo);
                Message message = Message.obtain();
                message.what = MSG_FAIL;
                mHandler.sendMessage(message);

            }

            @Override
            public void onSuccess(String path) {
                GALogger.d(TAG, "onSuccess: " + path);
                notificationManager.cancel(NotificationProgressID);
                if (progressDialog != null) {
                    progressDialog.cancel();
                    progressDialog = null;
                }
                sInstance = null;
                mHandler = null;
            }
        };
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START:
                    showNotification();

                    showProgressDialog();
                    break;
                case MSG_PROGRESS:
                    int pro = msg.arg1;

                    if (progressDialog != null) {
                        progressDialog.setProgress(pro);
                    }
                    break;
                case MSG_FAIL:
                    notificationManager.cancel(NotificationProgressID);
                    if (progressDialog != null) {
                        progressDialog.cancel();
                        progressDialog = null;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };


    private void showNotification() {
        String id = Constants.NOTIFICATION_CHANNEL_DOWNLOAD;
        String name = MyApplication.appContext.getString(R.string.download_progress_channel_message);
        notificationManager = (NotificationManager) MyApplication.appContext.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            GALogger.d(TAG, mChannel.toString());
            mChannel.setSound(null, null);
            notificationManager.createNotificationChannel(mChannel);
            notificationBuilder = new NotificationCompat.Builder(MyApplication.appContext, id);
            notification = notificationBuilder
                    .setContentTitle(MyApplication.appContext.getString(R.string.download_now))
                    .setContentText("0 %")
                    .setProgress(100, 0, false)
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher).build();

        } else {
            notificationBuilder = new NotificationCompat.Builder(MyApplication.appContext)
                    .setContentTitle(MyApplication.appContext.getString(R.string.download_now))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("0 %")
                    .setProgress(100, 0, false)
                    .setOngoing(true);
            notification = notificationBuilder.build();
        }
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(MyApplication.appContext, AppManager.getInstance().currentActivity().getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        pendingIntent = PendingIntent.getActivity(MyApplication.appContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = pendingIntent;
        notificationManager.notify(NotificationProgressID, notification);
    }

    private void showProgressDialog() {
        if (progressDialog == null && isFocusUpdate) {
            progressDialog = new ProgressDialog(AppManager.getInstance().currentActivity());
            progressDialog.setProgressNumberFormat(null);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }
    }
}
