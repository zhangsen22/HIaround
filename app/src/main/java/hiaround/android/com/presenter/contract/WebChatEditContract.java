package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.WebChatEditModle;
import hiaround.android.com.modle.WechatLoginModle;

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
