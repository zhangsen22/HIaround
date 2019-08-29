package aimi.android.com.presenter.modle;

import aimi.android.com.modle.RewardLogResponse;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class AwardDetailsModle {

    /**
     * 获取奖励记录
     * @return
     */
    public Observable<RewardLogResponse> rewardLog(){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .rewardLog()
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<RewardLogResponse>())
                .onErrorResumeNext(new ModelExceptionMap<RewardLogResponse>());
    }
}
