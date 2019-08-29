package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;

public interface IdCastContract {

    interface Presenter extends IBasePresenter {
        //银行卡收款设置
        void bank(long id,String bankName,String subName,String name,String account,double dailyLimit,String financePwd,long time);
    }
    interface View extends IBaseView<Presenter> {
        //银行卡收款设置成功
        void bankSuccess(String name, String account);
    }
}
