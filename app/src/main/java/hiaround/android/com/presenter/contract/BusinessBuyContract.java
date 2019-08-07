package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.BuyBusinessResponse;

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
