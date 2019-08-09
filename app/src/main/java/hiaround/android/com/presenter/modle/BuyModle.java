package hiaround.android.com.presenter.modle;

import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.modle.BuyAmountListResponse;
import hiaround.android.com.modle.BuyBusinessResponse;
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

    /**
     * 一键购买
     * @return
     */
    public Observable<BuyBusinessResponse> quickBuy(String amount, int type){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .quickBuy(amount,type)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BuyBusinessResponse>())
                .onErrorResumeNext(new ModelExceptionMap<BuyBusinessResponse>());
    }

    /**
     * 一键出售
     * @param type
     * @param financePwd
     * @param rmb
     * @param time
     * @return
     */
    public Observable<BaseBean> quickSell(int type,String financePwd,double rmb,long time){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .quickSell(type,financePwd,rmb,time)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BaseBean>())
                .onErrorResumeNext(new ModelExceptionMap<BaseBean>());
    }

}
