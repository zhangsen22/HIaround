package hiaround.android.com.presenter;

import android.text.TextUtils;

import hiaround.android.com.modle.WebChatEditModle;
import hiaround.android.com.modle.WechatLoginModle;
import hiaround.android.com.net.retrofit.ModelResultObserver;
import hiaround.android.com.net.retrofit.exception.ModelException;
import hiaround.android.com.presenter.contract.WebChatEditContract;
import hiaround.android.com.presenter.modle.PaySettingModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class WebChatEditPresenter implements WebChatEditContract.Presenter{

    private WebChatEditContract.View mView;
    private PaySettingModle mModel;

    public WebChatEditPresenter(WebChatEditContract.View view, PaySettingModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void starLoadData() {

    }

    @Override
    public void wechat(long id,final String name, final String account, final String base64Img, String empBase64Img, String financePwd, long time) {
        mView.showLoading();
        mModel.wechat(id,name,account,base64Img,empBase64Img,financePwd,time).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<WebChatEditModle>() {
                    @Override
                    public void onSuccess(WebChatEditModle webChatEditModle) {
                        mView.wechatSuccess(webChatEditModle);
                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void wechatLogin(long paymentId) {
        mModel.wechatLogin(paymentId).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<WechatLoginModle>() {
                    @Override
                    public void onSuccess(WechatLoginModle wechatLoginModle) {
                        if(wechatLoginModle != null){
                            String loginCode = wechatLoginModle.getLoginCode();
                            if(!TextUtils.isEmpty(loginCode)){
                                mView.wechatLoginSuccess(wechatLoginModle);
                            }else {
                                mView.wechatLoginError();
                            }
                        }else {
                            mView.wechatLoginError();
                        }
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.wechatLoginError();
                    }
                });
    }
}
