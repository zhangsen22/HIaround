package hiaround.android.com.net.retrofit.exception;

/**
 * 业务异常
 * 包括：网络请求异常，HTTP接口返回结果的业务异常
 */

public class ModelException extends Exception {

    public int mCode; //错误代码
    public String mMessage; //错误消息

    public ModelException(Throwable throwable) {
        super(throwable);
    }

    public ModelException(int code, String message) {
        super();
        this.mCode = code;
        this.mMessage = message;
    }
}