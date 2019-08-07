package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.WalletResponse;

public interface PropertyContract {
    interface Presenter extends IBasePresenter {
        //资产查询
        void getInfo();
    }
    interface View extends IBaseView<Presenter> {

        //资产查询成功
        void getInfoSuccess(WalletResponse walletResponse);
    }
}
