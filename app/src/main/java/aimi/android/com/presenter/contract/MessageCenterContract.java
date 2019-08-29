package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.MessageCenterResponse;

public interface MessageCenterContract {

    interface Presenter extends IBasePresenter {
        void msgCenterRefresh(long minId);
        void msgCenterLoadMore(long minId);
    }
    interface View extends IBaseView<Presenter> {
        void msgCenterRefreshSuccess(MessageCenterResponse messageCenterResponse);
        void msgCenterRefreshError();
        void msgCenterLoadMoreSuccess(MessageCenterResponse messageCenterResponse);
        void msgCenterLoadMoreError();
    }
}
