package aimi.android.com.presenter.modle;

import aimi.android.com.modle.BuyBusinessResponse;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class BusinessBuyModle {

    /**
     * 购买
     * @param billId
     * @param num
     * @param type
     * @return
     */
    public Observable<BuyBusinessResponse> buy(long billId, double num, int type){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .buy(billId,num,type)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BuyBusinessResponse>())
                .onErrorResumeNext(new ModelExceptionMap<BuyBusinessResponse>());
    }
}
