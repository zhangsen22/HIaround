package hiaround.android.com.presenter.modle;

import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.net.retrofit.BaseRetrofitClient;
import hiaround.android.com.net.retrofit.exception.ModelExceptionMap;
import hiaround.android.com.net.retrofit.exception.ServerExceptionMap;
import hiaround.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SetDefaultPayBaseModle {

    /**
     * 设置默认收款设置
     * @param type
     * @param id
     * @param financePwd
     * @param time
     * @return
     */
    public Observable<BaseBean> setDefaultPay(int type, long id, String financePwd, long time){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .setDefaultPay(type,id,financePwd,time)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BaseBean>())
                .onErrorResumeNext(new ModelExceptionMap<BaseBean>());
    }

    /**
     * 删除收款设置
     * @param type
     * @param id
     * @param financePwd
     * @param time
     * @return
     */
    public Observable<BaseBean> detelePay(int type,long id,String financePwd,long time){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .detelePay(type,id,financePwd,time)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BaseBean>())
                .onErrorResumeNext(new ModelExceptionMap<BaseBean>());
    }
}
