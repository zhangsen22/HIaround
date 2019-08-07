package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.RewardDetailResponse;

public interface RewardDetailContract {

    interface Presenter extends IBasePresenter {
        void rewardDetailRefresh(int type, long minId);
        void rewardDetailLoadMore(int type, long minId);
    }
    interface View extends IBaseView<Presenter> {
        void rewardDetailRefreshSuccess(RewardDetailResponse rewardDetailResponse);
        void rewardDetailRefreshError();
        void rewardDetailLoadMoreSuccess(RewardDetailResponse rewardDetailResponse);
        void rewardDetailLoadMoreError();
    }
}
