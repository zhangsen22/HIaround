package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.WalletResponse;

public interface RansferOfFundsContract {
    interface Presenter extends IBasePresenter {
        //资金划转
        void transfer(int type,double num,String financePwd,long time);
        //资产查询
        void getInfo();
    }
    interface View extends IBaseView<Presenter> {
        //资金划转成功
        void transferSuccess(WalletResponse walletResponse);
        //资产查询成功
        void getInfoSuccess(WalletResponse walletResponse);
    }
}
