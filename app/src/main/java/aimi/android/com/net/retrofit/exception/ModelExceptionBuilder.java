package aimi.android.com.net.retrofit.exception;

import com.growalong.util.util.NetWorkUtil;

import aimi.android.com.MyApplication;
import retrofit2.HttpException;

/**
 * 根据各种异常统一创建业务异常
 * 1）业务异常
 * 2）HTTP API 业务异常
 * 3）网络异常（HTTP请求和响应异常）
 * 4) 未知异常
 */
public class ModelExceptionBuilder {

    public static ModelException build(Throwable e) {
        if (e instanceof ModelException) {
            ModelException m = (ModelException) e;
            String errorDesc = HttpErrorCode.getErrorMessage(m.mCode, m.mMessage);
            m.mMessage = errorDesc;
            return m;
        } else if(e instanceof HttpException){
            HttpException exception = (HttpException)e;
            ModelException m = new ModelException(exception.code(),HttpErrorCode.getErrorMessage(exception.code(), exception.message()));
            return m;
        } else {
            ModelException m = new ModelException(e);
            if (NetWorkUtil.isNetworkConnected(MyApplication.appContext)) {
                String errorDesc = HttpErrorCode.getErrorMessage(m.mCode, m.mMessage);
                m.mMessage = errorDesc;
            }else {
                String errorDesc = HttpErrorCode.getErrorMessage(1009, m.mMessage);
                m.mMessage = errorDesc;
            }
            return m;
        }
    }
}
