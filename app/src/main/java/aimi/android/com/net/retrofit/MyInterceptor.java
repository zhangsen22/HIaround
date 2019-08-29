package aimi.android.com.net.retrofit;

import android.text.TextUtils;
import com.growalong.util.util.GALogger;
import java.io.IOException;
import java.util.List;
import aimi.android.com.app.Constants;
import aimi.android.com.util.SharedPreferencesUtils;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * 拦截器封装
 */
public class MyInterceptor implements Interceptor {
    private static final String TAG = "MyInterceptor";

    public MyInterceptor() {
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString();
        GALogger.d(TAG, "request url==" + url);
        String sessionId = SharedPreferencesUtils.getString(Constants.SESSIONID);

        /**配置请求头*/
        Request.Builder requestBuild = request.newBuilder()
//                .addHeader("caller", "app")
//                .addHeader("os", String.valueOf(android.os.Build.VERSION.SDK_INT))
//                .addHeader("platform", "android")
////                .addHeader("ver", VersionInfoUtil.getVersionCode(MyApplication.getContext())+"")
//                .addHeader("encrypt", "md5")
//                .addHeader("etag", "")
//                .addHeader("sid", sid)
//                .addHeader("userId", userId)
//                .addHeader("token", token)
//                .addHeader("adminUserId", "")
                ;
        if(!TextUtils.isEmpty(sessionId) && !url.contains(ApiConstants.login)){
            requestBuild.addHeader("cookie", sessionId);
        }

        okhttp3.Response response = chain.proceed(requestBuild.build());
            /**
             * 保存sessionId
             */
            Headers headers = response.headers();//response为okhttp请求后的响应
            List cookies = headers.values("Set-Cookie");
            if (cookies != null && cookies.size() > 0) {
                String session = (String) cookies.get(0);
                String sessionid = session.substring(0, session.indexOf(";"));
                if(url.contains(ApiConstants.login)) {
                    SharedPreferencesUtils.putString(Constants.SESSIONID, sessionid);
                }
                GALogger.d(TAG, "sessionid   is have   " + sessionid);
            } else {
                GALogger.d(TAG, "sessionid   is  no   have");
            }
//        String responseEncodeBody =response.body().string();
//        GALogger.d(TAG, "before response==" + responseEncodeBody);
//        GALogger.d(TAG, "response.code()" + response.toString());
//        if (response.code() == 200) {
//            String responseEncodeBody = response.body().string();
//            GALogger.d(TAG, "before response==" + responseEncodeBody);
//            BaseBean baseBean = GsonUtil.getInstance().getServerBean(responseEncodeBody, BaseBean.class);
//            if (baseBean != null) {
//                    if (baseBean.getRet() == 0) {//需要先登录才能进行此操作
//                        ResponseBody responseBody = ResponseBody.create(response.body().contentType(), "");
//                        return response.newBuilder().code(baseBean.getRet()).body(responseBody).build();
//                }
//            }
//        }
        return response;
    }
}

