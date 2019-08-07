package hiaround.android.com.presenter.modle;

import hiaround.android.com.modle.BuyResponse;
import hiaround.android.com.modle.LargeAmountResponse;
import hiaround.android.com.net.retrofit.BaseRetrofitClient;
import hiaround.android.com.net.retrofit.exception.ModelExceptionMap;
import hiaround.android.com.net.retrofit.exception.ServerExceptionMap;
import hiaround.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class BuyModle {

    /**
     * 获取挂取的卖单信息
     * @param minId
     * @return
     */
    public Observable<BuyResponse> getSellinfo(long minId){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .getSellinfo(minId)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BuyResponse>())
                .onErrorResumeNext(new ModelExceptionMap<BuyResponse>());
    }

    /**
     * 获取挂取的卖单信息
     * @param minId
     * @return
     */
    public Observable<BuyResponse> getBuyinfo(long minId){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .getBuyinfo(minId)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BuyResponse>())
                .onErrorResumeNext(new ModelExceptionMap<BuyResponse>());
    }

    /**
     * 获取大额提现订单
     * @param minId
     * @return
     */
    public Observable<LargeAmountResponse> getHugeBillinfo(long minId){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .getHugeBillinfo(minId)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<LargeAmountResponse>())
                .onErrorResumeNext(new ModelExceptionMap<LargeAmountResponse>());
    }
}
