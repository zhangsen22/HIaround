package aimi.android.com.presenter.modle;
import aimi.android.com.modle.PaySetupModelLaCara;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class LaCaraListModle extends SetDefaultPayBaseModle {


    /**
     * 获取自己的收款信息
     * 拉卡拉
     * @param type
     * @return
     */
    public Observable<PaySetupModelLaCara> paysetupLaCara(int type){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .paysetupLaCara(type)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<PaySetupModelLaCara>())
                .onErrorResumeNext(new ModelExceptionMap<PaySetupModelLaCara>());
    }

}
