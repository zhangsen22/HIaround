package aimi.android.com.presenter.modle;

import aimi.android.com.modle.PaySetupModelYunShanFu;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class YunShanFuListModle extends SetDefaultPayBaseModle {


    /**
     * 获取自己的收款信息
     * 云闪付
     * @param type
     * @return
     */
    public Observable<PaySetupModelYunShanFu> paysetupYunShanFu(int type){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .paysetupYunShanFu(type)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<PaySetupModelYunShanFu>())
                .onErrorResumeNext(new ModelExceptionMap<PaySetupModelYunShanFu>());
    }

}
