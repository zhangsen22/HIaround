package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.FinanceLogResponse;

public interface FinancialRecordsContract {
    interface Presenter extends IBasePresenter {
        void financeLogRefresh(long minId);
        void financeLogLoadMore(long minId);
    }
    interface View extends IBaseView<Presenter> {
        void financeLogRefreshSuccess(FinanceLogResponse financeLogResponse);
        void financeLogRefreshError();
        void financeLogLoadMoreSuccess(FinanceLogResponse financeLogResponse);
        void financeLogLoadMoreError();
    }
}
