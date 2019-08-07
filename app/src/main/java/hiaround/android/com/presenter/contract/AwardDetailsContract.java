package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.RewardLogResponse;

public interface AwardDetailsContract {
    interface Presenter extends IBasePresenter {
        //获取奖励记录
        void rewardLog();
    }
    interface View extends IBaseView<Presenter> {
        //获取奖励记录成功
        void rewardLogSuccess(RewardLogResponse rewardLogResponse);
    }
}
