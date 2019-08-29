package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.FinanceLogResponse;

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
