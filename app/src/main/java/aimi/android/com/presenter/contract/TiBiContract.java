package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.WalletResponse;

public interface TiBiContract {

    interface Presenter extends IBasePresenter {
        //提币
        void withdraw(String addr,double num,String financePwd,long time);
        //资产查询
        void getInfo();
    }
    interface View extends IBaseView<Presenter> {
        //提币  成功
        void withdrawSuccess(BaseBean baseBean);
        //资产查询成功
        void getInfoSuccess(WalletResponse walletResponse);
    }
}
