package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.BuyResponse;

public interface BuyContract {
    interface Presenter extends IBasePresenter {
        void getBuyRefresh(long minId);
        void getBuyLoadMore(long minId);
    }
    interface View extends IBaseView<Presenter> {
        void getBuyRefreshSuccess(BuyResponse buyResponse);
        void getBuyRefreshError();
        void getBuyLoadMoreSuccess(BuyResponse buyResponse);
        void getBuyLoadMoreError();
    }
}
