package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.SellResponse;
import aimi.android.com.modle.WalletResponse;

public interface BusinessSellContract {
    interface Presenter extends IBasePresenter {
        //出售
        void sell(long billId,double num,int type,String financePwd,long time);
        //资产查询
        void getInfo();
    }
    interface View extends IBaseView<Presenter> {
        //出售成功
        void sellSuccess(SellResponse sellResponse);
        //资产查询成功
        void getInfoSuccess(WalletResponse walletResponse);
    }
}
