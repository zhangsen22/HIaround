package hiaround.android.com.presenter.modle;

import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.net.retrofit.BaseRetrofitClient;
import hiaround.android.com.net.retrofit.exception.ModelExceptionMap;
import hiaround.android.com.net.retrofit.exception.ServerExceptionMap;
import hiaround.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class CenterModle {

    /**
     * 设置昵称
     * @param nickname
     * @return
     */
    public Observable<BaseBean> changeNickname(String nickname){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .changeNickname(nickname)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BaseBean>())
                .onErrorResumeNext(new ModelExceptionMap<BaseBean>());
    }
}
