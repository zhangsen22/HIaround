package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.WalletResponse;

public interface BusinessContainerContract {
    interface Presenter extends IBasePresenter {
        //资产查询
        void getInfo();
    }
    interface View extends IBaseView<Presenter> {

        //资产查询成功
        void getInfoSuccess(WalletResponse walletResponse);
    }
}
