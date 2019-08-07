package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.LargeAmountResponse;

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
