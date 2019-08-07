package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.UsdtPriceResponse;

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
