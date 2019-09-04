package aimi.android.com.presenter.modle;

import aimi.android.com.modle.PaySetupModelWebChat;
import aimi.android.com.modle.WebChatEditModle;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
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


    /**
     * 微信重新编辑
     * @param id
     * @return
     */
    public Observable<WebChatEditModle> reWechat(long id){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .reWechat(id)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<WebChatEditModle>())
                .onErrorResumeNext(new ModelExceptionMap<WebChatEditModle>());
    }
}
