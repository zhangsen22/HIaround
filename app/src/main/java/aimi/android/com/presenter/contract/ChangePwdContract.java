package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.SmsCodeResponse;

public interface ChangePwdContract {

    interface Presenter extends IBasePresenter {
        //发送验证码
        void senSenSmsCode(String phoneNum);
        //修改密码
        void changePwd(String financePwd, String smsCode);
    }
    interface View extends IBaseView<Presenter> {
        //发送验证码成功
        void senSenSmsCodeSuccess(SmsCodeResponse smsCodeResponse);
        //修改密码
        void changePwdSuccess(BaseBean baseBean);
    }
}
