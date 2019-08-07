package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.MessageCenterResponse;

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
