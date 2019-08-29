package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.LargeAmountResponse;

public interface LargeAmountContract {

    interface Presenter extends IBasePresenter {
        void getHugeBillinfoRefresh(long minId);
        void getHugeBillinfoLoadMore(long minId);
    }
    interface View extends IBaseView<Presenter> {
        void getHugeBillinfoRefreshSuccess(LargeAmountResponse largeAmountResponse);
        void getHugeBillinfoRefreshError();
        void getHugeBillinfoLoadMoreSuccess(LargeAmountResponse largeAmountResponse);
        void getHugeBillinfoLoadMoreError();
    }
}
