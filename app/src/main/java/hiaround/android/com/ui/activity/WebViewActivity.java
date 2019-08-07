package hiaround.android.com.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.growalong.util.util.CommonTools;
import com.growalong.util.util.GALogger;
import com.growalong.util.wegit.BGAProgressBar;
import java.io.File;
import java.lang.ref.WeakReference;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseActivity;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.util.CommonFunction;
import static android.view.View.VISIBLE;

/**
 * 所有WebView的显示 (可用于显示点击底部广告Banner条打开的网页)
 */

public class WebViewActivity extends BaseActivity {
    public static final String TAG = "WebViewActivity";
    public static final String WEB_VIEW_URL = "url";
    public static final String IS_CONTAIN_GOBACK = "isContainGoback";
    public static final int REQUEST_CODE_HOME_DETAILS = 1000;
    public static final int RESULT_CODE_HOME_DETAILS = 1001;
    public static final String HOME_DETAILS_LIKE_STATUS = "HOME_DETAILS_LIKE_STATUS";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.pb_main_demo4)
    BGAProgressBar pbMainDemo4;

    private String mUrl;
    private boolean mIsCantainGoback;//是否包含多级web界面跳转

    private boolean mIgnoreSSLError = false; //忽略SSL证书错误
    private WebChromeClientImpl mWebChromeClient;
    private static int mRequestCode = 1;
    private static ValueCallback<Uri[]> valueCallBacks;
    private static ValueCallback<Uri> valueCallBack;

    private int mBackStatus;// 1-退出Activity 2-回退上个H5页面


    public static void launchVerifyCode(Context context, String url) {
        Intent i = new Intent(context, WebViewActivity.class);
        i.putExtra(WEB_VIEW_URL, url);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static void launchVerifyCode(Context context, String url, boolean isContainGoBack) {
        Intent i = new Intent(context, WebViewActivity.class);
        i.putExtra(WEB_VIEW_URL, url);
        i.putExtra(IS_CONTAIN_GOBACK, isContainGoBack);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (mIsCantainGoback) {
                    if (mBackStatus == 1) {
                        finish();
                    }
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                } else {
                    if (mBackStatus == 2) {
                        if (mWebView.canGoBack()) {
                            mWebView.goBack();
                        } else {
                            finish();
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView(View mRootView) {
        setRootViewPaddingTop(flTitleComtent);
        mUrl = getIntent().getStringExtra(WEB_VIEW_URL);
        mIsCantainGoback = getIntent().getBooleanExtra(IS_CONTAIN_GOBACK, false);
        GALogger.d(TAG, "mUrl == " + mUrl);
        if (CommonFunction.isEmptyOrNullStr(mUrl)) {
            finish();
        }
        mWebChromeClient = new WebChromeClientImpl(this);

        mWebView.requestFocus();
        mWebView.setVisibility(VISIBLE);
        mWebView.getSettings().setPluginState(PluginState.ON);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.addJavascriptInterface(this, "callMethod");// 添加接口给js，使他们可以调用我们的代码
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.setDownloadListener(new DownloadListenerImpl(this));
        mWebView.setWebViewClient(new WebViewClientImpl(this));
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mWebView.loadUrl(mUrl);
    }

    @Override
    protected void initData() {

    }

    public class WebChromeClientImpl extends WebChromeClient {
        private WeakReference<WebViewActivity> mActivity;

        public WebChromeClientImpl(WebViewActivity activity) {
            mActivity = new WeakReference<WebViewActivity>(activity);
        }

        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            WebViewActivity activity = mActivity.get();
            if (activity.isDestroyed()) {
                return;
            }
            if (newProgress == 100) {
                pbMainDemo4.setProgress(100);
                MyApplication.runOnUIThread(runnable, 200);//0.2秒后隐藏进度条
            } else if (pbMainDemo4.getVisibility() == View.INVISIBLE) {
                pbMainDemo4.setVisibility(VISIBLE);
            }
            //设置初始进度10，这样会显得效果真一点，总不能从1开始吧
            if (newProgress < 10) {
                newProgress = 10;
            }
            //不断更新进度
            pbMainDemo4.setProgress(newProgress);
            GALogger.d(TAG, "newProgress: " + newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            WebViewActivity activity = mActivity.get();
            if (activity.isDestroyed()) {
                return;
            }
            GALogger.d(TAG, "title: " + title);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            WebViewActivity activity = mActivity.get();
            valueCallBacks = filePathCallback;
            take(activity);
            return true;
        }

        public void openFileChooser(ValueCallback<Uri> filePathCallback) {
            valueCallBack = filePathCallback;
            WebViewActivity activity = mActivity.get();
            take(activity);
        }

        public void openFileChooser(ValueCallback<Uri> filePathCallback, String acceptType) {
            valueCallBack = filePathCallback;
            WebViewActivity activity = mActivity.get();
            take(activity);
        }

        public void openFileChooser(ValueCallback<Uri> filePathCallback, String acceptType, String capture) {
            valueCallBack = filePathCallback;
            WebViewActivity activity = mActivity.get();
            take(activity);
        }

        private void take(Activity mActivity) {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //show Toast
                return;
            }

            //调用系统文件管理器打开指定路径目录
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            mActivity.startActivityForResult(intent, mRequestCode);
        }

    }

    static class DownloadListenerImpl implements DownloadListener {
        private WeakReference<WebViewActivity> mActivity;

        public DownloadListenerImpl(WebViewActivity activity) {
            mActivity = new WeakReference<WebViewActivity>(activity);
        }

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            WebViewActivity activity = mActivity.get();
            if (activity.isDestroyed()) {
                return;
            }
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        }
    }

    static class WebViewClientImpl extends WebViewClient {
        private WeakReference<WebViewActivity> mActivity;

        public WebViewClientImpl(WebViewActivity activity) {
            mActivity = new WeakReference<WebViewActivity>(activity);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            GALogger.d(TAG, "shouldOverrideUrlLoading() into, url=" + url);
            WebViewActivity activity = mActivity.get();
            if (activity.isDestroyed()) {
                return true;
            }
            if (activity.mWebView == null) {
                return true;
            }
            if (url.contains("https://") || url.contains("http://")) {//拦截url 做相应操作
                activity.mWebView.loadUrl(url);
            } else {
                // 其他链接时，唤醒第三方APP
                activity.openApp(url);
                GALogger.d(TAG, "webViewActivity unknow url == " + url);
            }
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            WebViewActivity activity = mActivity.get();
            GALogger.d(TAG, "onReceivedSslError() into, url=" + activity.mUrl);
            if (activity.mIgnoreSSLError) {
                handler.proceed(); //忽略证书错误
            } else {
                super.onReceivedSslError(view, handler, error);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            GALogger.d(TAG, "onReceivedError() into, error=" + error.toString());
            WebViewActivity activity = mActivity.get();
//            activity.showLoadFailView();
//            activity.mWebView.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            GALogger.d(TAG, "onReceivedHttpError() into, errorResponse=" + errorResponse.getReasonPhrase());
            WebViewActivity activity = mActivity.get();
//            activity.showLoadFailView();
//            activity.mWebView.setVisibility(View.GONE);
        }
    }

    //判断app是否安装
    private boolean isInstall(Intent intent) {
        return MyApplication.appContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    //打开app
    private boolean openApp(String url) {
        if (TextUtils.isEmpty(url)) return false;
        try {
            if (!url.startsWith("http") || !url.startsWith("https") || !url.startsWith("ftp")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (isInstall(intent)) {
                    startActivity(intent);
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsCantainGoback) {
                if (mBackStatus == 1) {
                    return super.onKeyDown(keyCode, event);
                }
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                    return !super.onKeyDown(keyCode, event);
                }
            } else {
                if (mBackStatus == 2) {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                        return !super.onKeyDown(keyCode, event);
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * JS调取Android原生方法
     *
     * @return
     */
    @JavascriptInterface
    public void finishWebView() {
        finish();
    }


    @Override
    public void finish() {
        try {
            if (mWebView != null) {
                CommonTools.hideInputMethod(mContext, mWebView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onPause() {
        super.onPause();

        mWebView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            mWebView.stopLoading();
//            mWebView.loadData("<a></a>", "text/html", "utf-8");
            mWebView.setDownloadListener(null);
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.removeJavascriptInterface("callMethod");
            mWebView = null;

        } catch (Exception e) {
            GALogger.d(TAG, "onDestroy() error happen");
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GALogger.d(TAG, "requestCode:" + requestCode);
        if (requestCode == mRequestCode) {
            if (null == valueCallBacks && null == valueCallBack) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            String path = getPath(getApplicationContext(), result);
            Uri uri = Uri.fromFile(new File(path));
            GALogger.d(TAG, "Uri:" + uri.getPath() + " " + uri.toString());
            if (valueCallBacks != null) {
                valueCallBacks.onReceiveValue(new Uri[]{uri});
            }
            if (valueCallBack != null) {
                valueCallBack
                        .onReceiveValue(uri);
            }
            valueCallBacks = null;
            valueCallBack = null;
        } else if (requestCode == REQUEST_CODE_HOME_DETAILS && resultCode == RESULT_CODE_HOME_DETAILS) {
            if (data != null) {
                int like = data.getIntExtra(HOME_DETAILS_LIKE_STATUS, -1);
                if (like == 0) {//不喜欢
                    mWebView.loadUrl("javascript:removeItem()");
                } else if (like == 1) {//喜欢
                    mWebView.loadUrl("javascript:goChat()");
                }
            }
        }
    }


    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (uri == null) {
            return "";
        }
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }


    static class WebViewHandler extends Handler {
        private WeakReference<WebViewActivity> mActivity;

        public WebViewHandler(WebViewActivity activity) {
            mActivity = new WeakReference<WebViewActivity>(activity);
        }

        public void handleMessage(Message msg) {
            WebViewActivity activity = mActivity.get();
            if (null == activity) {
                return;
            }
            GALogger.d(TAG, "handleMessage() == " + msg.obj.toString());

        }
    }

    private Handler mHandler = new WebViewHandler(this);


    /**
     * JS调取Android原生获取当前应用版本号
     *
     * @return
     */
//    @JavascriptInterface
//    public String jsLocalInfo() {
//        return Config.APP_VERSION;
//    }

    /**
     * JS调取Android原生获取状态栏高度
     *
     * @return
     */
    @JavascriptInterface
    public int getStatusBarHeight() {

        return mStatusBarHeight;
    }

    /**
     * JS调取Android原生 谁喜欢我  跳转个人详情
     *
     * @return
     */
    @JavascriptInterface
    public void toDetailsInfo(String usidJson) {
//        UserIdBean idBean = GsonUtil.getInstance().getServerBean(usidJson, UserIdBean.class);
//        Intent intent = new Intent(WebViewActivity.this, HomeDetailsActivity.class);
//        intent.putExtra("isShowBottomLickAndUnLick", true);
//        intent.putExtra("accountId", idBean.user_id);
//        intent.putExtra("from", TAG);
//        startActivityForResult(intent, REQUEST_CODE_HOME_DETAILS);
    }

    /**
     * JS调取Android原生 相互喜欢  跳转聊天
     *
     * @return
     */
    @JavascriptInterface
    public void toChat(String usidJson) {
//        UserIdBean idBean = GsonUtil.getInstance().getServerBean(usidJson, UserIdBean.class);
//        ChatPersonActivity.start(mContext, idBean.wy_accid);
    }

    /**
     * JS调取Android原生 跳转到个人中心
     *
     * @return
     */
    @JavascriptInterface
    public void toPersonal() {
//        startActivity(new Intent(this, CenterActivity.class));
    }

    /**
     * JS调取Android原生 给我们评分
     *
     * @return
     */
    @JavascriptInterface
    public void toAppComment() {
        GALogger.d(TAG, "toAppComment into");

        try {
            Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * JS调取Android原生 版本更新
     *
     * @return
     */
    @JavascriptInterface
    public void toAppUpdate() {
    }


    /**
     * S调取Android原生 获取是否退出还是回退上个界面
     *
     * @param status 1-退出Activity 2-回退上个H5页面
     */
    @JavascriptInterface
    public void androidLikeReturn(String status) {
//        mBackStatus = status;
        GALogger.d(TAG, "androidLikeReturn into status:" + status);

        mBackStatus = Integer.parseInt(status);
    }

    /**
     * JS调取Android原生 app去升级
     *
     * @return
     */
    @JavascriptInterface
    public void toAppUpgrade() {
        GALogger.d(TAG, "toAppUpgrade into");
//        if (!DownloadUtils.getInstance().getIsDownloading()) {
//            DownloadUtils.getInstance().initDownload(null, false);
//            DownloadUtils.getInstance().download();
//        }

    }

    /**
     * JS调取Android原生 匹配成功页面
     *
     * @return
     */
    @JavascriptInterface
    public void matchingSucceed(String usidJson) {
//        UserIdBean idBean = GsonUtil.getInstance().getServerBean(usidJson, UserIdBean.class);
//        Intent intent = new Intent(this, MatchSuccessActivity.class);
//        intent.putExtra("slide_user_id", idBean.user_id);
//        startActivity(intent);
    }

    /**
     *刷新界面（此处为加载完成后进度消失）
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            pbMainDemo4.setVisibility(View.INVISIBLE);
        }
    };
}