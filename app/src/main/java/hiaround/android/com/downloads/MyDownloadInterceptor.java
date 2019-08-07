package hiaround.android.com.downloads;

import com.growalong.util.util.GALogger;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * 下载文件拦截器封装
 */
public class MyDownloadInterceptor implements Interceptor {
    private static final String TAG = "MyDownloadInterceptor";
    private DownloadListener mListener;

    public MyDownloadInterceptor(DownloadListener listener) {
        mListener = listener;
    }


    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        String url = request.url().toString();
        GALogger.d(TAG, "request url==" + url);

        okhttp3.Response response = chain.proceed(request);

        return response.newBuilder().body(new DownloadResponseBody(response.body(), mListener)).build();


    }
}

