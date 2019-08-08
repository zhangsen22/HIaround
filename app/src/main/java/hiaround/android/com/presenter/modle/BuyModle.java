package hiaround.android.com.presenter.modle;

import hiaround.android.com.modle.BuyAmountListResponse;
import hiaround.android.com.net.retrofit.BaseRetrofitClient;
import hiaround.android.com.net.retrofit.exception.ModelExceptionMap;
import hiaround.android.com.net.retrofit.exception.ServerExceptionMap;
import hiaround.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class BuyModle {

    /**
     * 交易 我要买  获取购买金额列表
     * @return
     */
    public Observable<BuyAmountListResponse> buyAmountList(){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .buyAmountList()
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BuyAmountListResponse>())
                .onErrorResumeNext(new ModelExceptionMap<BuyAmountListResponse>());
    }

}
