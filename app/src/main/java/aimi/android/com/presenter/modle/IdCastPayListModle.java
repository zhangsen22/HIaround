package aimi.android.com.presenter.modle;

import aimi.android.com.modle.PaySetupModelBank;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class IdCastPayListModle extends SetDefaultPayBaseModle{

    /**
     * 获取自己的收款信息
     * 银行卡
     * @param type
     * @return
     */
    public Observable<PaySetupModelBank> paysetupBank(int type){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .paysetupBank(type)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<PaySetupModelBank>())
                .onErrorResumeNext(new ModelExceptionMap<PaySetupModelBank>());
    }
}
