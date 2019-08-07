package hiaround.android.com.presenter.modle;

import hiaround.android.com.modle.PaySetupModelWebChat;
import hiaround.android.com.net.retrofit.BaseRetrofitClient;
import hiaround.android.com.net.retrofit.exception.ModelExceptionMap;
import hiaround.android.com.net.retrofit.exception.ServerExceptionMap;
import hiaround.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class WebChatListModle extends SetDefaultPayBaseModle {

    /**
     * 获取自己的收款信息
     * 微信
     * @param type
     * @return
     */
    public Observable<PaySetupModelWebChat> paysetupWebChat(int type){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .paysetupWebChat(type)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<PaySetupModelWebChat>())
                .onErrorResumeNext(new ModelExceptionMap<PaySetupModelWebChat>());
    }
}
