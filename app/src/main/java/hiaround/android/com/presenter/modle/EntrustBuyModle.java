package hiaround.android.com.presenter.modle;

import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.net.retrofit.BaseRetrofitClient;
import hiaround.android.com.net.retrofit.exception.ModelExceptionMap;
import hiaround.android.com.net.retrofit.exception.ServerExceptionMap;
import hiaround.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class EntrustBuyModle extends WalletModle{

    /**
     * 委托购买
     * @param price
     * @param minNum
     * @param maxNum
     * @param financePwd
     * @param time
     * @return
     */
    public Observable<BaseBean> putUpBuy(double price, double minNum, double maxNum, String financePwd, long time){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .putUpBuy(price,minNum,maxNum,financePwd,time)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BaseBean>())
                .onErrorResumeNext(new ModelExceptionMap<BaseBean>());
    }
}
