package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.SellResponse;
import hiaround.android.com.modle.WalletResponse;

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
