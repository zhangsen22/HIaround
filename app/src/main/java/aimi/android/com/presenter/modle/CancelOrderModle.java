package aimi.android.com.presenter.modle;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
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
