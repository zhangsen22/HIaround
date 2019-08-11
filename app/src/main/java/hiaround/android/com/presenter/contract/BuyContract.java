package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.BuyAmountListResponse;
import hiaround.android.com.modle.BuyBusinessResponse;

public interface BuyContract {
    interface Presenter extends IBasePresenter {
        void buyAmountList();
        void quickBuy(String amount, int type);
    }
    interface View extends IBaseView<Presenter> {
        void buyAmountListSuccess(BuyAmountListResponse buyResponse);
        void quickBuySuccess(BuyBusinessResponse buyBusinessResponse);
    }
}
