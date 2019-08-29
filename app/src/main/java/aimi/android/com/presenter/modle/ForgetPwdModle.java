package aimi.android.com.presenter.modle;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ForgetPwdModle extends GetSmsCodeModle{

    /**
     * 忘记密码
     * @param pwd
     * @param phoneNum
     * @param imageCode
     * @param smsCode
     * @return
     */
    public Observable<BaseBean> forgetPwd(String pwd, String phoneNum, String imageCode, String smsCode){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .forgetPwd(pwd,"86"+phoneNum,imageCode,smsCode)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BaseBean>())
                .onErrorResumeNext(new ModelExceptionMap<BaseBean>());
    }
}
