package aimi.android.com.net.retrofit;

import com.growalong.util.util.NetWorkUtil;

import java.io.IOException;

import aimi.android.com.MyApplication;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @ProjectName: videochat
 * @Package: com.growalong.android.net.retrofit
 * @ClassName: HttpCacheInterceptor
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2019/4/26 11:53
 * @Version: ${VERSION_NAME}
 */
public class HttpCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetWorkUtil.isNetworkConnected(MyApplication.appContext)) {
            request = request.newBuilder()
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (NetWorkUtil.isNetworkConnected(MyApplication.appContext)) {
            int maxAge = 0;
            // 有网络时, 不缓存, 最大保存时长为0
            response.newBuilder()
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Pragma")
                    .build();
        } else {
            // 无网络时，设置超时为4周
            int maxStale = 60 * 60 * 24 * 28;
            response.newBuilder()
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
        }
        return response;
    }
}
