package hiaround.android.com.downloads;

/**
 * Created by yz on 2019/2/14 4:50 PM
 * Describe: 下载监听回调
 */
public interface DownloadListener {
    void onStartDownload(long length);
    void onProgress(int progress);
    void onFail(String errorInfo);
    void onSuccess(String path);
}
