package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.BuyResponse;

public interface SellContract {

    interface Presenter extends IBasePresenter {
        void getSellRefresh(long minId);
        void getSellLoadMore(long minId);
    }
    interface View extends IBaseView<Presenter> {
        void getSellRefreshSuccess(BuyResponse buyResponse);
        void getSellRefreshError();
        void getSellLoadMoreSuccess(BuyResponse buyResponse);
        void getSellLoadMoreError();
    }
}
