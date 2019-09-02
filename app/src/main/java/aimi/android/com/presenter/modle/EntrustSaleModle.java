package aimi.android.com.presenter.modle;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class EntrustSaleModle extends WalletModle {

    /**
     * 委托出售
     * @param price
     * @param minNum
     * @param maxNum
     * @param supporAli
     * @param supportWechat
     * @param supportBank
     * @param financePwd
     * @param time
     * @return
     */
    public Observable<BaseBean> putUpSell(double price, double minNum, double maxNum, boolean supporAli, boolean supportWechat, boolean supportBank, boolean supportCloud, boolean supportLakala, String financePwd, long time){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .putUpSell(price,minNum,maxNum,supporAli,supportWechat,supportBank,supportCloud,supportLakala,financePwd,time)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BaseBean>())
                .onErrorResumeNext(new ModelExceptionMap<BaseBean>());
    }
}
