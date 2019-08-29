package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.WebChatEditModle;
import aimi.android.com.modle.WechatLoginModle;

public interface WebChatEditContract {

    interface Presenter extends IBasePresenter {
        //微信收款设置
        void wechat(long id,String name,String account,String base64Img,String empBase64Img,String financePwd,long time);
        //微信登录
        void wechatLogin(long paymentId);
    }
    interface View extends IBaseView<Presenter> {
        //微信收款设置成功
        void wechatSuccess(WebChatEditModle webChatEditModle);
        //微信登录成功
        void wechatLoginSuccess(WechatLoginModle wechatLoginModle);
        //微信登录失败
        void wechatLoginError();
    }
}
