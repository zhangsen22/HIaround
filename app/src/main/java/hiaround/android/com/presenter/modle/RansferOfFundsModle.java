package hiaround.android.com.presenter.modle;

import hiaround.android.com.modle.WalletResponse;
import hiaround.android.com.net.retrofit.BaseRetrofitClient;
import hiaround.android.com.net.retrofit.exception.ModelExceptionMap;
import hiaround.android.com.net.retrofit.exception.ServerExceptionMap;
import hiaround.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class RansferOfFundsModle extends WalletModle{

    /**
     *资金划转
     * @param type
     * @param num
     * @param financePwd
     * @param time
     * @return
     */
    public Observable<WalletResponse> transfer(int type,double num,String financePwd,long time){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .transfer(type,num,financePwd,time)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<WalletResponse>())
                .onErrorResumeNext(new ModelExceptionMap<WalletResponse>());
    }
}
