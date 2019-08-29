package aimi.android.com.presenter.modle;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.BuyAmountListResponse;
import aimi.android.com.modle.BuyBusinessResponse;
import aimi.android.com.modle.SellLimitResponse;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
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

    /**
     * 一键出售区间值
     * @return
     */
    public Observable<SellLimitResponse> sellLimit(){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .sellLimit()
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<SellLimitResponse>())
                .onErrorResumeNext(new ModelExceptionMap<SellLimitResponse>());
    }

}
