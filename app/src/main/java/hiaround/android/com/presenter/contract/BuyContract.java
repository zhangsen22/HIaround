package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.BuyAmountListResponse;

public interface BuyContract {
    interface Presenter extends IBasePresenter {
        void buyAmountList();
    }
    interface View extends IBaseView<Presenter> {
        void buyAmountListSuccess(BuyAmountListResponse buyResponse);
    }
}
