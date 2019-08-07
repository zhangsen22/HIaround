package hiaround.android.com.presenter.modle;

import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.net.retrofit.BaseRetrofitClient;
import hiaround.android.com.net.retrofit.exception.ModelExceptionMap;
import hiaround.android.com.net.retrofit.exception.ServerExceptionMap;
import hiaround.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class TiBiModle extends WalletModle{

    /**
     * 提币
     * @param addr
     * @param num
     * @param financePwd
     * @param time
     * @return
     */
    public Observable<BaseBean> withdraw(String addr,double num,String financePwd,long time){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .withdraw(addr,num,financePwd,time)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BaseBean>())
                .onErrorResumeNext(new ModelExceptionMap<BaseBean>());
    }
}
