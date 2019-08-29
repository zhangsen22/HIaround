package aimi.android.com.net.retrofit.exception;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 将各种异常转换为 ModelException
 * @param <T>
 */
public class ModelExceptionMap<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(Throwable throwable) {
        return Observable.error(ModelExceptionBuilder.build(throwable));
    }
}
