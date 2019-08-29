package aimi.android.com.presenter.modle;

import com.growalong.util.util.Md5Utils;

import aimi.android.com.app.AccountInfo;
import aimi.android.com.app.AccountManager;
import aimi.android.com.modle.RegistResponse;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.net.retrofit.exception.ModelExceptionBuilder;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RegistModle extends GetSmsCodeModle{

    /**
     * 手机号注册成功后再去登录
     * @param phoneNum
     * @param invitedCode
     * @param imageCode
     * @param smsCode
     * @param pwd
     * @param time
     * @param password
     * @return
     */
    public Observable<AccountInfo> registerAndLogin(final String phoneNum
            , String invitedCode
            , String imageCode
            , String smsCode
            , String pwd
            , final long time, final String password){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .regist("86"+phoneNum,invitedCode,imageCode,smsCode,pwd)
                .flatMap(new Function<RegistResponse, ObservableSource<AccountInfo>>() {
                    @Override
                    public ObservableSource<AccountInfo> apply(RegistResponse registResponse){
                        if(registResponse != null){
                            int code = registResponse.getRet();
                            if(code == 0){
                                return BaseRetrofitClient.getInstance().create(ApiServices.class).login("86"+phoneNum,Md5Utils.getMD5(password + time),time);
                            }else {
                                return Observable.error(ModelExceptionBuilder.build(new ModelException(code, registResponse.getMsgg())));
                            }
                        }else{
                            return Observable.error(ModelExceptionBuilder.build(new ModelException(Integer.MAX_VALUE, null)));
                        }
                    }
                }).map(new Function<AccountInfo, AccountInfo>() {
                    @Override
                    public AccountInfo apply(AccountInfo accountInfo){
                        if(accountInfo != null){
                            int code = accountInfo.getRet();
                            if(code == 0){
                                accountInfo.setPhoneNumber(phoneNum);
                                accountInfo.setPassword(password);
                                AccountManager.getInstance().saveAccountInfoFormModel(accountInfo);
                                return accountInfo;
                            }else {
                                return accountInfo;
                            }
                        }else{
                            return accountInfo;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<AccountInfo>())
                .onErrorResumeNext(new ModelExceptionMap<AccountInfo>());
    }
}
