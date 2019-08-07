package hiaround.android.com.presenter.modle;

import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.net.retrofit.BaseRetrofitClient;
import hiaround.android.com.net.retrofit.exception.ModelExceptionMap;
import hiaround.android.com.net.retrofit.exception.ServerExceptionMap;
import hiaround.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class CancelOrderModle {
    /**
     * 取消订单
     * @param tradeId
     * @return
     */
    public Observable<BaseBean> ordercancel(String tradeId){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .ordercancel(tradeId)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BaseBean>())
                .onErrorResumeNext(new ModelExceptionMap<BaseBean>());
    }
}
