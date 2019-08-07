package hiaround.android.com.net.retrofit;

import com.growalong.util.util.GALogger;

import java.util.concurrent.TimeUnit;

import hiaround.android.com.MyApplication;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @ProjectName: videochat
 * @Package: com.growalong.android.net.retrofit
 * @ClassName: BaseRetrofitClient
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2019/4/26 11:35
 * @Version: ${VERSION_NAME}
 */
public class BaseRetrofitClient {
    private static final String TAG = BaseRetrofitClient.class.getSimpleName();
    private static BaseRetrofitClient sInstance;
    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;
    private String hostAddress = null;

    public static BaseRetrofitClient getInstance() {
        if (null == sInstance) {
            synchronized (BaseRetrofitClient.class) {
                if (null == sInstance) {
                    sInstance = new BaseRetrofitClient();
                }
            }
        }
        return sInstance;
    }

    private BaseRetrofitClient() {
    }


    /* 生成 Retrofit 接口定义的 Observable 对象
     * @param service Retrofit接口类
     * */
    public <T> T create(final Class<T> rxJavaInterface) {
        if (mRetrofit == null || !hostAddress.equals(MyApplication.getHostAddress())) {
            init();
        }
        return mRetrofit.create(rxJavaInterface);
    }

    public void init() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new MyInterceptor())
                .build();
        hostAddress = MyApplication.getHostAddress();
        GALogger.d(TAG, "hostAddress   " + hostAddress);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(hostAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())) //Observable will execute on the 'io' scheduler
                .callFactory(mOkHttpClient)
                .build();
    }
}
