package aimi.android.com.presenter.modle;

import aimi.android.com.modle.RewardDetailResponse;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class RewardDetailModle {

    public Observable<RewardDetailResponse> rewardDetail(int type, long minId){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .rewardDetail(type,minId)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<RewardDetailResponse>())
                .onErrorResumeNext(new ModelExceptionMap<RewardDetailResponse>());
    }
}
