package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.BuyAmountListResponse;
import aimi.android.com.modle.BuyBusinessResponse;

public interface BuyContract {
    interface Presenter extends IBasePresenter {
        void buyAmountList();
        void quickBuy(String amount, int type);
    }
    interface View extends IBaseView<Presenter> {
        void buyAmountListSuccess(BuyAmountListResponse buyResponse);
        void buyAmountListError();
        void quickBuySuccess(BuyBusinessResponse buyBusinessResponse);
    }
}
