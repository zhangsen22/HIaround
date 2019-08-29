package aimi.android.com.presenter.modle;

import aimi.android.com.modle.ImageCodeResponse;
import aimi.android.com.modle.SmsCodeResponse;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class GetSmsCodeModle {

    /**
     * 获取图片验证码
     * @param phoneNum
     * @return
     */
    public Observable<ImageCodeResponse> getImageCode(String phoneNum){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .getImageCode("86"+phoneNum)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<ImageCodeResponse>())
                .onErrorResumeNext(new ModelExceptionMap<ImageCodeResponse>());
    }


    /**
     * 发送验证码接口
     * @param phoneNum
     * @return
     */
    public Observable<SmsCodeResponse> senSenSmsCode(String phoneNum){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .senSenSmsCode("86"+phoneNum)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<SmsCodeResponse>())
                .onErrorResumeNext(new ModelExceptionMap<SmsCodeResponse>());
    }
}
