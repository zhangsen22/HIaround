package hiaround.android.com.net.retrofit.exception;


import hiaround.android.com.modle.BaseBean;
import io.reactivex.functions.Function;

public class ServerExceptionMap<T extends BaseBean> implements Function<T, T> {
    @Override
    public T apply(T t) throws Exception {
        if (t != null && t.getRet() != 0) { //HTTP API 接口返回非 0则抛出异常
            throw new ModelException(t.getRet(), t.getMsgg());
        }
        return t;
    }
}
