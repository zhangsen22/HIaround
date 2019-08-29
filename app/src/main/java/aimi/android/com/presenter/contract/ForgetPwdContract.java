package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.ImageCodeResponse;
import aimi.android.com.modle.SmsCodeResponse;

public interface ForgetPwdContract {
    interface Presenter extends IBasePresenter {
        //发送验证码
        void senSenSmsCode(String phoneNum);
        //获取图片验证码
        void getImageCode(String phoneNum);
        //忘记密码
        void forgetPwd(String pwd, String phoneNum, String imageCode, String smsCode);
    }
    interface View extends IBaseView<Presenter> {
        //获取图片验证码成功
        void getImageCodeSuccess(ImageCodeResponse imageCodeResponse);
        //发送验证码成功
        void senSenSmsCodeSuccess(SmsCodeResponse smsCodeResponse);
        //忘记密码成功
        void forgetPwdSuccess(BaseBean baseBean);
    }
}
