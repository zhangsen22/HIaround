package hiaround.android.com.presenter.modle;

import hiaround.android.com.modle.RewardDetailResponse;
import hiaround.android.com.net.retrofit.BaseRetrofitClient;
import hiaround.android.com.net.retrofit.exception.ModelExceptionMap;
import hiaround.android.com.net.retrofit.exception.ServerExceptionMap;
import hiaround.android.com.net.retrofit.service.ApiServices;
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
