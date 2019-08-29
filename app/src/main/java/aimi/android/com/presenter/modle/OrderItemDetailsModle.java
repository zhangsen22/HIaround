package aimi.android.com.presenter.modle;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.MyEntrustinfoResponse;
import aimi.android.com.modle.MySellOrBuyinfoResponse;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class OrderItemDetailsModle extends CancelOrderModle{

    /**
     * 获取我的卖出交易信息
     * @param type
     * @param minId
     * @return
     */
    public Observable<MySellOrBuyinfoResponse> mySellinfo(int type, long minId){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .mySellinfo(type,minId)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<MySellOrBuyinfoResponse>())
                .onErrorResumeNext(new ModelExceptionMap<MySellOrBuyinfoResponse>());
    }

    /**
     * 获取我的买入交易信息
     * @param type
     * @param minId
     * @return
     */
    public Observable<MySellOrBuyinfoResponse> myBuyinfo(int type, long minId){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .myBuyinfo(type,minId)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<MySellOrBuyinfoResponse>())
                .onErrorResumeNext(new ModelExceptionMap<MySellOrBuyinfoResponse>());
    }

    /**
     * 获取我的委托单交易信息
     * @param type
     * @param minId
     * @return
     */
    public Observable<MyEntrustinfoResponse> myBillInfo(int type, long minId){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .myBillInfo(type,minId)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<MyEntrustinfoResponse>())
                .onErrorResumeNext(new ModelExceptionMap<MyEntrustinfoResponse>());
    }

    /**
     * 撤销委托
     * @param billId
     * @param type
     * @return
     */
    public Observable<BaseBean> cancel(long billId, int type){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .cancel(billId,type)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BaseBean>())
                .onErrorResumeNext(new ModelExceptionMap<BaseBean>());
    }

    /**
     * 订单申诉
     * @param tradeId
     * @return
     */
    public Observable<BaseBean> appeal(String tradeId){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .appeal(tradeId)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BaseBean>())
                .onErrorResumeNext(new ModelExceptionMap<BaseBean>());
    }
}
