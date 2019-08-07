package hiaround.android.com.presenter.modle;

import hiaround.android.com.modle.InvitationResponse;
import hiaround.android.com.net.retrofit.BaseRetrofitClient;
import hiaround.android.com.net.retrofit.exception.ModelExceptionMap;
import hiaround.android.com.net.retrofit.exception.ServerExceptionMap;
import hiaround.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class InvitationModle {

    /**
     * 推荐奖励
     * @param upUserId
     * @return
     */
    public Observable<InvitationResponse> recommendReward(long upUserId){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .recommendReward(upUserId)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<InvitationResponse>())
                .onErrorResumeNext(new ModelExceptionMap<InvitationResponse>());
    }
}
