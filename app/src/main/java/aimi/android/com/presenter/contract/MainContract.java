package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.UsdtPriceResponse;

public interface MainContract {
    interface Presenter extends IBasePresenter {
        //查看USDT最新价格
        void usdtPrice();
    }
    interface View extends IBaseView<Presenter> {
        //查看USDT最新价格成功
        void usdtPriceSuccess(UsdtPriceResponse usdtPriceResponse);
        void usdtPriceError();
    }
}
