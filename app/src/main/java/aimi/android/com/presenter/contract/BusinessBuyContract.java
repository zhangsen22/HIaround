package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.BuyBusinessResponse;

public interface BusinessBuyContract {

    interface Presenter extends IBasePresenter {
        //购买
        void buy(long billId, double num, int type);
    }
    interface View extends IBaseView<Presenter> {
        //购买成功
        void buySuccess(BuyBusinessResponse buyBusinessResponse,int type);
    }
}
